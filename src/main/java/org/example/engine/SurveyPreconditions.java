package org.example.engine;

import org.example.community.Community;
import org.example.model.Survey;
import org.example.SurveyState;

public class SurveyPreconditions {
    private static final String ERROR_ACTIVE_SURVEY = "יש כבר סקר פעיל. סגור את הסקר הנוכחי לפני פתיחת סקר חדש.";
    private static final String ERROR_MIN_COMMUNITY = "נדרשים לפחות 3 חברים בקהילה כדי להתחיל סקר.";
    private static final String ERROR_SURVEY_NULL = "Survey is null";
    private static final String ERROR_QUESTIONS_EMPTY = "Survey has no questions";

    private Community community;
    private SurveyState surveyState;

    public SurveyPreconditions(Community community, SurveyState surveyState) {
        this.community = community;
        this.surveyState = surveyState;
    }

    public void validateStart(Survey survey) {
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
}
