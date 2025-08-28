package org.example;


import org.example.community.Community;
import org.example.model.OptionForQuestion;
import org.example.model.Question;
import org.example.model.Survey;
import org.telegram.telegrambots.meta.api.objects.polls.PollAnswer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SurveyEngine {
    private static final String ERROR_ACTIVE_SURVEY = "יש כבר סקר פעיל. סגור את הסקר הנוכחי לפני פתיחת סקר חדש.";
    private static final String ERROR_MIN_COMMUNITY = "נדרשים לפחות 3 חברים בקהילה כדי להתחיל סקר.";
    private static final String ERROR_SURVEY_NULL = "Survey is null";
    private static final String ERROR_QUESTIONS_EMPTY = "Survey has no questions";
    private static final String LOG_PREFIX = "[SurveyEngine] ";
    private static final String LOG_START = LOG_PREFIX + "מתחיל סקר...";
    private static final String LOG_SENT = LOG_PREFIX + "נשלחה שאלה #%d ל-chatId=%d (messageId=%d)";
    private static final String LOG_ALL_ANSWERED = LOG_PREFIX + "כולם ענו לכל השאלות. סוגר מיידית.";


    private TelegramPollEngine telegramPollEngine;
    private Community community;
    private SurveyState surveyState;
    private SurveyResult surveyResult;

    public SurveyEngine(TelegramPollEngine telegramPollEngine, Community community) {
        this.telegramPollEngine = telegramPollEngine;
        this.community = community;
        this.surveyState = new SurveyState();
    }

    public boolean isSurveyActive() {
        return this.surveyState.isSurveyOpen();
    }

    public void startSurvey(Survey survey) {
        validateStartPreconditions(survey);
        int communitySize = this.community.size();

        this.surveyState.resetForNewSurvey(survey, communitySize);
        this.surveyResult = new SurveyResult(
                this.surveyState.getOptionCountsByQuestionIndex(),
                this.surveyState.getAnsweredUserIdsByQuestionIndex()
        );

        prepareCountersForQuestions(survey);

        System.out.println(LOG_START);
        sendAllQuestionsToAllMembers(survey);
    }

    private void validateStartPreconditions(Survey survey) {
        if (this.surveyState.isSurveyOpen()) {
            throw new IllegalStateException(ERROR_ACTIVE_SURVEY);
        }
        if (this.community == null || this.community.size() < 3) {
            throw new IllegalStateException(ERROR_MIN_COMMUNITY);
        }
        if (survey == null) {
            throw new IllegalArgumentException(ERROR_SURVEY_NULL);
        }
        if (survey.getQuestions() == null || survey.getQuestions().isEmpty()) {
            throw new IllegalArgumentException(ERROR_QUESTIONS_EMPTY);
        }
    }

    private void prepareCountersForQuestions(Survey survey) {
        int numQuestions = survey.getQuestions().size();
        int qIndex = 0;
        while (qIndex < numQuestions) {
            List<OptionForQuestion> opts = survey.getQuestions().get(qIndex).getOptions();
            int optionsCount = (opts == null) ? 0 : opts.size();
            this.surveyState.prepareQuestionBuckets(qIndex, optionsCount);
            qIndex = qIndex + 1;
        }
    }

    private void sendAllQuestionsToAllMembers(Survey survey) {
        Set<Long> allChatIds = this.community.getAllChetId();
        int questionsCount = survey.getQuestions().size();

        for (Long chatId : allChatIds) {
            int qIndex = 0;
            while (qIndex < questionsCount) {
                Question q = survey.getQuestions().get(qIndex);
                String questionText = q.getText();
                List<String> optionTexts = buildOptionTexts(q.getOptions());

                try {
                    PollSendResult result = this.telegramPollEngine.sendQuestionPoll(chatId, questionText, optionTexts);
                    rememberMessageAndPollIds(chatId, qIndex, result);
                    System.out.println(String.format(LOG_SENT, qIndex, chatId, result.getMessageId()));
                } catch (Exception e) {

                }
                qIndex = qIndex + 1;
            }
        }
    }

    private List<String> buildOptionTexts(List<OptionForQuestion> options) {
        List<String> out = new ArrayList<String>();
        if (options == null) return out;
        int i = 0;
        while (i < options.size()) {
            out.add(options.get(i).getText());
            i = i + 1;
        }
        return out;
    }

    private void rememberMessageAndPollIds(long chatId, int questionIndex, PollSendResult result) {
        // pollId -> questionIndex
        if (result.getPollId() != null) {
            this.surveyState.getPollIdToQuestionIndex().put(result.getPollId(), questionIndex);
        }
        // chatId -> questionIndex -> messageId
        Map<Integer, Integer> perQuestion = this.surveyState.getMessageIdByChatIdAndQuestion().get(chatId);
        if (perQuestion == null) {
            perQuestion = new java.util.HashMap<Integer, Integer>();
            this.surveyState.getMessageIdByChatIdAndQuestion().put(chatId, perQuestion);
        }
        perQuestion.put(questionIndex, result.getMessageId());
    }

    public void onPollAnswer(PollAnswer pollAnswer) {
        if (!this.surveyState.isSurveyOpen()) return;
        if (pollAnswer == null) return;

        String pollId = pollAnswer.getPollId();
        Integer questionIndex = this.surveyState.getPollIdToQuestionIndex().get(pollId);
        if (questionIndex == null) return;

        long userId = pollAnswer.getUser().getId();

        int chosenOptionIndex = -1;
        java.util.List<Integer> ids = pollAnswer.getOptionIds();
        if (ids != null && !ids.isEmpty()) {
            chosenOptionIndex = ids.get(0);
        }
        if (chosenOptionIndex < 0) return;

        if (this.surveyResult.didUserAlreadyAnswer(questionIndex, userId)) {
            return;
        }

        this.surveyResult.markUserAnswered(questionIndex, userId);
        this.surveyResult.incrementOptionCount(questionIndex, chosenOptionIndex);

        if (this.surveyResult.everyoneAnsweredAllQuestions(this.surveyState.getCommunitySizeAtStart())) {
            closeSurveyImmediately();
        }
    }


    public void closeSurveyImmediately() {
        if (!this.surveyState.isSurveyOpen()) return;

        // עוצר את כל ה-Poll-ים לפי המיפוי chatId -> (questionIndex -> messageId)
        for (Map.Entry<Long, Map<Integer, Integer>> entry : this.surveyState.getMessageIdByChatIdAndQuestion().entrySet()) {
            long chatId = entry.getKey();
            Map<Integer, Integer> perQuestion = entry.getValue();
            if (perQuestion == null) continue;

            for (Map.Entry<Integer, Integer> byQuestion : perQuestion.entrySet()) {
                int messageId = byQuestion.getValue();
                this.telegramPollEngine.stopPollByMessageId(chatId, messageId);
            }
        }

        System.out.println(LOG_ALL_ANSWERED);
        this.surveyState.setSurveyOpen(false);
    }


    public Map<Integer, int[]> getOptionCountsSnapshot() {
        return this.surveyResult.snapshotOptionCounts();
    }
}
