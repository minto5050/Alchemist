package in.co.geekninja.charithranweshikal.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


/**
 * The type Paging.
 */
public class Paging implements Serializable{

    @SerializedName("previous")
    @Expose
    private String previous;
    @SerializedName("next")
    @Expose
    private String next;

    /**
     * Gets previous.
     *
     * @return The  previous
     */
    public String getPrevious() {
        return previous;
    }

    /**
     * Sets previous.
     *
     * @param previous The previous
     */
    public void setPrevious(String previous) {
        this.previous = previous;
    }

    /**
     * Gets next.
     *
     * @return The  next
     */
    public String getNext() {
        return next;
    }

    /**
     * Sets next.
     *
     * @param next The next
     */
    public void setNext(String next) {
        this.next = next;
    }

}