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


    public TwitchUsers getTwitchUsers(String channelName) {
        String url = "https://tmi.twitch.tv/group/user/" + channelName + "/chatters";
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


}
