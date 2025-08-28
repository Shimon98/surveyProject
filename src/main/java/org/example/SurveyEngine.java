package org.example;

import org.example.community.Community;
import org.example.engine.*;
import org.example.model.Survey;
import org.telegram.telegrambots.meta.api.objects.polls.PollAnswer;

import java.util.Map;

public class SurveyEngine {
    private static final String LOG_PREFIX = "[SurveyEngine] ";
    private static final String LOG_START = LOG_PREFIX + "מתחיל סקר...";

    private Community community;
    private TelegramPollEngine telegramPollEngine;

    private SurveyState surveyState;
    private SurveyResult surveyResult;

    private SurveyLocks surveyLocks;
    private SurveyPreconditions surveyPreconditions;
    private CountersPreparer countersPreparer;
    private OptionTextBuilder optionTextBuilder;
    private MessageAndPollRegistry messageAndPollRegistry;
    private SurveySender surveySender;
    private AnswerProcessor answerProcessor;
    private SurveyCloser surveyCloser;

    public SurveyEngine(TelegramPollEngine telegramPollEngine, Community community) {
        this.telegramPollEngine = telegramPollEngine;
        this.community = community;
        this.surveyState = new SurveyState();
        this.surveyLocks = new SurveyLocks();
        this.surveyPreconditions = new SurveyPreconditions(this.community, this.surveyState);
        this.countersPreparer = new CountersPreparer(this.surveyState, this.surveyLocks);
        this.optionTextBuilder = new OptionTextBuilder();
        this.messageAndPollRegistry = new MessageAndPollRegistry(this.surveyState, this.surveyLocks);
        this.surveySender = new SurveySender(this.community, this.telegramPollEngine, this.optionTextBuilder, this.messageAndPollRegistry);

    }

    public boolean isSurveyActive() {
        return this.surveyState.isSurveyOpen();
    }


    public void startSurvey(Survey survey) {
        this.surveyPreconditions.validateStart(survey);

        int communitySize = this.community.size();
        synchronized (this.surveyLocks.getStateLock()) {
            this.surveyState.resetForNewSurvey(survey, communitySize);
            this.surveyResult = new SurveyResult(
                    this.surveyState.getOptionCountsByQuestionIndex(),
                    this.surveyState.getAnsweredUserIdsByQuestionIndex()
            );
            this.answerProcessor = new AnswerProcessor(this.surveyState, this.surveyResult, this.surveyLocks);
            this.surveyCloser = new SurveyCloser(this.telegramPollEngine, this.surveyState, this.surveyLocks);
        }
        this.countersPreparer.prepareForAllQuestions(survey);
        System.out.println(LOG_START);
        this.surveySender.sendAllQuestionsToAllMembers(survey);

        SurveyTimeoutThread t = new SurveyTimeoutThread(this.surveyState, this.surveyCloser, 5 * 60 * 1000L);
        t.start();
    }

    public void onPollAnswer(PollAnswer pollAnswer) {
        boolean allAnswered = this.answerProcessor.process(pollAnswer);
        if (allAnswered) {
            this.surveyCloser.closeNow();
        }
    }

    public Map<Integer, int[]> getOptionCountsSnapshot() {
        return this.surveyResult.snapshotOptionCounts();
    }
}
