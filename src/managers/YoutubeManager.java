package managers;

import bot.YoutubeVideo;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by palepail on 8/17/2015.
 */
public class YoutubeManager {

    private static String API_KEY = "AIzaSyDd5oIjLRX_L1nUozmqnKTP1GtYOPM8ZkQ";

    private static YoutubeManager  youtubeManager= new YoutubeManager();

    public static YoutubeManager getInstance() {
        return youtubeManager;
    }

    public static String getApiKey(){
        return API_KEY;
    }

    public static HashMap<String, List<YoutubeVideo>> playListsMap = new HashMap<>();


    public List<YoutubeVideo> getPlaylist(String channelName)
    {
        if(playListsMap.containsKey(channelName))
        {
            return playListsMap.get(channelName);
        }
        else return null;
    }

    public void setPlayList(String channelName, List<YoutubeVideo> playList)
    {
        playListsMap.put(channelName, playList);
    }

    public YoutubeVideo getVideoDetails(String videoId)
    {
        String url =  "https://www.googleapis.com/youtube/v3/videos?id="+videoId+"&key="+API_KEY+"&part=snippet,contentDetails";
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

        YoutubeVideo video = gson.fromJson(jsonString, YoutubeVideo.class);
        return video;
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
