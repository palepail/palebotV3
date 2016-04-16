package bot;


import managers.ChannelManager;
import managers.ListenerManager;
import models.Channel;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.output.OutputIRC;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

/**
 * Created by palepail on 7/25/2015.
 */
public class Palebot {
    private static Palebot palebot;
    private static ListenerManager listenerManager;
    private static HashMap<String,PircBotX> botMap = new HashMap<>();
    private static OutputIRC serverManager;



    private static String BOT_NAME = "palebot";
    private static String BOT_SERVER = "irc.chat.twitch.tv";
    private static int BOT_PORT = 6667;
    private static String BOT_AUTH = "oauth:xfm9be1bwt7kvnr4nmbbtmq5imsohy";


    private static ChannelManager channelManager = new ChannelManager();

    public static Palebot getInstance(){
        if (palebot==null)
        {
            palebot = new Palebot();
            palebot.initializePalebot();
            listenerManager = ListenerManager.getInstance();
            MessageManager.startTimer();

        }
        return palebot;
    }

    public PircBotX getBotByChannelId(int channelId)
    {
        return botMap.get(channelManager.getChannelById(channelId).getName());
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

    public boolean activateBot(String channel){
        PircBotX pircBot;
        if(!botMap.containsKey(channel))
        {
            pircBot = createBot(channel);
            botMap.put(channel, pircBot);

        }
        if(getStatus(channel)!="CONNECTED") {

                pircBot = botMap.get(channel);

                addListeners(pircBot,channel);

                new StartBot(pircBot).start();

        }
        return true;
    }

    public boolean deactivateBot(String channel){

        if(getStatus(channel)=="CONNECTED")
        {
            PircBotX pircBot = botMap.get(channel);
            pircBot.getConfiguration().getListenerManager().shutdown(pircBot);
            removeListeners(pircBot);
            serverManager = new OutputIRC(pircBot);
            serverManager.quitServer();

        }
        return false;
    }

    private static PircBotX createBot(String channel){

        Configuration configuration = new Configuration.Builder()
                .setCapEnabled(true)
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

    public boolean updateListeners(int channelId)
    {
        Channel channel = channelManager.getChannelById(channelId);
        PircBotX pircbot = botMap.get(channel.getName());
      removeListeners(pircbot);
        addListeners(pircbot, channel.getName());
        return true;
    }

    private void removeListeners(PircBotX pircBotX){

        for(org.pircbotx.hooks.Listener listener :  pircBotX.getConfiguration().getListenerManager().getListeners())
        {
            pircBotX.getConfiguration().getListenerManager().removeListener(listener);
        }
    }

    private void addListeners(PircBotX pircBotX, String channelName){

        for(ListenerAdapter listener: listenerManager.getAllByChannelName(channelName))
        {
            if(!pircBotX.getConfiguration().getListenerManager().listenerExists(listener)) {
                pircBotX.getConfiguration().getListenerManager().addListener(listener);
            }
        }

    }


    class StartBot extends Thread{
        private PircBotX bot;

        public StartBot(PircBotX bot){
            this.bot = bot;
        }
    public void run(){
        try{
        bot.startBot();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IrcException e) {
            e.printStackTrace();
        }
    }

    }



}