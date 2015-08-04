package managers;

import bot.Palebot;

/**
 * Created by palepail on 7/29/2015.
 */

public class PalebotManager {
    static PalebotManager  palebotManager= new PalebotManager();
    static Palebot palebot =  Palebot.getInstance();


    public static PalebotManager getInstance(){
        return palebotManager;
    }

    public String getStatus(String channel) {
        return palebot.getStatus(channel);
    }

    public void activateBot(String channel) {
        palebot.activateBot(channel);
    }


    public boolean deactivateBot(String channel) {
        return palebot.deactivateBot(channel);
    }


    public void deleteChannelByName(String channelName)
    {
        palebot.deleteChannelByName(channelName);
    }

    public boolean updateListeners(int channelId){
       return palebot.updateListeners(channelId);
    }


}
