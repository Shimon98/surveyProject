package org.example.engine;

import org.example.SurveyState;

public class SurveyTimeoutThread extends Thread {
    private static final String LOG_PREFIX = "[SurveyTimeoutThread] ";
    private static final String LOG_START = LOG_PREFIX + "התחיל טיימר של 5 דקות לסקר הפעיל.";
    private static final String LOG_WAKEUP = LOG_PREFIX + "עברו 5 דקות. מנסה לסגור את הסקר אם עדיין פתוח.";

    private SurveyState surveyState;
    private SurveyCloser surveyCloser;
    private long timeoutMillis;

    public SurveyTimeoutThread(SurveyState surveyState, SurveyCloser surveyCloser, long timeoutMillis) {
        super("survey-timeout-thread");
        this.surveyState = surveyState;
        this.surveyCloser = surveyCloser;
        this.timeoutMillis = timeoutMillis;
    }

    @Override
    public void run() {
        System.out.println(LOG_START);
        try {
            Thread.sleep(this.timeoutMillis);
        } catch (InterruptedException ignored) {}
        System.out.println(LOG_WAKEUP);
        if (this.surveyState.isSurveyOpen()) {
            this.surveyCloser.closeNow();
        }
    }
}
