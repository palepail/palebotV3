package managers;

import bot.Palebot;

/**
 * Created by palepail on 7/29/2015.
 */

public class PalebotManager {
    Palebot palebot =  Palebot.getInstance();

    public boolean isOn() {
        return palebot.isOn();
    }

    public void activateBot() {
        palebot.activateBot();
    }

    public boolean deactivateBot() {
        return palebot.deactivateBot();
    }
}
