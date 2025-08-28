package org.example;

import org.example.model.OptionForQuestion;
import org.example.model.Question;

import java.util.List;
import java.util.Set;

public class SurveyResultFormatter {

    private static final String TITLE_RESULTS = "Survey results:";
    private static final String TITLE_QUESTION_FMT = "Q%d: %s";
    private static final String LINE_OPTION_FMT = " - %s: %d (%d%%)";

    public String buildSummary(SurveyState state, SurveyResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append(TITLE_RESULTS).append("\n");

        List<Question> questions = state.getCurrentSurvey().getQuestions();
        int qIndex = 0;
        while (qIndex < questions.size()) {
            Question q = questions.get(qIndex);
            sb.append(String.format(TITLE_QUESTION_FMT, (qIndex + 1), q.getText())).append("\n");

            int[] counts = result.snapshotOptionCounts().get(qIndex);
            if (counts == null) counts = new int[0];

            Set<Long> whoAnswered = state.getAnsweredUserIdsByQuestionIndex().get(qIndex);
            int totalAnswered = (whoAnswered == null) ? 0 : whoAnswered.size();

            List<OptionForQuestion> opts = q.getOptions();
            int optIdx = 0;
            while (optIdx < (opts == null ? 0 : opts.size())) {
                String optText = opts.get(optIdx).getText();
                int c = (optIdx < counts.length) ? counts[optIdx] : 0;
                int pct = (totalAnswered == 0) ? 0 : (c * 100) / totalAnswered;
                sb.append(String.format(LINE_OPTION_FMT, optText, c, pct)).append("\n");
                optIdx = optIdx + 1;
            }

            sb.append("\n");
            qIndex = qIndex + 1;
        }
        return sb.toString().trim();
    }
}
