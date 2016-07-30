package in.co.geekninja.charithranweshikal;

import in.co.geekninja.charithranweshikal.Models.Graphfeed;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by PS on 2/26/2016.
 */
public interface Fb  {
    @GET("/{user-id}/picture")
    void getPicture(@Path("user-id")String user_id, @Query("access_token")String access_token, @Query("width")int width, Callback<Response> responseCallback);
    @GET("/feed")
    void feed(@Query("fields")String fields,
              @Query("access_token")String access_token,@Query("time_format") String time_format,
              Callback<Graphfeed> callback);
    @GET("/feed")
    void previous(@Query("fields")String fields,
                  @Query("format")String format,
                  @Query("access_token")String access_token,
                  @Query("since")String since,
                  @Query("__previous")int previous,@Query("time_format") String time_format,
                  Callback<Graphfeed> callback);
    @GET("/feed")
    void next(@Query("fields")String fields,
              @Query("format")String format,
              @Query("access_token")String access_token,
              @Query("limit") String limit,
              @Query("until")String untill,
              @Query("__paging_token")String paging_token,@Query("time_format") String time_format,
              Callback<Graphfeed> callback);
    @GET("/feed")
    void feedWithLimit(@Query("fields")String fields,@Query("limit") int limit,
                       @Query("access_token")String access_token,@Query("time_format") String time_format,
                       Callback<Graphfeed> callback);
}
