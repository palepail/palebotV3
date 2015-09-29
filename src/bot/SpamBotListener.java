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
public class SpamBotListener extends ListenerAdapter {
    MessageManager messageManager;
    SpamBotActor actor = new SpamBotActor();
    public static final String NAME = "SPAM";

    @Override
    public void onMessage(MessageEvent event) {

        if (event.getMessage().startsWith("!")) {
            actor.setValues(event);


            if(event.getMessage().startsWith("!spam add "))
            {
                actor.addSpam(event);
                return;
            }
        }
        actor.checkIfBot(event);

    }


}
