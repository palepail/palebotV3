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
import java.util.HashMap;
import java.util.List;

/**
 * Created by palepail on 8/5/2015.
 */
public class TwitchManager {

    private static String CLIENT_ID = "7eghc5wv04zimbltpovl83a89ajps8t";
    private static TwitchManager twitchManager = new TwitchManager();

    public static TwitchManager getInstance() {
        return twitchManager;
    }

    public static String getClientId(){
        return CLIENT_ID;
    }
    public static HashMap<String, List<String>> modList = new HashMap<>();
    public static HashMap<String, List<String>> subList = new HashMap<>();
    int modRefresh = 0;

    public TwitchUsers getTwitchUsers(String channelName) {
        String url = "https://tmi.twitch.tv/group/user/" + channelName + "/chatters";
        return getUsers(url);
    }

    public TwitchUsers getSubscriptions(String channelName)
    {
        String url = "https://api.twitch.tv/kraken/" + channelName + "/subscriptions";
        return getUsers(url);
    }

    private TwitchUsers getUsers(String url)
    {
        String charSet = java.nio.charset.StandardCharsets.UTF_8.name();
        String jsonString="";
        try {
            URLConnection connection = new URL(url).openConnection();

            connection.setRequestProperty("Accept-Charset", charSet);
            connection.addRequestProperty("Client-Id", CLIENT_ID);
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

    public boolean isMod(String channelName,String userName){
        updateMods(channelName);
        return (userName.equalsIgnoreCase("palepail")||modList.get(channelName).contains(userName.toLowerCase()));
    }

    public void updateMods(String channelName){
        if(modList.containsKey(channelName)) {
            modList.remove(channelName);
        }
        modList.put(channelName, getMods(channelName));
    }

    public static List<String> getMods(String channelName){
        TwitchUsers users = twitchManager.getTwitchUsers(channelName.substring(1));
        if(users != null) {
            return users.getChatters().getModerators();
        }
        return new ArrayList();
    }

    public boolean isSubscriber(String channelName,String userName) {
        return (subList.get(channelName).contains(userName.toLowerCase()));
    }
}
