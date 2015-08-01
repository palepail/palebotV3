package models;

import javax.persistence.*;

/**
 * Created by palepail on 7/31/2015.
 */
@Entity
public class Waifu {
    private int id;
    private String name;
    private String link;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "link")
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Waifu waifu = (Waifu) o;

        if (id != waifu.id) return false;
        if (name != null ? !name.equals(waifu.name) : waifu.name != null) return false;
        if (link != null ? !link.equals(waifu.link) : waifu.link != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (link != null ? link.hashCode() : 0);
        return result;
    }
}
