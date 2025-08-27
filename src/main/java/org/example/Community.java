package org.example;

import org.example.model.Member;
import org.example.util.JoinOutcome;

import java.util.HashSet;
import java.util.Set;

public class Community {
    private Set <Member> members;
    private CommunityRegistry registry;

    public Community() {
        this.registry=new CommunityRegistry();
        this.members = this.registry.getMembers();
    }
    public JoinOutcome handlRegister(Long chetID, String userName, String text){
       return this.registry.handlRegisterToCommunity(chetID,userName,text);
    }

    public Set<Member> getMembers() {
        return members;
    }

    public Member getMemberByChetId(Long chetId){
        if (exists(chetId)){
        for (Member member :this.members){
            if (member.getChetID()==chetId){
                return member;
            }
        }}
        return null;
    }

    public boolean exists(long chatId) {
        return members.contains(new Member(chatId, ""));
    }

    public int size(){
        return this.members.size();
    }
    public Set<Long> getAllChetId(){
        Set<Long> allChetID= new HashSet<>();

        for (Member member: this.members){
            allChetID.add(member.getChetID());
        }
        return allChetID;
    }
}
