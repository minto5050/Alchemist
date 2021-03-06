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
import in.co.geekninja.charithranweshikal.Storage.SharedPrefs;
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

    public static final String ACTION_NEXT = "in.co.geekninja.charithranweshikal.Services.action.NEXT";
    private static final String EXTRA_PAGING_TOKEN = "PagingToken";
    private static final String EXTRA_UNTILL = "Untill";
    private static final String ACTION_LIMIT = "in.co.geekninja.charithranweshikal.Services.action.LIMIT";
    public static final String EXTRA_LIMIT = "in.co.geekninja.charithranweshikal.Services.extra.LIMIT";
    Fb graphApi;
    public static final String ACTION_CURRENT = "in.co.geekninja.charithranweshikal.Services.action.CURRENT";
    public static final String ACTION_PREVIOUS = "in.co.geekninja.charithranweshikal.Services.action.PREVIOUS";
    String token="NoN";
    private static final String EXTRA_SINCE = "in.co.geekninja.charithranweshikal.Services.extra.SINCE";
    private static final int TITLE = 0;
    private int SHORT_DESC=1;
    DbHandler database;


    public Fetcher() {
        super("Fetcher");
    }

    SharedPreferences sp;

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
                .setEndpoint(Urls.getBase())
                .build();
        if (sp==null)
            sp=SharedPrefs.getInstance(Fetcher.this);
        if (graphApi==null)
            graphApi=restAdapter.create(Fb.class);
        if (database==null)
            database=DbHandler.getInstance(Fetcher.this);
        if (token.equals("NoN"))
            token= sp.getString(SharedPrefs.TOKEN,"NoN");

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

        if (token.equals("NoN"))
            token=sp.getString(SharedPrefs.TOKEN,"NoN");
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_CURRENT.equals(action)) {
                handleActionCurrent();
            } else if (ACTION_PREVIOUS.equals(action)) {
                 String since = sp.getString(SharedPrefs.SINCE,"NoN");
                handleActionPrevious(since);
            }
            else if (ACTION_NEXT.equals(action))
            {
                final String un=sp.getString(SharedPrefs.UNTIL,"NoN");
                final String pt=sp.getString(SharedPrefs.PAGING_TOKEN,"NoN");
                handleActionNext(un,pt);
            }
            else if (ACTION_LIMIT.equals(action)){
                int limit=intent.getIntExtra(EXTRA_LIMIT,20);
                handleActionLimit(limit);
            }
        }
    }

    private void handleActionNext(String un, String pt) {
        graphApi.next("id,message,full_picture,picture,from,link,created_time","json",token,"25",un,pt,"U",  new Callback<Graphfeed>() {
            @Override
            public void success(Graphfeed graphfeed, Response response) {
                processFeeds(graphfeed);
                Intent looked=new Intent(ACTION_NEXT);
                looked.putExtra("data",graphfeed);
                sendBroadcast(looked);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionCurrent() {

        graphApi.feed("id,message,full_picture,picture,from,link,created_time", token,"U", new Callback<Graphfeed>() {
            @Override
            public void success(Graphfeed graphfeed, Response response) {
                processFeeds(graphfeed);
                Intent looked=new Intent(ACTION_CURRENT);
                looked.putExtra("data",graphfeed);
                sendBroadcast(looked);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
    private void handleActionLimit(int limit) {

        graphApi.feedWithLimit("id,message,full_picture,picture,from,link,created_time",limit, token,"U", new Callback<Graphfeed>() {
            @Override
            public void success(Graphfeed graphfeed, Response response) {
                processFeeds(graphfeed);
                if (graphfeed.getData().size()>10) {
                   // List<Feed> short_list = graphfeed.getData().subList(0, 10);
                    graphfeed.setData(new ArrayList<>(graphfeed.getData().subList(0, 10)));
                }
                Intent looked=new Intent(ACTION_PREVIOUS);
                looked.putExtra("data",graphfeed);
                sendBroadcast(looked);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     * @param since
     */
    private void handleActionPrevious(String since) {

            graphApi.previous("id,message,full_picture,picture,from,link,created_time", "json", token, since, 1,"U", new Callback<Graphfeed>() {
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


    private void processFeeds(Graphfeed graphfeed) {
       Map<String, List<String>> nextParams= null;
       if (graphfeed.getPaging()!=null) {
           try {
               nextParams = Boilerplate.getUrlParameters(graphfeed.getPaging().getNext());
           } catch (UnsupportedEncodingException e) {
               e.printStackTrace();
           }
           Map<String, List<String>> prevParams = null;
           try {
               prevParams = Boilerplate.getUrlParameters(graphfeed.getPaging().getPrevious());
           } catch (UnsupportedEncodingException e) {
               e.printStackTrace();
           }
           String untill = nextParams.get("until").get(0);
           String since = prevParams.get("since").get(0);
           String pagingToken = nextParams.get("__paging_token").get(0);
           SharedPreferences.Editor ed = SharedPrefs.getInstance(Fetcher.this).edit();
           ed.putString(SharedPrefs.UNTIL, untill);
           ed.putString(SharedPrefs.PAGING_TOKEN, pagingToken);
           ed.putString(SharedPrefs.SINCE, since);
           ed.apply();
       }
        if (graphfeed.getData().size()>0) {
            for (Feed feed : graphfeed.getData()) {
                if(feed.getMessage()!=null){
                try {
                    ContentValues values = new ContentValues();
                    Feeds feeds = new Feeds();
                    feeds.setImageUrl(feed.getPicture());
                    feeds.setFull_image(feed.getFull_picture());
                    feeds.setFrom(feed.getFrom().getName());
                    feeds.setLink(feed.getLink());

                    String[] feedSplitted = new String[0];
                    try {
                        feedSplitted = feed.getMessage().split("\\r?\\n");
                    } catch (Exception e) {
                        feedSplitted = feed.getMessage().split("\n|\\.(?!\\d)|(?<!\\d)\\.");
                        e.printStackTrace();
                    }
                    feeds.setTitle(Boilerplate.getReleavent(feedSplitted, TITLE));
                    feeds.setDesc(Boilerplate.getReleavent(feedSplitted, SHORT_DESC));
                    values.put(Database.FEED_ROW_TITLE, feeds.getTitle());
                    values.put(Database.FEED_ROW_FULLIMG, feed.getFull_picture());
                    values.put(Database.FEED_ROW_DESC, feed.getMessage());
                    values.put(Database.FEED_ROW_THUMB, feed.getPicture());
                    values.put(Database.FEED_ROW_LINK, feed.getLink());
                    values.put(Database.FEED_ROW_FROM, feed.getFrom().getName());
                    values.put(Database.FEED_ROW_ID, feed.getId());
                    values.put(Database.FEED_ROW_CREATED_AT,feed.getCreated_time());
                    values.put(Database.FEED_ROW_USER_ID,feed.getFrom().getId());
                    database.Insert(values, Database.TAB_FEED);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                }
            }
        }
    }


    public static void startActionNext(Context context, String untill, String pagingToken) {
        Intent intent = new Intent(context, Fetcher.class);
        intent.setAction(ACTION_NEXT);
        intent.putExtra(EXTRA_UNTILL,untill);
        intent.putExtra(EXTRA_PAGING_TOKEN,pagingToken);
        context.startService(intent);
    }

    public static void startActionLimit(int i,Context context) {
        Intent intent = new Intent(context, Fetcher.class);
        intent.setAction(ACTION_LIMIT);
        intent.putExtra(EXTRA_LIMIT,i);
        context.startService(intent);
    }
}
