package org.example.config;

import org.example.model.Question;
import org.example.model.Survey;

import java.util.ArrayList;
import java.util.List;

public final class DemoSurveyFactory {
    private DemoSurveyFactory() {}

    public static Survey buildDemoSurvey() {

        List<String> optionText= List.of("You","Me");
        Question question1= Question.crate(1,"Who’s more likely to get handcuffed tonight",optionText);

        List<String> optionText2= List.of("Red","Blue","Green","Yellow");
        Question question2= Question.crate(1,"Favorite color?",optionText2);

        List<Question> questionArrayList=new ArrayList<>();
        questionArrayList.add(question1);
        questionArrayList.add(question2);
        Survey survey =Survey.create("Demo Survey",questionArrayList);


        return survey;
    }



}
