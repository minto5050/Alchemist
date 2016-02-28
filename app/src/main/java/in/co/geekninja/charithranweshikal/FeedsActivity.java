package in.co.geekninja.charithranweshikal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.yalantis.taurus.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import in.co.geekninja.charithranweshikal.Adapters.FeedRecyclerAdapter;
import in.co.geekninja.charithranweshikal.Adapters.Feeds;
import in.co.geekninja.charithranweshikal.Models.Feed;
import in.co.geekninja.charithranweshikal.Models.Graphfeed;
import in.co.geekninja.charithranweshikal.Services.Fetcher;
import in.co.geekninja.charithranweshikal.Storage.DbHandler;
import in.co.geekninja.charithranweshikal.Storage.SharedPrefs;
import retrofit.RestAdapter;

/**
 * Created by PS on 2/26/2016.
 */
public class FeedsActivity extends FragmentActivity {
    private static final String TAG = "Chari";
    private static final int TITLE = 1;
    private static final int SHORT_DESC = 3;
    Fb graphApi;
    FeedRecyclerAdapter  adapter;
    List<Feeds> feedses;
    private PullToRefreshView mPullToRefreshView;
    private String since="-1";
    RecyclerView list_view;
    String token="NoN";

    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    LinearLayoutManager mLayoutManager;
    private SharedPreferences sp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feeds_activity);
        sp=SharedPrefs.getInstance(FeedsActivity.this);
        token=sp.getString(SharedPrefs.TOKEN,"NoN");
        since=sp.getString(SharedPrefs.SINCE,"NoN");
        list_view=(RecyclerView)findViewById(R.id.list_view);
        if (feedses==null)
            feedses=new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(this);
        list_view.setLayoutManager(mLayoutManager);
        list_view.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = list_view.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if (totalItemCount > previousTotal) {
                            loading = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if (!loading && (totalItemCount - visibleItemCount)
                            <= (firstVisibleItem + visibleThreshold)) {
                        // End has been reached

                        Log.i("Yaeye!", "end called");

                        if (!since.equals("NoN"))
                            Fetcher.startPrevious(FeedsActivity.this,since);


                    }


                }
            }
        });
        adapter=new FeedRecyclerAdapter(feedses,FeedsActivity.this);
        //adapter=new FeedsAdapter(FeedsActivity.this,R.layout.feeds_single_row,feedses);
        list_view.setAdapter(adapter);
        /*list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(FeedsActivity.this,ReadActivity.class);
                intent.putExtra("feed",(Feeds)view.getTag());
                startActivity(intent);
            }
        });*/
        Initialize();
        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        next();
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(Urls.Base)
                .build();
        graphApi = restAdapter.create(Fb.class);
        if (token.equals("NoN"))
        {
            finish();
            startActivity(new Intent(FeedsActivity.this,MainActivity.class));
        }
        else
        {
         Initialize();
            //look(token);
        }

    }

    private void next() {
        SharedPreferences sp = SharedPrefs.getInstance(FeedsActivity.this);
        String untill = sp.getString(SharedPrefs.UNTIL, "NoN");
        String pagingToken = sp.getString(SharedPrefs.PAGING_TOKEN, "NoN");
        Fetcher.startActionNext(FeedsActivity.this,untill,pagingToken);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcast, new IntentFilter(Fetcher.ACTION_CURRENT));
        registerReceiver(broadcast, new IntentFilter(Fetcher.ACTION_NEXT));
        registerReceiver(broadcast, new IntentFilter(Fetcher.ACTION_PREVIOUS));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcast);
    }

    void look(String token) {
        Fetcher.startActionCurrent(FeedsActivity.this);
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
    void Initialize()
    {
        DbHandler handler=new DbHandler(FeedsActivity.this);
        List<Feeds> feedse=handler.getFeeds();
        for (Feeds f:feedse)
        {
            adapter.addItem(f,FeedRecyclerAdapter.BOTTOM);
        }
        adapter.notifyDataSetChanged();

    }
    BroadcastReceiver broadcast= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Graphfeed graphfeed= (Graphfeed) intent.getSerializableExtra("data");
            for (Feed feed : graphfeed.getData()) {
                try {
                    Feeds feeds = new Feeds();
                    feeds.setImageUrl(feed.getPicture());
                    feeds.setFull_image(feed.getFull_picture());
                    feeds.setLink(feed.getLink());
                    feeds.setFrom(feed.getFrom().getName());
                    String[] feedSplitted = feed.getMessage().split("\\r?\\n");
                    feeds.setTitle(getReleavent(feedSplitted, TITLE));
                    feeds.setDesc(getReleavent(feedSplitted, SHORT_DESC));
                    switch (intent.getAction()) {
                        case Fetcher.ACTION_NEXT:
                            adapter.addItem(feeds, FeedRecyclerAdapter.TOP);

                            break;
                        case Fetcher.ACTION_CURRENT:
                            Initialize();
                            break;
                        case Fetcher.ACTION_PREVIOUS:
                            adapter.addItem(feeds, FeedRecyclerAdapter.BOTTOM);

                            break;
                    }
                }catch (Exception e){

                }
                adapter.notifyDataSetChanged();
                loading=true;
            }

        }
    };
}
