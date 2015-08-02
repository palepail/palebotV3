package bot;


import com.google.common.io.CharStreams;
import com.google.gson.Gson;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by palepail on 7/31/2015.
 */
public class MessageManager {

    public static MessageManager messageManager = new MessageManager();


    static final int MAX_MESSAGES = 19;
    static ArrayList<String> lockArray = new ArrayList();
    static int messageCount = MAX_MESSAGES;
    static Timer timer = new Timer();

    public static MessageManager getInstance() {
        return messageManager;
    }

    public static void delayMessage(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

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


    public static void lockAction(String id, int time) {
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

    public TwitchUsers getTwitchUsers(String channelName) {
        String url = "http://tmi.twitch.tv/group/user/" + channelName + "/chatters";
        String charSet = java.nio.charset.StandardCharsets.UTF_8.name();
        String jsonString="";
        try {
            URLConnection connection = new URL(url).openConnection();

            connection.setRequestProperty("Accept-Charset", charSet);
            InputStream response = connection.getInputStream();

            jsonString = convertInputStreamToString(response).replace("\n", "");


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        TwitchUsers users = gson.fromJson(jsonString, TwitchUsers.class);
        return users;


    }

    private String convertInputStreamToString(InputStream is){
        String text = null;
        try (final Reader reader = new InputStreamReader(is)) {
            text = CharStreams.toString(reader);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    public void reduceMessages(int num) {
        messageCount -= num;
    }


}
