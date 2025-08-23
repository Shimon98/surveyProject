package org.example.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Config {
    private static final String KEY_BOT_TOKEN     = "BOT_TOKEN";
    private static final String KEY_BOT_USERNAME  = "BOT_USERNAME";
    private static final String DEFAULT_USERNAME  = "surveyProject2Bot";
    private static final String ERROR_MISSING_TOKEN = "BOT_TOKEN is missing. Set it in /main/resources/config.properties";
    public static final String API_CHAT_GPT_URL_BILDER= "https://app.seker.live/fm1/send-message?id=319028015"+"&text=AHOOO";


    private static final Properties PROPS = new Properties();
    private static boolean loaded = false;


    private Config() {} // private constructor זה חשובב מונע מיצירת המחלקה

    private static void loadPropsIfNeeded() {
        if (loaded) return;
        try (InputStream in = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) {
                PROPS.load(in);
            }
        } catch (final IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
        loaded = true;
    }

    private static String get(final String key) {
        final String env = System.getenv(key);
        if (env != null && !env.isBlank()) return env;

        loadPropsIfNeeded();
        return PROPS.getProperty(key);
    }

    public static String getBotToken() {
        final String token = get(KEY_BOT_TOKEN);
        if (token == null) {
            throw new IllegalStateException(ERROR_MISSING_TOKEN);
        }
        return token;
    }

    public static String getBotUsername() {
        final String username = get(KEY_BOT_USERNAME);
        return (username == null || username.isBlank()) ? DEFAULT_USERNAME : username;
    }
}
