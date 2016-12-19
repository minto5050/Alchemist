package in.co.geekninja.charithranweshikal;

import in.co.geekninja.charithranweshikal.Models.Graphfeed;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by PS on 2/26/2016.
 * This interface is responsible for encapsulating all the web methods in the facebook graph API.
 */
public interface Fb {

    /**
     * Gets comments.
     *
     * @param fields           the fields
     * @param object           the object
     * @param access_token     the access token
     * @param responseCallback the response callback
     */
    @GET("/{object}/comments")
//eg:- fields=created_time,from,message,like_count
    void getComments(@Query("fields") String fields, @Path("object") String object, @Query("access_token") String access_token, Callback<Response> responseCallback);

    /**
     * Feed.
     *
     * @param fields       the fields
     * @param access_token the access token
     * @param time_format  the time format
     * @param callback     the callback
     */
    @GET("/feed")
    void feed(@Query("fields") String fields,
              @Query("access_token") String access_token, @Query("time_format") String time_format,
              Callback<Graphfeed> callback);

    /**
     * Previous.
     *
     * @param fields       the fields
     * @param format       the format
     * @param access_token the access token
     * @param since        the since
     * @param previous     the previous
     * @param time_format  the time format
     * @param callback     the callback
     */
    @GET("/feed")
    void previous(@Query("fields") String fields,
                  @Query("format") String format,
                  @Query("access_token") String access_token,
                  @Query("since") String since,
                  @Query("__previous") int previous, @Query("time_format") String time_format,
                  Callback<Graphfeed> callback);

    /**
     * Next.
     *
     * @param fields       the fields
     * @param format       the format
     * @param access_token the access token
     * @param limit        the limit
     * @param untill       the untill
     * @param paging_token the paging token
     * @param time_format  the time format
     * @param callback     the callback
     */
    @GET("/feed")
    void next(@Query("fields") String fields,
              @Query("format") String format,
              @Query("access_token") String access_token,
              @Query("limit") String limit,
              @Query("until") String untill,
              @Query("__paging_token") String paging_token, @Query("time_format") String time_format,
              Callback<Graphfeed> callback);

    /**
     * Feed with limit.
     *
     * @param fields       the fields
     * @param limit        the limit
     * @param access_token the access token
     * @param time_format  the time format
     * @param callback     the callback
     */
    @GET("/feed")
    void feedWithLimit(@Query("fields") String fields, @Query("limit") int limit,
                       @Query("access_token") String access_token, @Query("time_format") String time_format,
                       Callback<Graphfeed> callback);
}
