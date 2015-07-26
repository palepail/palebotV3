package models;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;

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
    private Integer channelid;

    @Id
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
    @Column(name = "DATE")
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
    public Integer getChannelid() {
        return channelid;
    }

    public void setChannelid(Integer channelid) {
        this.channelid = channelid;
    }
}
