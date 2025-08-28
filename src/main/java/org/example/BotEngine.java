package org.example;

import org.example.bot.TelegramGateway;
import org.example.util.JoinOutcome;
import org.telegram.telegrambots.meta.api.objects.Update;

public class BotEngine {
    private TelegramGateway gateway;
    private CommunityService communityService;
    private Community community;

    public BotEngine(TelegramGateway gateway ) {

        this.gateway = gateway;
        this.community=new Community();
        this.communityService =new CommunityService(this.community,this.gateway);

    }

   public void onUpdate(Update update) {
        chakMasege(update);
   }


    private void chakMasege(Update update){
        long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        String name = getName(update);
        JoinOutcome outcome = this.communityService.handleRegister(chatId,name,text);
        this.communityService.respondToJoinOutcome(outcome,chatId);
    }




    private String getName( Update update){
        if (update.getMessage().getFrom().getFirstName()  + update.getMessage().getFrom().getLastName() == null) {
            return null;
        }
        String name = update.getMessage().getFrom().getFirstName() + " " + update.getMessage().getFrom().getLastName();
        return name;
    }


}
