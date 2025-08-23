package org.example.bot;

import org.example.Community;
import org.example.CommunityRegistry;
import org.example.config.Config;
import org.example.model.Member;
import org.example.util.JoinOutcome;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


public class Bot extends TelegramLongPollingBot {
    // neme= Survey Project Bot
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

    private void botEngine(Update update) {
        long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        String name = getName(update);
        joinOutcome(chatId, text, name);

    }

    private String getName(Update update) {
        if (update.getMessage().getFrom().getUserName() == null) {
            return null;
        }
        String name = update.getMessage().getFrom().getFirstName() + " " + update.getMessage().getFrom().getLastName();
        return name;

    }

    private void joinOutcome(long chatId, String text, String displayName) {
        JoinOutcome outcome = community.handlRegister(chatId, displayName, text);
        switch (outcome) {
            case JOINED -> {
                Member member = community.getMemberByChetId(chatId);
                displayName = member.getUserName();
                send(chatId, "Congratulations " + displayName +
                        ", you have joined the community🎉. " +
                        " There are now  " + community.size() + " members in the community. ");

                for (Long id : community.getAllChetId()) {
                    if (!id.equals(chatId)) {
                        send(id, displayName + " joined the community. The community now has "
                                + community.size() + " members.");
                    }
                }
            }
            case ALREADY_MEMBER -> {
            }
            case NOT_TRIGGER -> {
            }
        }

    }

    private void send(long chatId, String text) {
        try {
            execute(SendMessage.builder().chatId(chatId).text(text).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
