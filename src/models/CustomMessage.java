package models;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by palepail on 8/4/2015.
 */
@Entity
public class CustomMessage {
    private int id;
    private int channelId;
    private String customTrigger;
    private String message;
    private Integer restriction;

    @Id
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "CHANNEL_ID")
    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    @Basic
    @Column(name = "CUSTOM_TRIGGER")
    public String getCustomTrigger() {
        return customTrigger;
    }

    public void setCustomTrigger(String customTrigger) {
        this.customTrigger = customTrigger;
    }

    @Basic
    @Column(name = "MESSAGE")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Basic
    @Column(name = "RESTRICTION")
    public Integer getRestriction() {
        return restriction;
    }

    public void setRestriction(Integer restriction) {
        this.restriction = restriction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomMessage that = (CustomMessage) o;

        if (id != that.id) return false;
        if (channelId != that.channelId) return false;
        if (customTrigger != null ? !customTrigger.equals(that.customTrigger) : that.customTrigger != null)
            return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        if (restriction != null ? !restriction.equals(that.restriction) : that.restriction != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + channelId;
        result = 31 * result + (customTrigger != null ? customTrigger.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (restriction != null ? restriction.hashCode() : 0);
        return result;
    }
}
