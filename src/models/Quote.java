package models;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by palepail on 7/23/2015.
 */
@Entity
public class Quote {
    private int id;
    private String quote;
    private Date date;
    private String quotee;
    private String author;
    private Integer channelId;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "QUOTE")
    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    @Basic
    @Column(name = "DATE", columnDefinition = "DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Basic
    @Column(name = "QUOTEE")
    public String getQuotee() {
        return quotee;
    }

    public void setQuotee(String quotee) {
        this.quotee = quotee;
    }

    @Basic
    @Column(name = "AUTHOR")
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Quote quote1 = (Quote) o;

        if (id != quote1.id) return false;
        if (quote != null ? !quote.equals(quote1.quote) : quote1.quote != null) return false;
        if (date != null ? !date.equals(quote1.date) : quote1.date != null) return false;
        if (quotee != null ? !quotee.equals(quote1.quotee) : quote1.quotee != null) return false;
        if (author != null ? !author.equals(quote1.author) : quote1.author != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (quote != null ? quote.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (quotee != null ? quotee.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        return result;
    }

    @Basic
    @Column(name = "CHANNELID")
    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelid) {
        this.channelId = channelid;
    }
}
