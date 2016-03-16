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
    MessageManager messageManager;
    DefaultActor actor = new DefaultActor();
    public static final String NAME = "DEFAULT";

    @Override
    public void onMessage(MessageEvent event) {

        if (event.getMessage().startsWith("!")) {
            String channelName = event.getChannel().getName();
            messageManager = MessageManager.getInstance(channelName);
            actor.setValues(event);

            if (event.getMessage().startsWith("!ban") && !messageManager.overLimit()) {
                actor.palebotBan(event);
                return;
            }

            if (event.getMessage().startsWith("!unban") && !messageManager.overLimit()) {
                actor.palebotUnban(event);
                return;
            }

            if (event.getMessage().startsWith("!timeout") && !messageManager.overLimit()) {
                actor.palebotTimeout(event);
                return;
            }

            if (event.getMessage().equals("!palebot") && !messageManager.overLimit()) {
                actor.palebotInfo(event);
                return;
            }


            if (event.getMessage().equals("!commands") && !messageManager.overLimit()) {
                actor.palebotCommands(event);
                return;
            }

            if (event.getMessage().equals("!suicide") && !messageManager.overLimit()) {
                actor.selfTimeout(event);
                return;
            }

            if (event.getMessage().startsWith("!custom delete") && !messageManager.overLimit()) {
                if (messageManager.isMod(event.getChannel().getName(), event.getUser().getNick())) {
                    actor.deleteCustomMessage(event);
                }
                return;
            }

            if (event.getMessage().startsWith("!custom all") && !messageManager.overLimit()) {

                    actor.getAllCustomMessages(event);

                return;
            }

            if (event.getMessage().startsWith("!custom") && !messageManager.overLimit()) {
                if (messageManager.isMod(event.getChannel().getName(), event.getUser().getNick())) {
                    actor.saveCustomMessage(event);
                }
                return;
            }


            if (event.getMessage().equals("!dice") && !messageManager.overLimit()) {
                actor.rollDice(event);
                return;
            }

            if (event.getMessage().startsWith("!")) {
                actor.customTrigger(event);
                return;
            }
        }

    }


}
