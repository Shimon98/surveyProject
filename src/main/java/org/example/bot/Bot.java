package org.example.bot;

import org.example.BotEngine;
import org.example.Community;
import org.example.Question;
import org.example.Survey;
import org.example.config.Config;
import org.example.model.Member;
import org.example.model.OptionForQuestion;
import org.example.util.JoinOutcome;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class Bot extends TelegramLongPollingBot {
    // name= Survey Project Bot
    // userName= @surveyProject2Bot

    private Community community;

    public Bot(Community community) {
        this.community = community;
    }


    @Override
    public String getBotUsername() {
        return Config.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return Config.getBotToken();
    }


    @Override
    public void onUpdateReceived(Update update) {
        botEngine(update);
    }

    private void botEngine(Update update)  {
        long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        String name = getName(update);
        joinOutcome(chatId, text, name);


//        try {
//            Thread.sleep(10000);
//            Survey survey = survey();
//            Set<Long>chetIDS = this.community.getAllChetId();
//            for (Long id : chetIDS){
//                System.out.println("111111111111111");
//                sendSurvey(survey, id);
//            }
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
    }


    private String getName(Update update) {
        if (update.getMessage().getFrom().getUserName() == null) {
            return null;
        }
        String name = update.getMessage().getFrom().getFirstName() + " " + update.getMessage().getFrom().getLastName();
        return name;

    }



    public void sendSurvey(Survey survey , Long chatId){
        System.out.println("Entered the survey sender");
        int totalQuestions = survey.getQuestions().size();
        int counter = 0;
        for (Question question : survey.getQuestions()){
            Question tempQ = question;
            List<String> textOptions = extractOptionsAsTextFromQuestion(tempQ);
            counter++;

            SendPoll sendPoll = new SendPoll();
            sendPoll.setChatId(chatId);
            sendPoll.setQuestion("(" + counter + "/" + totalQuestions + ") " + tempQ.getText());
            sendPoll.setOptions(textOptions);
            sendPoll.setIsAnonymous(false);
            sendPoll.setAllowMultipleAnswers(false);
            sendPoll.setOpenPeriod(300);

            try {
                execute(sendPoll);
            }catch (TelegramApiException e){
                e.printStackTrace();
            }
        }

    }

    private List<String> extractOptionsAsTextFromQuestion(Question question){
        List<String> textOptions = new ArrayList<>();
        List<OptionForQuestion> options = question.getOptions();
        for (int i = 0; i < options.size(); i++){
            textOptions.add(options.get(i).getText());
        }
        return textOptions;

    }


    private void send(long chatId, String text) {
        try {
            execute(SendMessage.builder().chatId(chatId).text(text).build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    private Survey survey(){// רק בדיקה
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
