package org.example.util;

public enum JoinOutcome {
    JOINED,          // נרשם עכשיו
    ALREADY_MEMBER,  // כבר היה רשום
    NOT_TRIGGER      // לא טקסט שמעורר טריגר (/start, היי, hi, hello)
}