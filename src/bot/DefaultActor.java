package bot;

import managers.ChannelManager;
import managers.CustomMessageManager;
import models.Channel;
import models.CustomMessage;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.List;
import java.util.Random;

/**
 * Created by palepail on 8/5/2015.
 */
public class DefaultActor {

    MessageManager messageManager;
    ChannelManager channelManager = new ChannelManager();
    CustomMessageManager customMessageManager = new CustomMessageManager();
    Channel channelEntity;
    String channelName;
    String message;
    public void setValues(MessageEvent event){
        channelName = event.getChannel().getName();
        messageManager = MessageManager.getInstance(channelName);
        channelEntity = channelManager.getChannelByName(channelName.substring(1));
       message = event.getMessage();
    }


    public void palebotInfo(MessageEvent event){

        messageManager.reduceMessages(1);
        event.getBot().sendIRC().message(event.getChannel().getName(), "Hi! I'm palebot.");
    }

    public void  selfTimeout(MessageEvent event)
        {
            messageManager.reduceMessages(1);
            messageManager.delayMessage(1500);
            event.getBot().sendIRC().message(event.getChannel().getName(), "/timeout " + event.getUser().getNick() + " 1");
        }

    public void deleteCustomMessage(MessageEvent event)
    {
        String message = event.getMessage();
        String regex = "\\!custom delete ?\\(\\!([a-z1-9]+)\\)";
        if (!message.matches(regex)) {
            messageManager.sendMessage(event, event.getUser().getNick()+ ", correct !custom delete syntax is !custom delete (!TRIGGER)");
            return;
        }

        String trigger = message.substring(message.indexOf("(") + 1, message.indexOf(")"));
        if(customMessageManager.deleteTriggerFromChannel(channelEntity.getId(), trigger))
        {
            messageManager.sendMessage(event,"Trigger Deleted");
        }else{
            messageManager.sendMessage(event, "Trigger Not Found" );
        }
    }

    public void saveCustomMessage(MessageEvent event)
    {
        String regex = "\\!custom ?\\(\\!([a-z1-9]+)\\) ?(.{0,240})";
        if (!event.getMessage().matches(regex)) {
            messageManager.sendMessage(event, event.getUser().getNick() + ", correct new custom message syntax is !custom (!TRIGGER) MESSAGE - Max message length is 240");
            return;
        }
        String trigger = message.substring(message.indexOf("(") + 1, message.indexOf(")"));
        String customMessage = message.substring(message.indexOf(")") + 2);


        CustomMessage custom = new CustomMessage();
        if (customMessage.indexOf("-mod") != -1) {
            customMessage.replace("-mod", "");
            custom.setRestriction(1);
        }


        custom.setMessage(customMessage);
        custom.setCustomTrigger(trigger);
        custom.setChannelId(channelEntity.getId());
        customMessageManager.addCustomMessage(custom);
        messageManager.sendMessage(event, "Custom Message Saved");

    }

    public void rollDice(MessageEvent event)
    {

        Random rand = new Random();
        int number = rand.nextInt(5);
        number += 1;
        String flair = "";
        switch (number) {
            case 1: {
                flair = "Kappa";
                break;
            }
            case 2: {
                flair = "BibleThump";
                break;
            }
            case 3: {
                flair = "DansGame";
                break;
            }
            case 4: {
                flair = "MVGame";
                break;
            }
            case 5: {
                flair = "FrankerZ";
                break;
            }
            case 6: {
                flair = "PogChamp";
                break;
            }
        }
        messageManager.sendMessage(event, event.getUser().getNick() + "rolled a " + number + " " + flair);

    }

    public void customTrigger(MessageEvent event){
        List<CustomMessage> customMessages = customMessageManager.getCustomMessagesByChannel(channelEntity.getId());
        for (CustomMessage customMessage : customMessages) {
            if (event.getMessage().startsWith(customMessage.getCustomTrigger())) {
                messageManager.reduceMessages(1);
                event.getBot().sendIRC().message(channelName, customMessage.getMessage());
            }
        }
    }

}
