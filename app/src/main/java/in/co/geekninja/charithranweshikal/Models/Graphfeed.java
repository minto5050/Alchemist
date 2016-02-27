package in.co.geekninja.charithranweshikal.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Graphfeed implements Serializable{

    @SerializedName("data")
    @Expose
    private List<Feed> data = new ArrayList<Feed>(
            
    );
    @SerializedName("paging")
    @Expose
    private Paging paging;

    /**
     *
     * @return
     * The data
     */
    public List<Feed> getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(List<Feed> data) {
        this.data = data;
    }

    /**
     *
     * @return
     * The paging
     */
    public Paging getPaging() {
        return paging;
    }

    /**
     *
     * @param paging
     * The paging
     */
    public void setPaging(Paging paging) {
        this.paging = paging;
    }

}