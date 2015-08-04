package bot;

import managers.ChannelManager;
import managers.CustomMessageManager;
import models.Channel;
import models.CustomMessage;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.List;
import java.util.Random;

/**
 * Created by palepail on 7/26/2015.
 */
public class DefaultListener extends ListenerAdapter {
    MessageManager messageManager = MessageManager.getInstance();
    ChannelManager channelManager = new ChannelManager();

    CustomMessageManager customMessageManager = new CustomMessageManager();
    public static final String NAME = "DEFAULT";

    @Override
    public void onMessage(MessageEvent event) {

        String channelName = event.getChannel().getName();
        Channel channelEntity = channelManager.getChannelByName(channelName.substring(1));

        if (event.getMessage().startsWith("!palebot")) {
            if (!messageManager.overLimit()) {
                messageManager.reduceMessages(1);
                event.getBot().sendIRC().message(event.getChannel().getName(), "Hi! I'm palebot.");
            }
        }

        if (event.getMessage().startsWith("!suicide")) {
            if (!messageManager.overLimit()) {
                messageManager.reduceMessages(1);
                messageManager.delayMessage(1500);
                event.getBot().sendIRC().message(event.getChannel().getName(), "/timeout " + event.getUser().getNick() + " 1");
            }
        }

        if (event.getMessage().startsWith("!custom") && !messageManager.overLimit()) {

            String message = event.getMessage();
            if (messageManager.isMod(channelName, event.getUser().getNick())) {

                if (message.indexOf("(") == -1 || message.indexOf(")") == -1 || message.substring(message.indexOf(")") + 1).isEmpty()
                        || message.substring(message.indexOf("(") + 1).charAt(0) != '!') {
                    messageManager.reduceMessages(1);
                    event.respond(", correct waifu syntax is !custom (!TRIGGER) MESSAGE");
                    return;
                }
                if (message.substring(message.indexOf(")") + 2).length() > 245) {
                    event.respond(", that message is too long to comprehend");
                    return;
                }
                String trigger = message.substring(message.indexOf("(") + 1, message.indexOf(")"));
                String customMessage = message.substring(message.indexOf(")") + 2);



                CustomMessage custom = new CustomMessage();
                if(customMessage.indexOf("-mod")!=-1)
                {
                    customMessage.replace("-mod", "");
                    custom.setRestriction(1);
                }


                custom.setMessage(customMessage);
                custom.setCustomTrigger(trigger);
                custom.setChannelId(channelEntity.getId());
                customMessageManager.addCustomMessage(custom);
                messageManager.reduceMessages(1);
                event.getBot().sendIRC().message(channelName, "Custom Message Saved");
            }
            return;
        }




        if (event.getMessage().startsWith("!dice")) {

            if (!messageManager.overLimit()) {
                messageManager.reduceMessages(1);
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
                event.respond("rolled a " + number + " " + flair);
            }
            return;
        }

        if(event.getMessage().startsWith("!")){
            List<CustomMessage> customMessages = customMessageManager.getCustomMessagesByChannel(channelEntity.getId());
            for(CustomMessage customMessage : customMessages)
            {
                if(event.getMessage().startsWith(customMessage.getCustomTrigger()))
                {
                    messageManager.reduceMessages(1);
                    event.getBot().sendIRC().message(channelName, customMessage.getMessage());
                }
            }
        }


    }


}
