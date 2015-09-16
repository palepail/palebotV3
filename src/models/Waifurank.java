package models;

import javax.persistence.*;

/**
 * Created by palepail on 9/16/2015.
 */
@Entity
public class WaifuRank {
    private int id;
    private int tier;
    private String rank;
    private int channelId;

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
    @Column(name = "TIER")
    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    @Basic
    @Column(name = "RANK")
    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    @Basic
    @Column(name = "CHANNEL_ID")
    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WaifuRank waifuRank = (WaifuRank) o;

        if (id != waifuRank.id) return false;
        if (tier != waifuRank.tier) return false;
        if (channelId != waifuRank.channelId) return false;
        if (rank != null ? !rank.equals(waifuRank.rank) : waifuRank.rank != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + tier;
        result = 31 * result + (rank != null ? rank.hashCode() : 0);
        result = 31 * result + channelId;
        return result;
    }
}
