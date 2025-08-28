package org.example.model;

import org.example.util.SurveyStatus;
import org.example.util.Validate;

import java.util.List;


public class Survey {

    public static final String ERROR_SIZE = "List of questions for the survey";
    public static final int MIN_QUESTION = 1;
    public static final int MAX_QUESTION = 3;


    private String title;
    private List<Question> questions;
    private SurveyStatus status;


    private Survey(String title, List<Question> questions) {
        this.title = title;
        this.questions = questions;
     }

    public static Survey create( String title, List<Question> questions) {

        String t = Validate.requireText(title, "Survey title");
        List<Question> questionList = Validate.requireSizeBetween(questions, MIN_QUESTION, MAX_QUESTION, ERROR_SIZE);
        return new Survey( t, questionList);
    }


    private static SurveyStatus calcStatus(long start, long close, long now) {
        if (now < start) return SurveyStatus.SCHEDULED;
        if (now < close) return SurveyStatus.ACTIVE;
        return SurveyStatus.CLOSED;
    }





    public String getTitle() { return title; }
    public List<Question> getQuestions() { return questions; }


    @Override
    public String toString() {
        return "Survey{" +
                "title='" + title + '\'' +
                ", questions=" + questions +
                ", status=" + status +
                '}';
    }
}