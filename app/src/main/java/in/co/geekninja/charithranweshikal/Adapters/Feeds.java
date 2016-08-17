package in.co.geekninja.charithranweshikal.Adapters;

import com.ibm.icu.text.Transliterator;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by PS on 2/26/2016.
 */
public class Feeds implements Serializable, Comparable {
    private String title;
    private String desc;
    private String imageUrl;
    private String full_image;
    private String from;
    private String link;
    private String user_image;
    private String createdDate;
    private boolean Favorite;
    private String id;


    public String getTitle_transliterated() {
        Transliterator transliterator = Transliterator.getInstance("Malayalam-Latin; Lower; Latin-ASCII");
        String s=transliterator.transliterate(getTitle());
        return s;
        //return ;

    }


    public boolean isFavorite() {
        return Favorite;
    }

    public void setFavorite(boolean favorite) {
        Favorite = favorite;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getFull_image() {
        return full_image;
    }

    public void setFull_image(String full_image) {
        this.full_image = full_image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int compareTo(Object o) {
        String thisDate = ((Feeds) o).getCreatedDate();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        try {
            Date date = format.parse(thisDate);
            long utcTimestamp = date.getTime();
            Date date2 = format.parse(createdDate);
            long utcTimestamp2 = date2.getTime();
            if (utcTimestamp > utcTimestamp2) {
                return 1;
            }
            if (utcTimestamp == utcTimestamp) {
                return 0;
            } else {
                return -1;
            }
        } catch (Exception e) {
            return 0;
        }


    }
}
