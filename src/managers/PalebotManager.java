package managers;

import bot.Palebot;
import dao.PalebotDAO;
import org.pircbotx.PircBotX;

/**
 * Created by palepail on 7/29/2015.
 */

public class PalebotManager {
    static PalebotManager  palebotManager= new PalebotManager();
    static PalebotDAO palebotDAO = new PalebotDAO();
    static Palebot palebot =  Palebot.getInstance();


    public static PalebotManager getInstance(){
        return palebotManager;
    }

    public String getStatus(String channel) {
        return palebot.getStatus(channel);
    }

    public boolean activateBot(String channel) {
       return palebot.activateBot(channel);
    }

    public PircBotX getBotByChannelId(int channelId){
        return palebot.getBotByChannelId(channelId);
    }

    public boolean isPalebotAdmin(String userName){return palebotDAO.isPalebotAdmin(userName);}

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
