package bot;

/**
 * Created by palepail on 8/16/2015.
 */


import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;


/**
 * Created by palepail on 7/26/2015.
 */
public class YoutubeListener extends ListenerAdapter {
    MessageManager messageManager;
    YoutubeActor actor = new YoutubeActor();
    public static final String NAME = "YOUTUBE";


    @Override
    public void onMessage(MessageEvent event) {

        if (event.getMessage().startsWith("!")) {
            String channelName = event.getChannel().getName();
            messageManager = MessageManager.getInstance(channelName);
            actor.setValues(event);

            if (event.getMessage().startsWith("!request") && !messageManager.overLimit() && !actor.tooManyRequests(event)) {
                actor.sendYoutubeRequest(event);
                return;
            }
            if (event.getMessage().equals("!song") && !messageManager.overLimit()) {
                actor.sendCurrentSongQuery(event);
                return;
            }

        }

    }


}
