package dto;

/**
 * Created by palepail on 7/31/2015.
 */
public class WaifuDTO {
    int id;
    String name;
    String uploader;
    String claimed;
    int points;

    public String getUploader() {
        return uploader;
    }

    public int getPoints() {
        return points;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getClaimed() {
        return claimed;
    }

    public void setClaimed(String claimed) {
        this.claimed = claimed;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    String link;
}
