package managers;

import bot.Palebot;

/**
 * Created by palepail on 7/29/2015.
 */

public class PalebotManager {
    static Palebot palebot =  Palebot.getInstance();

    public String getStatus(String channel) {
        return palebot.getStatus(channel);
    }

    public void activateBot(String channel) {
        palebot.activateBot(channel);
    }

    public void deleteChannelByName(String channelName)
    {
        palebot.deleteChannelByName(channelName);
    }

    public boolean deactivateBot(String channel) {
        return palebot.deactivateBot(channel);
    }
}
