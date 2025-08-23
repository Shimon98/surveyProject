package org.example;

import org.example.util.SurveyStatus;
import org.example.util.TimeUtils;
import org.example.util.Validate;

import java.util.List;


public class Survey {

    public static final String ERROR_SIZE = "List of questions for the survey";
    public static final int MIN_QUESTION = 1;
    public static final int MAX_QUESTION = 3;

    private final long id;
    private final String title;
    private List<Question> questions;
    private long createdAtMs;
    private long startAtMs;
    private long closeAtMs;
    private SurveyStatus status;


    private Survey(long id, String title, List<Question> questions,
                   long createdAtMs, long startAtMs, long closeAtMs, SurveyStatus status) {
        this.id = id;
        this.title = title;
        this.questions = questions;
        this.createdAtMs = createdAtMs;
        this.startAtMs = startAtMs;
        this.closeAtMs = closeAtMs;
        this.status = status;
    }

    public static Survey create(long id, String title, List<Question> questions,
                                long startAtMs, long durationMs) {
        long closeAtMs = startAtMs+durationMs;
        String t = Validate.requireText(title, "Survey title");
        List<Question> questionList = Validate.requireSizeBetween(questions, MIN_QUESTION, MAX_QUESTION, ERROR_SIZE);
        long now = TimeUtils.now();
        if (closeAtMs <= startAtMs) {
            throw new IllegalArgumentException("closeAtMs must be after startAtMs");
        }
        if (closeAtMs <= now) {
            throw new IllegalArgumentException("closeAtMs must be in the future");
        }
        SurveyStatus status = calcStatus(startAtMs, closeAtMs, now);
        return new Survey(id, t, questionList, now, startAtMs, closeAtMs, status);
    }


    private static SurveyStatus calcStatus(long start, long close, long now) {
        if (now < start) return SurveyStatus.SCHEDULED;
        if (now < close) return SurveyStatus.ACTIVE;
        return SurveyStatus.CLOSED;
    }

    public void refreshStatus(long now) {
        this.status = calcStatus(this.startAtMs, this.closeAtMs, now);
    }

    public boolean isActive()     { return status == SurveyStatus.ACTIVE; }
    public boolean isClosed()     { return status == SurveyStatus.CLOSED; }
    public boolean isScheduled()  { return status == SurveyStatus.SCHEDULED; } // אם תבחרו באופציה א'


    public long getId() { return id; }
    public String getTitle() { return title; }
    public List<Question> getQuestions() { return questions; }
    public long getCreatedAtMs() { return createdAtMs; }
    public long getStartAtMs() { return startAtMs; }
    public long getCloseAtMs() { return closeAtMs; }
    public SurveyStatus getStatus() { return status; }

    // שינויי זמן (מוגנים בעזרת השירות)
    void setStartAtMs(long startAtMs) { this.startAtMs = startAtMs; }
    void setCloseAtMs(long closeAtMs) { this.closeAtMs = closeAtMs; }
    void setStatus(SurveyStatus status) { this.status = status; }

    @Override
    public String toString() {
        return "Survey{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", questions=" + questions +
                ", createdAtMs=" + createdAtMs +
                ", startAtMs=" + startAtMs +
                ", closeAtMs=" + closeAtMs +
                ", status=" + status +
                '}';
    }
}