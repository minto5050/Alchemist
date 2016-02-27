package in.co.geekninja.charithranweshikal.Services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import in.co.geekninja.charithranweshikal.Adapters.Feeds;
import in.co.geekninja.charithranweshikal.Fb;
import in.co.geekninja.charithranweshikal.Misc.Boilerplate;
import in.co.geekninja.charithranweshikal.Models.Feed;
import in.co.geekninja.charithranweshikal.Models.Graphfeed;
import in.co.geekninja.charithranweshikal.Storage.Database;
import in.co.geekninja.charithranweshikal.Storage.DbHandler;
import in.co.geekninja.charithranweshikal.Urls;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 *
 * helper methods.
 */
public class Fetcher extends IntentService {

    Fb graphApi;
    public static final String ACTION_CURRENT = "in.co.geekninja.charithranweshikal.Services.action.CURRENT";
    public static final String ACTION_PREVIOUS = "in.co.geekninja.charithranweshikal.Services.action.PREVIOUS";
    String token="NoN";
    private static final String EXTRA_SINCE = "in.co.geekninja.charithranweshikal.Services.extra.SINCE";
    private static final int TITLE = 1;
    private int SHORT_DESC=3;
    DbHandler database;
    private String untill="NoN";
    private String pagingToken="NoN";

    public Fetcher() {
        super("Fetcher");
    }



    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */

    public static void startActionCurrent(Context context) {
        Intent intent = new Intent(context, Fetcher.class);
        intent.setAction(ACTION_CURRENT);
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(Urls.Base)
                .build();
        if (graphApi==null)
            graphApi=restAdapter.create(Fb.class);
        if (database==null)
            database=new DbHandler(Fetcher.this);
        if (token.equals("NoN"))
            token=getSharedPreferences("Chari",MODE_PRIVATE).getString("token","NoN");
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startPrevious(Context context, String since) {
        Intent intent = new Intent(context, Fetcher.class);
        intent.setAction(ACTION_PREVIOUS);
        intent.putExtra(EXTRA_SINCE, since);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_CURRENT.equals(action)) {
                handleActionCurrent();
            } else if (ACTION_PREVIOUS.equals(action)) {
                final String since = intent.getStringExtra(EXTRA_SINCE);
                handleActionPrevious(since);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionCurrent() {

        look(token);
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionPrevious(String since) {

        if (!since.equals("-1"))
            graphApi.previous("id,message,full_picture,picture,from,link", "json", token, since, 1, new Callback<Graphfeed>() {
                @Override
                public void success(Graphfeed graphfeed, Response response) {
                    processFeeds(graphfeed);
                    Intent looked=new Intent(ACTION_PREVIOUS);
                    looked.putExtra("data",graphfeed);
                    sendBroadcast(looked);
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
    }
    void look(String token) {
        untill=getSharedPreferences("Chari",MODE_PRIVATE).getString("unitill","NoN");
        pagingToken=getSharedPreferences("Chari",MODE_PRIVATE).getString("pagingToken","NoN");
        if (!untill.equals("NoN"))
        graphApi.next("id,message,full_picture,picture,from,link","json",token,"25",untill,pagingToken,  new Callback<Graphfeed>() {
            @Override
            public void success(Graphfeed graphfeed, Response response) {
                processFeeds(graphfeed);
                String next=graphfeed.getPaging().getNext();
                String previous=graphfeed.getPaging().getPrevious();
                try {
                    Map<String, List<String>> nextParams=Boilerplate.getUrlParameters(next);
                    untill=String.valueOf(nextParams.get("untill"));
                    pagingToken=String.valueOf(nextParams.get("__paging_token"));
                    SharedPreferences.Editor ed=getSharedPreferences("Chari",MODE_PRIVATE).edit();
                    ed.putString("untill",untill);
                    ed.putString("pagingToken",pagingToken);
                    ed.apply();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Intent looked=new Intent(ACTION_CURRENT);
                looked.putExtra("data",graphfeed);
                sendBroadcast(looked);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        else
            graphApi.feed("id,message,full_picture,picture,from,link", token, new Callback<Graphfeed>() {
                @Override
                public void success(Graphfeed graphfeed, Response response) {
                    processFeeds(graphfeed);
                    String next=graphfeed.getPaging().getNext();
                    String previous=graphfeed.getPaging().getPrevious();
                    try {
                        Map<String, List<String>> nextParams=Boilerplate.getUrlParameters(next);
                        untill=String.valueOf(nextParams.get("untill"));
                        pagingToken=String.valueOf(nextParams.get("__paging_token"));
                        SharedPreferences.Editor ed=getSharedPreferences("Chari",MODE_PRIVATE).edit();
                        ed.putString("untill",untill);
                        ed.putString("pagingToken",pagingToken);
                        ed.apply();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Intent looked=new Intent(ACTION_CURRENT);
                    looked.putExtra("data",graphfeed);
                    sendBroadcast(looked);
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
    }


    private void processFeeds(Graphfeed graphfeed) {
        for (Feed feed : graphfeed.getData()) {
            try {
                ContentValues values=new ContentValues();
                Feeds feeds = new Feeds();
                feeds.setImageUrl(feed.getPicture());
                feeds.setFull_image(feed.getFull_picture());
                feeds.setFrom(feed.getFrom().getName());
                feeds.setLink(feed.getLink());

                String[] feedSplitted = feed.getMessage().split("\\r?\\n");
                feeds.setTitle(getReleavent(feedSplitted,TITLE));
                feeds.setDesc(getReleavent(feedSplitted,SHORT_DESC));
                values.put(Database.FEED_ROW_TITLE,feeds.getTitle());
                values.put(Database.FEED_ROW_FULLIMG,feed.getFull_picture());
                values.put(Database.FEED_ROW_DESC,feed.getMessage());
                values.put(Database.FEED_ROW_THUMB,feed.getPicture());
                values.put(Database.FEED_ROW_LINK,feed.getLink());
                values.put(Database.FEED_ROW_FROM,feed.getFrom().getName());
                values.put(Database.FEED_ROW_ID,feed.getId());
                database.Insert(values,Database.TAB_FEED);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getReleavent(String[] feedSplitted, int TyPE) {
        List<String> array=new ArrayList<String>();
        for (String sin: feedSplitted)
            array.add(sin);
        if (TyPE==SHORT_DESC)
        {
            array.remove(0);
        }
        for (String line:array)
        {
            if (!line.equals("")
                    &&!line.contains("=====")
                    &&!line.contains("xxxxx")
                    &&!line.contains("......")
                    &&!line.contains(",,,,,")
                    &&!line.contains("-----")
                    &&!line.contains("#####")
                    &&!line.contains("\\u2605\\u2605\\u2605\\u2605")
                    &&!line.contains("****")
                    &&!line.contains("\\u2606\\u2606\\u2606\\u2606"))
                return line;
        }
        return null;
    }
}
