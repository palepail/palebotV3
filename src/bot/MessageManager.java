package bot;


import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import managers.ChannelManager;
import managers.PalebotManager;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.ActionEvent;
import org.pircbotx.hooks.events.MessageEvent;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * Created by palepail on 7/31/2015.
 */
public class MessageManager {

    private static HashMap<String,MessageManager> messageManagerMap = new HashMap<>();
   static TwitchManager twitchManager = TwitchManager.getInstance();

    static final int MAX_MESSAGES = 19;
    static ArrayList<String> lockArray = new ArrayList();
    static int messageCount = MAX_MESSAGES;
    static Timer timer = new Timer();
    static PalebotManager palebotManager = PalebotManager.getInstance();
    static ChannelManager channelManager = new ChannelManager();

    private static HashMap<String, List<String>> modList = new HashMap<>();

    public static MessageManager getInstance(String channelName) {
        if(messageManagerMap.containsKey(channelName)){
            return messageManagerMap.get(channelName);
        }else{
            MessageManager manager = new MessageManager();
            modList.put(channelName, getMods(channelName));
            messageManagerMap.put(channelName, manager);
            return messageManagerMap.get(channelName);
        }
    }


    public static MessageManager getInstance(int channelId) {
        String channelName = channelManager.getChannelById(channelId).getName();
        return getInstance(channelName);
    }

    public static void delayMessage(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

    }

    public static boolean isLocked(String id){
        if (lockArray.contains(id)) {
            return true;
        }
        return false;
    }

    public static boolean lock(String id, int time) {
        if (lockArray.contains(id)) {
            return false;
        }
        lockAction(id, time);
        return true;
    }

    public static void startTimer() {
        timer.schedule(new addMessage(), 0, // initial delay
                666);

    }


    private static void lockAction(String id, int time) {
        lockArray.add(id);
        timer.schedule(new unlockAction(id), time);

    }

    static class unlockAction extends TimerTask {
        private String id;

        unlockAction(String id) {
            this.id = id;
        }

        public void run() {
            lockArray.remove(id);

        }
    }

    public static void stopTimer() {
        timer.cancel();

    }

    static class addMessage extends TimerTask {
        public void run() {
            if (messageCount < 19) {
                messageCount++;
            }
        }
    }


    public boolean overLimit() {
        if (messageCount < 0) {
            return true;
        } else {
            return false;
        }

    }



    public void sendMessage(MessageEvent event,String message){
        reduceMessages(1);
        event.getBot().sendIRC().message(event.getChannel().getName(), message);
    }
    public void sendMessage(ActionEvent event,String message){
        reduceMessages(1);
        event.getBot().sendIRC().message(event.getChannel().getName(), message);
    }
    public void sendMessage(int channelId, String message){
        reduceMessages(1);
        PircBotX pircBotX = palebotManager.getBotByChannelId(channelId);
        pircBotX.sendIRC().message('#'+channelManager.getChannelById(channelId).getName(), message);
    }

    public boolean isMod(String channelName,String userName){

        return (userName.equalsIgnoreCase("palepail")||modList.get(channelName).contains(userName));
    }

    public void updateMods(String channelName){
        modList.put(channelName, getMods(channelName));
    }

    public static List<String> getMods(String channelName){
        TwitchUsers users = twitchManager.getTwitchUsers(channelName.substring(1));
        return users.getChatters().getModerators();
    }


    public void reduceMessages(int num) {
        messageCount -= num;
    }


}
