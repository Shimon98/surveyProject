package org.example.model;

import java.util.Objects;

public class Member {
    private long chatId;
    private String userName;

    public Member(long chetID, String userName) {
        this.chatId = chetID;
        this.userName = userName;
    }

    public long getChatId() {
        return chatId;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Member member = (Member) object;
        return this.chatId == member.getChatId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId);                // חייב להיות עקבי עם equals
    }

    @Override
    public String toString() {
        return "Member{" +
                "chetID=" + chatId +
                ", userName='" + userName + '\'' +
                '}';
    }
}