package bot;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by palepail on 8/17/2015.
 */
public class YoutubeVideo {


     String uploader;
     int channelId;
     String kind;
     String etag;
     PageInfo pageInfo;
     public List<Item> items;


    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }




    class PageInfo {
        int totalResults;
        int resultsPerPage;
    }
    public class Item{
        String kind;
        String etag;
        String id;
       public Snippet snippet;
        ContentDetails contentDetails;



        public class ContentDetails{
            String duration;
            String dimention;
            String definition;
            String caption;
            boolean licensedContnent;
        }

       public class Snippet{
            String publishedAt;
            String channelId;
            public String title;
            String description;
            Thumbnails thumbnails;
            String channelTitle;
            List<String> tags;
            int categoryId;
            String liveBroadcastContent;
            Localized localized;

            class Localized{
                String title;
                String description;
            }

            class Thumbnails{
                @SerializedName("default")
                Thumbnail defaultRes;
                Thumbnail medium;
                Thumbnail high;
                Thumbnail standard;
                Thumbnail maxres;

                class Thumbnail{
                    String url;
                    int width;
                    int height;
                }
            }
        }

    }


}
