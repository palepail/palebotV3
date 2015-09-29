package bot;

import managers.ChannelManager;
import managers.QuoteManager;
import managers.SpamManager;
import models.Channel;
import models.Quote;
import models.Spam;
import org.pircbotx.hooks.events.MessageEvent;

import java.text.SimpleDateFormat;
import java.util.*;

public class SpamBotActor {


    private static MessageManager messageManager ;
    private static SpamManager spamManager = SpamManager.getInstance();
    private static ChannelManager channelManager = new ChannelManager();


    String channelName;
    String userName;
    Channel channelEntity;
    String message;
    List<Spam> spamList = spamManager.getAll();

    public void setValues(MessageEvent event){
        channelName = event.getChannel().getName();

        userName = event.getUser().getNick();
        channelEntity = channelManager.getChannelByName(channelName.substring(1));
        messageManager = MessageManager.getInstance(channelName);
        message = event.getMessage();
    }

    public void addSpam(MessageEvent event){

        if (messageManager.isMod(channelName,userName)) {
            String regex = "!spam add \\(.+\\)";


            if (message.matches(regex)) {
                String offence = message.substring(message.indexOf("(") + 1, message.indexOf(")"));
                Spam spam = new Spam();
                spam.setChannelId(channelEntity.getId());
                spam.setOffence(offence);
                spam.setUser(userName);

                spamManager.addSpam(spam);

                spamList = spamManager.getAll();
            }
        }
    }

    public void checkIfBot(MessageEvent event){

        for(Spam currentSpam : spamList){
            if(message.contains(currentSpam.getOffence()))
            {
                setValues(event);
                banBot(event);
                return;
            }
        }
    }
    public void banBot(MessageEvent event){
        messageManager.sendMessage(event, "I think "+ userName+" is a bot.");
        messageManager.sendMessage(event, ".ban "+userName);
    }


}

