package models;

import javax.persistence.*;

/**
 * Created by palepail on 9/29/2015.
 */
@Entity
public class Spam {
    private int id;
    private String offence;
    private int channelId;
    private String user;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "OFFENCE")
    public String getOffence() {
        return offence;
    }

    public void setOffence(String offence) {
        this.offence = offence;
    }

    @Basic
    @Column(name = "CHANNELID")
    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    @Basic
    @Column(name = "USER")
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Spam spam = (Spam) o;

        if (id != spam.id) return false;
        if (channelId != spam.channelId) return false;
        if (offence != null ? !offence.equals(spam.offence) : spam.offence != null) return false;
        if (user != null ? !user.equals(spam.user) : spam.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (offence != null ? offence.hashCode() : 0);
        result = 31 * result + channelId;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }
}
