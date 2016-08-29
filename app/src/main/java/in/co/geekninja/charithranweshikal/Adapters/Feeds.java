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


    /**
     * Gets title transliterated.
     *
     * @return the title transliterated
     */
    public String getTitle_transliterated() {
        Transliterator transliterator = Transliterator.getInstance("Malayalam-Latin; Lower; Latin-ASCII");
        String s=transliterator.transliterate(getTitle());
        return s;
        //return ;

    }


    /**
     * Is favorite boolean.
     *
     * @return the boolean
     */
    public boolean isFavorite() {
        return Favorite;
    }

    /**
     * Sets favorite.
     *
     * @param favorite the favorite
     */
    public void setFavorite(boolean favorite) {
        Favorite = favorite;
    }

    /**
     * Gets created date.
     *
     * @return the created date
     */
    public String getCreatedDate() {
        return createdDate;
    }

    /**
     * Sets created date.
     *
     * @param createdDate the created date
     */
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Gets user image.
     *
     * @return the user image
     */
    public String getUser_image() {
        return user_image;
    }

    /**
     * Sets user image.
     *
     * @param user_image the user image
     */
    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    /**
     * Gets full image.
     *
     * @return the full image
     */
    public String getFull_image() {
        return full_image;
    }

    /**
     * Sets full image.
     *
     * @param full_image the full image
     */
    public void setFull_image(String full_image) {
        this.full_image = full_image;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets desc.
     *
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Sets desc.
     *
     * @param desc the desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * Gets image url.
     *
     * @return the image url
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Sets image url.
     *
     * @param imageUrl the image url
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Sets from.
     *
     * @param from the from
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Gets from.
     *
     * @return the from
     */
    public String getFrom() {
        return from;
    }

    /**
     * Sets link.
     *
     * @param link the link
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Gets link.
     *
     * @return the link
     */
    public String getLink() {
        return link;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int compareTo(Object o) {
        String thisDate = ((Feeds) o).getCreatedDate();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        try {
            Date tdate = format.parse(thisDate);
            Date date2 = format.parse(createdDate);
            return tdate.compareTo(date2);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }


    }
}
