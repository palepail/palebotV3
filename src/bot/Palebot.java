package bot;


import managers.ChannelManager;
import models.Channel;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.output.OutputChannel;
import org.pircbotx.output.OutputIRC;
import org.pircbotx.PircBotX.State;

import java.io.IOException;
import java.util.List;

/**
 * Created by palepail on 7/25/2015.
 */
public class Palebot {
    private static Palebot palebot;
    private static PircBotX pircBot;
    private static OutputIRC serverManager;
    private static OutputChannel outChan;

    private static String BOT_NAME = "palebot";
    private static String BOT_SERVER = "irc.twitch.tv";
    private static int BOT_PORT = 6667;
    private static String BOT_AUTH = "oauth:xfm9be1bwt7kvnr4nmbbtmq5imsohy";
    private static String BOT_DEFAULT_CHANNEL = "#palebot";

    private static ChannelManager channelManager = new ChannelManager();

    public static Palebot getInstance(){
        if (palebot==null)
        {
            palebot = new Palebot();
            if(palebot.pircBot == null) {
                palebot.createPalebot();
            }
        }
        return palebot;
    }
    public boolean isOn(){

        return pircBot.getState() == State.CONNECTED;
    }

    private static void createPalebot(){
        Configuration configuration = new Configuration.Builder()
                .addListener(new DefaultListener())
                .setName(BOT_NAME)
                .setRealName(BOT_NAME)
                .setLogin(BOT_NAME)
                .setServerPassword(BOT_AUTH)
                .setServerHostname(BOT_SERVER)
                .addAutoJoinChannel(BOT_DEFAULT_CHANNEL)
                .buildForServer(BOT_SERVER, BOT_PORT, BOT_AUTH);

        pircBot = new PircBotX(configuration);





    }

    public void activateBot(){
        if(!pircBot.isConnected()) {
            try {
                pircBot.startBot();
                serverManager = new OutputIRC(pircBot);
                List<Channel> channels = channelManager.getAll();
                for(Channel channel : channels) {
                    if(!channel.getName().equals(BOT_NAME)) {
                       serverManager.joinChannel("#"+channel.getName());

                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (IrcException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean deactivateBot(){

        if(pircBot.isConnected())
        {
            serverManager = new OutputIRC(pircBot);
            serverManager.quitServer();
            return false;
        }
        return false;
    }





}