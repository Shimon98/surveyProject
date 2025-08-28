package org.example.bot;

import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.polls.StopPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramGateway implements TelegramGatewayInterface {
    private Bot bot;

    public TelegramGateway(Bot bot) {
        this.bot = bot;
    }

    public synchronized Message sendMessage(SendMessage msg) throws TelegramApiException {
        return bot.execute(msg);
    }

    public synchronized Message sendPoll(SendPoll poll) throws TelegramApiException {
        return bot.execute(poll);
    }

    public synchronized Poll stopPoll(StopPoll stop) throws TelegramApiException {// יש פה בעיה
        return bot.execute(stop);
    }

    public synchronized Poll stopPollByChatId(long chatId, int messageId) throws TelegramApiException {
        StopPoll stop = new StopPoll(String.valueOf(chatId), messageId);
        return bot.execute(stop);
    }

}
