package bot;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.output.OutputChannel;
import org.pircbotx.output.OutputIRC;

import java.io.IOException;

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
    private static String BOT_AUTH = "oauth:4ddb4uuqgehsatte852blk9s6jbm1n";
    private static String BOT_DEFAULT_CHANNEL = "#palebot";

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

    private static void createPalebot(){
        Configuration configuration = new Configuration.Builder()
                .addListener(new DefaultListener())
                .setName(BOT_NAME)
                .setServerHostname(BOT_SERVER)
                .addAutoJoinChannel(BOT_DEFAULT_CHANNEL)
                .buildForServer(BOT_SERVER, BOT_PORT, BOT_AUTH);
        pircBot = new PircBotX(configuration);

    }

    public void activateBot(){
        try {
            pircBot.startBot();
            serverManager= new OutputIRC(pircBot);
            //set Active Channel

            outChan = new OutputChannel(pircBot, pircBot.getUserChannelDao().getChannel(BOT_DEFAULT_CHANNEL));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IrcException e) {
            e.printStackTrace();
        }
    }

    public void deactivateBot(){
        pircBot.stopBotReconnect();
        serverManager.quitServer();

    }




}