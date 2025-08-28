package org.example.model;

import org.example.util.SurveyStatus;
import org.example.util.Validate;

import java.util.List;


public class Survey {

    public static final String ERROR_SIZE = "List of questions for the survey";
    public static final int MIN_QUESTION = 1;
    public static final int MAX_QUESTION = 3;

    private long id;

    private String title;
    private List<Question> questions;

    private long createdAtMs;
    private long startAtMs;
    private long closeAtMs;

    private SurveyStatus status;


    private Survey(String title, List<Question> questions) {
//        this.id = id;

        this.title = title;
        this.questions = questions;

//        this.createdAtMs = createdAtMs;
//        this.startAtMs = startAtMs;
//        this.closeAtMs = closeAtMs;
//        this.status = status;
     }

    public static Survey create( String title, List<Question> questions) {

        String t = Validate.requireText(title, "Survey title");
        List<Question> questionList = Validate.requireSizeBetween(questions, MIN_QUESTION, MAX_QUESTION, ERROR_SIZE);


//        long closeAtMs = startAtMs+durationMs;
//        long now = TimeUtils.now();
//        if (closeAtMs <= startAtMs) {
//            throw new IllegalArgumentException("closeAtMs must be after startAtMs");
//        }
//        if (closeAtMs <= now) {
//            throw new IllegalArgumentException("closeAtMs must be in the future");
//        }
//        SurveyStatus status = calcStatus(startAtMs, closeAtMs, now);

        return new Survey( t, questionList);
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
    public boolean isScheduled()  { return status == SurveyStatus.SCHEDULED; }


    public long getId() { return id; }
    public String getTitle() { return title; }
    public List<Question> getQuestions() { return questions; }
    public long getCreatedAtMs() { return createdAtMs; }
    public long getStartAtMs() { return startAtMs; }
    public long getCloseAtMs() { return closeAtMs; }
    public SurveyStatus getStatus() { return status; }



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