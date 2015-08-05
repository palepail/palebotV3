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
    DefaultActor actor = new DefaultActor();

    CustomMessageManager customMessageManager = new CustomMessageManager();
    public static final String NAME = "DEFAULT";

    @Override
    public void onMessage(MessageEvent event) {

        if (event.getMessage().startsWith("!")) {

            actor.setValues(event);

            if (event.getMessage().startsWith("!palebot") && !messageManager.overLimit()) {
                actor.palebotInfo(event);
                return;
            }

            if (event.getMessage().startsWith("!suicide") && !messageManager.overLimit()) {
                actor.selfTimeout(event);
                return;
            }

            if (event.getMessage().startsWith("!custom delete") && !messageManager.overLimit()) {
                if (messageManager.isMod(event.getChannel().getName(), event.getUser().getNick())) {
                    actor.deleteCustomMessage(event);
                }
                return;
            }

            if (event.getMessage().startsWith("!custom") && !messageManager.overLimit()) {
                if (messageManager.isMod(event.getChannel().getName(), event.getUser().getNick())) {
                    actor.saveCustomMessage(event);
                }
                return;
            }


            if (event.getMessage().startsWith("!dice") && !messageManager.overLimit()) {
                actor.rollDice(event);
                return;
            }

            if (!messageManager.overLimit()) {
                actor.customTrigger(event);
            }
        }

    }


}
