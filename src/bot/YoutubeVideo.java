package bot;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by palepail on 8/17/2015.
 */
public class YoutubeVideo {


    String uploader;
    String kind;
    String etag;
    PageInfo pageInfo;
    List<Item> items;

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    class PageInfo{
        int totalResults;
        int resultsPerPage;
    }
    class Item{
        String kind;
        String etag;
        String id;
        Snippet snippet;

        class Snippet{
            String publishedAt;
            String channelId;
            String title;
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
