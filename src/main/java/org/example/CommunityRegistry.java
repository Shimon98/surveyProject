package org.example;

import org.example.model.Member;
import org.example.util.JoinOutcome;
import org.example.util.JoinTrigger;

import java.util.HashSet;
import java.util.Set;

public class CommunityRegistry {
    private static final String NULL_NAME = "Anonymous ";

    private Set<Member> members;
    private int anonymousCounter;

    public CommunityRegistry(){
        this.members=new HashSet<>();
        this.anonymousCounter=0;
    }



    public JoinOutcome handlRegisterToCommunity(Long chetID, String userName, String text){
        if (!isTrigerText(text)){
            return JoinOutcome.NOT_TRIGGER;
        }
        String name = userName;
        if (name== null){
            this.anonymousCounter++;
            name=NULL_NAME+String.valueOf(this.anonymousCounter);
        }
        Member newMember = new Member(chetID,name );
        boolean added = members.add(newMember);
        return added ? JoinOutcome.JOINED : JoinOutcome.ALREADY_MEMBER;
    }

    private boolean isTrigerText(String text){
    return JoinTrigger.matches(text);
    }



    public Set<Member> getMembers() {
        return members;
    }



}
