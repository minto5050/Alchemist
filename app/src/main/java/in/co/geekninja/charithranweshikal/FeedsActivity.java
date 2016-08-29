package in.co.geekninja.charithranweshikal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.yalantis.taurus.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import in.co.geekninja.charithranweshikal.Adapters.EndlessRecyclerOnScrollListener;
import in.co.geekninja.charithranweshikal.Adapters.FeedRecyclerAdapter;
import in.co.geekninja.charithranweshikal.Adapters.Feeds;
import in.co.geekninja.charithranweshikal.Misc.Boilerplate;
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

    Fb graphApi;
    FeedRecyclerAdapter adapter;
    List<Feeds> feedses;
    private PullToRefreshView mPullToRefreshView;
    private String since = "-1";
    RecyclerView list_view;
    String token = "NoN";
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem;
    LinearLayoutManager mLayoutManager;
    private SharedPreferences sp;
    private boolean shown_disclaimer = false;
    SweetAlertDialog disclaimer;
    private AppEventsLogger logger;
    SearchView searchView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feeds_activity);
        searchView = (SearchView) findViewById(R.id.search_bar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        sp = SharedPrefs.getInstance(FeedsActivity.this);
        if (getIntent().getExtras().containsKey(SharedPrefs.TOKEN))
            token = getIntent().getStringExtra(SharedPrefs.TOKEN);
        else
            token = sp.getString(SharedPrefs.TOKEN, "NoN");
        since = sp.getString(SharedPrefs.SINCE, "NoN");
        try {
            logger = AppEventsLogger.newLogger(this);
        } catch (Exception e) {
            FacebookSdk.sdkInitialize(FeedsActivity.this);
            logger = AppEventsLogger.newLogger(this);
        }
        disclaimer = new SweetAlertDialog(FeedsActivity.this, SweetAlertDialog.CUSTOM_LAYOUT_TYPE);
        shown_disclaimer = sp.getBoolean(SharedPrefs.DISCLAIMER, false);
        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);

        if (!shown_disclaimer) {

            mPullToRefreshView.setRefreshing(true);
            look(token);
            TextView disc = new TextView(FeedsActivity.this);
            disc.setTypeface(Boilerplate.getFontPrimary(FeedsActivity.this));
            disc.setText(getString(R.string.disclaimer));
            disclaimer.setCustomView(disc);
            disclaimer.show();
            disclaimer.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    sp.edit().putBoolean(SharedPrefs.DISCLAIMER, true).apply();
                }
            });

        }
        list_view = (RecyclerView) findViewById(R.id.list_view);

        if (feedses == null)
            feedses = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(this);
        list_view.setLayoutManager(mLayoutManager);
        list_view.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if (!since.equals("NoN"))
                    Fetcher.startPrevious(FeedsActivity.this, since);
            }
        });

        adapter = new FeedRecyclerAdapter(feedses, FeedsActivity.this);
        list_view.setAdapter(adapter);
        Initialize();


        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                next();
                logger.logEvent("PulledTheList");
            }
        });

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(Urls.getBase())
                .build();
        graphApi = restAdapter.create(Fb.class);
        if (token.equals("NoN")) {
            finish();
            startActivity(new Intent(FeedsActivity.this, MainActivity.class));
        } else {
            Initialize();
            //look(token);
        }

    }

    private void next() {
        SharedPreferences sp = SharedPrefs.getInstance(FeedsActivity.this);
        String until = sp.getString(SharedPrefs.UNTIL, "NoN");
        String pagingToken = sp.getString(SharedPrefs.PAGING_TOKEN, "NoN");
        Fetcher.startActionNext(FeedsActivity.this, until, pagingToken);
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
        mPullToRefreshView.setRefreshing(false);
    }

    void look(String token) {
        Fetcher.startActionCurrent(FeedsActivity.this);
    }


    void Initialize() {
        DbHandler handler = DbHandler.getInstance(FeedsActivity.this);
        List<Feeds> feedse = handler.getFeeds();
        for (Feeds f : feedse) {

            adapter.addItem(f, FeedRecyclerAdapter.BOTTOM);
        }
        adapter.Sort();
        //adapter.notifyDataSetChanged();

    }

    BroadcastReceiver broadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mPullToRefreshView.setRefreshing(false);
            if (disclaimer.isShowing()) {
                disclaimer.dismiss();
                Toast.makeText(FeedsActivity.this, R.string.scroll_down, Toast.LENGTH_SHORT).show();
            }
            Graphfeed graphfeed = (Graphfeed) intent.getSerializableExtra("data");

            try {
                for (Feed feed : graphfeed.getData()) {
                    try {
                        Feeds feeds = new Feeds();
                        feeds.setImageUrl(feed.getPicture());
                        feeds.setFull_image(feed.getFull_picture());
                        feeds.setLink(feed.getLink());
                        feeds.setFrom(feed.getFrom().getName());
                        feeds.setDesc(feed.getMessage());
                        feeds.setCreatedDate(feed.getCreated_time());
                        feeds.setTitle(Boilerplate.getReleavent(feed.getMessage(), Boilerplate.TITLE));
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }



                    //  loading=true;
                }
                adapter.Sort();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };
}
