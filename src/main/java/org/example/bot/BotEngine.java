package org.example.bot;

import org.example.SurveyEngine;
import org.example.TelegramPollEngine;
import org.example.community.Community;
import org.example.community.CommunityService;
import org.example.model.Question;
import org.example.model.Survey;
import org.example.util.JoinOutcome;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BotEngine {
    private TelegramGateway gateway;
    private CommunityService communityService;
    private Community community;
    private TelegramPollEngine telegramPollEngine;
    private SurveyEngine surveyEngine;


    public BotEngine(TelegramGateway gateway ) {

        this.gateway = gateway;
        this.community=new Community();
        this.communityService =new CommunityService(this.community,this.gateway);
        this.telegramPollEngine = new TelegramPollEngine(this.gateway);
        this.surveyEngine = new SurveyEngine(this.telegramPollEngine, this.community);
    }



    public void startSurveyFromUi(Survey survey) {

//        this.surveyEngine.startSurvey(survey);
        this.surveyEngine.startSurvey(surveyDemo());
    }
    public Map<Integer, int[]> getCountsForUi() {
        return this.surveyEngine.getOptionCountsSnapshot();
    }


   public void onUpdate(Update update) {
        chakMasege(update);

       if (update.hasPollAnswer()) {
           this.surveyEngine.onPollAnswer(update.getPollAnswer());
       }
       startSurveyFromUi(surveyDemo());

   }


    private void chakMasege(Update update){
        long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        String name = getName(update);
        JoinOutcome outcome = this.communityService.handleRegister(chatId,name,text);
        this.communityService.respondToJoinOutcome(outcome,chatId);
    }


    private String getName( Update update){
        if (update.getMessage().getFrom().getFirstName()  + update.getMessage().getFrom().getLastName() == null) {
            return null;
        }
        String name = update.getMessage().getFrom().getFirstName() + " " + update.getMessage().getFrom().getLastName();
        return name;
    }



    private Survey surveyDemo() {// רק בדיקה
        System.out.println("entered Survey");
        List<String> optionText= List.of("You","Me");
        System.out.println("1");
        Question question= Question.crate(1,"Who’s more likely to get handcuffed tonight",optionText);
        System.out.println("2");
        List<Question> questionArrayList=new ArrayList<>();
        System.out.println("3");
        questionArrayList.add(question);
        System.out.println("4");
        Survey survey =Survey.create("temp",questionArrayList);
        System.out.println("about to leave Survey");

        return survey;
    }

    }
