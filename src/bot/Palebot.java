package bot;


import managers.ChannelManager;
import managers.ListenerManager;
import models.Channel;
import models.Listener;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.output.OutputIRC;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by palepail on 7/25/2015.
 */
public class Palebot {
    private static Palebot palebot;
    private static MessageManager messageManager;
    private static ListenerManager listenrManager;
    private static HashMap<String,PircBotX> botMap = new HashMap<>();
    private static OutputIRC serverManager;



    private static String BOT_NAME = "palebot";
    private static String BOT_SERVER = "irc.twitch.tv";
    private static int BOT_PORT = 6667;
    private static String BOT_AUTH = "oauth:xfm9be1bwt7kvnr4nmbbtmq5imsohy";


    private static ChannelManager channelManager = new ChannelManager();

    public static Palebot getInstance(){
        if (palebot==null)
        {
            palebot = new Palebot();
            palebot.initializePalebot();
            messageManager = MessageManager.getInstance();
            listenrManager = ListenerManager.getInstance();
            messageManager.startTimer();

        }
        return palebot;
    }


    public String getStatus(String channel){
        PircBotX pircBot = botMap.get(channel);
        if(pircBot != null) {
            return pircBot.getState().toString();
        }
        else{
            return "ERROR";
        }
    }

    private static void initializePalebot(){

        List<Channel> channels = channelManager.getAll();

        for(Channel channel : channels)
        {
            PircBotX pircBot = createBot(channel.getName());
            botMap.put(channel.getName(),pircBot);
        }

    }

    public void activateBot(String channel){
        PircBotX pircBot;
        if(!botMap.containsKey(channel))
        {
            pircBot = createBot(channel);
            botMap.put(channel, pircBot);

        }
        if(getStatus(channel)!="CONNECTED") {
            try {
                pircBot = botMap.get(channel);

                addListeners(pircBot,channel);

                pircBot.startBot();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (IrcException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean deactivateBot(String channel){

        if(getStatus(channel)=="CONNECTED")
        {
            PircBotX pircBot = botMap.get(channel);
            serverManager = new OutputIRC(pircBot);
            serverManager.quitServer();
            return false;
        }
        return false;
    }

    private static PircBotX createBot(String channel){

        Configuration configuration = new Configuration.Builder()
                .addListener(new DefaultListener())
                .setName(BOT_NAME)
                .setRealName(BOT_NAME)
                .setLogin(BOT_NAME)
                .setServerPassword(BOT_AUTH)
                .setServerHostname(BOT_SERVER)
                .addAutoJoinChannel('#' + channel)
                .buildForServer(BOT_SERVER, BOT_PORT, BOT_AUTH);

        return new PircBotX(configuration);
    }

    public void deleteChannelByName(String channelName){
        deactivateBot(channelName);
        botMap.remove(channelName);
    }

    private void addListeners(PircBotX pircBotX, String channelName){

        for(ListenerAdapter listener: listenrManager.getAllByChannelName(channelName))
        {
            pircBotX.getConfiguration().getListenerManager().addListener(listener);
        }

    }


}