package org.example.engine;

import org.example.SurveyState;

public class SurveyTimeoutThread extends Thread {
    private static final String NAME = "[SurveyTimeoutThread] ";

    private SurveyState surveyState;
    private SurveyCloser surveyCloser;
    private long timeoutMillis;

    public SurveyTimeoutThread(SurveyState surveyState, SurveyCloser surveyCloser, long timeoutMillis) {
        super(NAME);
        this.surveyState = surveyState;
        this.surveyCloser = surveyCloser;
        this.timeoutMillis = timeoutMillis;
    }

    @Override
    public void run() {

        try {
            Thread.sleep(this.timeoutMillis);
        } catch (InterruptedException ignored) {}
        if (this.surveyState.isSurveyOpen()) {
            this.surveyCloser.closeNow();
        }
    }
}
