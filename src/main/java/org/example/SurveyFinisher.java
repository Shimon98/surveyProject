package org.example;


import org.example.SurveyResult;
import org.example.SurveyState;
import org.example.community.CommunityBroadcaster;
import org.example.engine.SurveyCloser;

public class SurveyFinisher {

    private static final String MSG_HEADER_ALL = "Survey ended: everyone answered.";
    private static final String MSG_HEADER_TIMEOUT = "Survey ended: time expired.";

    private SurveyCloser surveyCloser;
    private SurveyState surveyState;
    private SurveyResult surveyResult;
    private SurveyResultFormatter formatter;
    private CommunityBroadcaster broadcaster;

    public SurveyFinisher(SurveyCloser surveyCloser, SurveyState surveyState, SurveyResult surveyResult,
                          SurveyResultFormatter formatter, CommunityBroadcaster broadcaster) {
        this.surveyCloser = surveyCloser;
        this.surveyState = surveyState;
        this.surveyResult = surveyResult;
        this.formatter = formatter;
        this.broadcaster = broadcaster;
    }

    public void finishAllAnswered() {
        finishNow(MSG_HEADER_ALL);
    }

    public void finishTimeout() {
        finishNow(MSG_HEADER_TIMEOUT);
    }

    private void finishNow(String header) {

        this.surveyCloser.closeNow();

        String summary = this.formatter.buildSummary(this.surveyState, this.surveyResult);

        String finalMessage = header + "\n\n" + summary;

        System.out.println(finalMessage);

        this.broadcaster.broadcast(finalMessage);
    }
}
