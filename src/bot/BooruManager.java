package bot;

import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


/**
 * Created by palepail on 8/24/2016.
 */
public class BooruManager {

    private static BooruManager booruManager = new BooruManager();
    String API_KEY = "AIzaSyDd5oIjLRX_L1nUozmqnKTP1GtYOPM8ZkQ";

    public static BooruManager getInstance() {
        return booruManager;
    }

    public String getShortenedUrl(String longUrl)
    {
        String url = "https://www.googleapis.com/urlshortener/v1/url?key="+API_KEY;

        String data="{\"longUrl\": \""+longUrl+"\"}";
        String jsonString = "";
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build(); //Use this instead
                HttpPost request = new HttpPost(url);
                StringEntity params =new StringEntity(data);
                request.addHeader("content-type", "application/json");
                request.setEntity(params);
                CloseableHttpResponse response = httpClient.execute(request);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    try {

                        jsonString = convertInputStreamToString(instream).replace("\n", "");
                    }  finally {
                        instream.close();
                    }
                }
            } finally {
                response.close();
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        ShortURL shortUrl = gson.fromJson(jsonString, ShortURL.class);
        return shortUrl.id;

    }

    public BooruImage[] getBooruImages(String tag) {
        String url = "https://danbooru.donmai.us/posts.json?tags="+tag+"+Rating:Safe";
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
       BooruImage[] images = gson.fromJson(jsonString, BooruImage[].class);
        return images;


    }

    public GelBooruImage[] getGelBooruImages(String tag) {
        String url = "http://gelbooru.com/index.php?page=dapi&s=post&q=index&pid=1&limit=50&tags="+tag+"+rating:Safe&json=1";
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
        GelBooruImage[] images = gson.fromJson(jsonString, GelBooruImage[].class);
        return images;


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
