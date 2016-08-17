package in.co.geekninja.charithranweshikal.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * The type Graphfeed.
 */
public class Graphfeed implements Serializable{

    @SerializedName("data")
    @Expose
    private List<Feed> data = new ArrayList<Feed>(
            
    );
    @SerializedName("paging")
    @Expose
    private Paging paging;

    /**
     * Gets data.
     *
     * @return The  data
     */
    public List<Feed> getData() {
        return data;
    }

    /**
     * Sets data.
     *
     * @param data The data
     */
    public void setData(List<Feed> data) {
        this.data = data;
    }

    /**
     * Gets paging.
     *
     * @return The  paging
     */
    public Paging getPaging() {
        return paging;
    }

    /**
     * Sets paging.
     *
     * @param paging The paging
     */
    public void setPaging(Paging paging) {
        this.paging = paging;
    }

}