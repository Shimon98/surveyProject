package org.example;

import org.example.bot.TelegramGateway;
import org.example.model.Member;
import org.example.util.JoinOutcome;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class CommunityService {

    private Community community;
    private TelegramGateway telegramGateway;

    public CommunityService(Community community, TelegramGateway telegramGateway) {
        this.community = community;
        this.telegramGateway = telegramGateway;
    }

    public JoinOutcome handleRegister(long chatId, String displayName, String text) {
        return this.community.handlRegister(chatId, displayName, text);
    }

    public void respondToJoinOutcome(JoinOutcome outcome, long chatId) {
        switch (outcome) {
            case JOINED -> {
                Member member = this.community.getMemberByChetId(chatId);
                String name = member.getUserName();
                send(chatId, "Congratulations " + name + ", you have joined the community🎉. " +
                        "There are now " + this.community.size() + " members in the community.");

                for (Long id : this.community.getAllChetId()) {
                    if (id != chatId) {
                        send(id, name + " joined the community. The community now has " + this.community.size() + " members.");
                    }
                }
            }
            case ALREADY_MEMBER -> { //לא מומש
            }
            case NOT_TRIGGER -> {  // לא מומש
            }
        }
    }

    private void send(long chatId, String text) {
        try {
            this.telegramGateway.sendMessage(SendMessage.builder().chatId(chatId).text(text).build());
        } catch (TelegramApiException e) {
        }
    }
}

