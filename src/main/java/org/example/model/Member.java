package org.example.model;

import java.util.Objects;

public class Member {
    private long chetID;
    private String userName;

    public Member(long chetID, String userName) {
        this.chetID = chetID;
        this.userName = userName;
    }

    public long getChetID() {
        return chetID;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Member member = (Member) object;
        return this.chetID == member.getChetID();
    }

    @Override
    public int hashCode() {
        return Objects.hash(chetID);                // חייב להיות עקבי עם equals
    }

    @Override
    public String toString() {
        return "Member{" +
                "chetID=" + chetID +
                ", userName='" + userName + '\'' +
                '}';
    }
}