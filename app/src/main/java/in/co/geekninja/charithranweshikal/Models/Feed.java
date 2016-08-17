package in.co.geekninja.charithranweshikal.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * The type Feed.
 */
public class Feed implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("full_picture")
    @Expose
    private String full_picture;

    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("from")
    @Expose
    private From from;
    @SerializedName("created_time")
    @Expose
    private String created_time;

    /**
     * Gets created time.
     *
     * @return the created time
     */
    public String getCreated_time() {
        return created_time;
    }

    /**
     * Sets created time.
     *
     * @param created_time the created time
     */
    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    /**
     * Gets from.
     *
     * @return the from
     */
    public From getFrom() {
        return from;
    }

    /**
     * Sets from.
     *
     * @param from the from
     */
    public void setFrom(From from) {
        this.from = from;
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
     * Sets link.
     *
     * @param link the link
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Gets full picture.
     *
     * @return the full picture
     */
    public String getFull_picture() {
        return full_picture;
    }

    /**
     * Sets full picture.
     *
     * @param full_picture the full picture
     */
    public void setFull_picture(String full_picture) {
        this.full_picture = full_picture;
    }

    /**
     * Gets id.
     *
     * @return The  id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets message.
     *
     * @return The  message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets picture.
     *
     * @return The  picture
     */
    public String getPicture() {
        return picture;
    }

    /**
     * Sets picture.
     *
     * @param picture The picture
     */
    public void setPicture(String picture) {
        this.picture = picture;
    }

}