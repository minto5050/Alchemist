package in.co.geekninja.charithranweshikal;

import in.co.geekninja.charithranweshikal.Models.Graphfeed;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by PS on 2/26/2016.
 */
public interface Fb  {
    @GET("/feed")
    void feed(@Query("fields")String fields,@Query("access_token")String access_token, Callback<Graphfeed> callback);
    @GET("/feed") void previous(@Query("fields")String fields,@Query("format")String format,@Query("access_token")String access_token,@Query("since")String since,@Query("__previous")int previous, Callback<Graphfeed> callback);
}
