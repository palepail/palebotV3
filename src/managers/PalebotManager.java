package managers;

import bot.Palebot;

/**
 * Created by palepail on 7/29/2015.
 */

public class PalebotManager {
    Palebot palebot =  Palebot.getInstance();

    public boolean isOn(String channel) {
        return palebot.isOn(channel);
    }

    public void activateBot(String channel) {
        palebot.activateBot(channel);
    }

    public boolean deactivateBot(String channel) {
        return palebot.deactivateBot(channel);
    }
}
