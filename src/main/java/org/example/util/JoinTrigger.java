package org.example.util;

public enum JoinTrigger {
    START("/start"),
    HI_EN("hi"),
    HELLO("hello"),
    HI_HE("היי");

    private String text;

    JoinTrigger(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static boolean matches(String input) {
        if (input == null) return false;
        String textToLCase = input.trim().toLowerCase();
        for (JoinTrigger trigger : values()) {
            if (trigger.text.equals(textToLCase)) return true;
        }
        return false;
    }
}
