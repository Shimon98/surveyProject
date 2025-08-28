package org.example.ui;

import org.example.community.Community;
import org.example.bot.Bot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static final String TITLE = "Survey Maker";
    public static final int WINDOW_WIDTH = 500;
    public static final int WINDOW_HEIGHT = 800;
    public static final int WINDOW_X = 0;
    public static final int WINDOW_Y = 0;
    public static final String CARD_HOME = "HOME";
    public static final String CARD_CREATE = "CREATE";
    public static final String CARD_RESULTS = "RESULTS";

    private Community community;



    public MainFrame() {
        this.community = new Community();
        this.telegramAPI();
        this.setTitle(TITLE);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void telegramAPI() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new Bot(this.community));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }



}
