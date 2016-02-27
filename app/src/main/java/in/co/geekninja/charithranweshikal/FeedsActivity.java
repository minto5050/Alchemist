package in.co.geekninja.charithranweshikal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;

import com.paging.listview.PagingListView;
import com.yalantis.taurus.PullToRefreshView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import in.co.geekninja.charithranweshikal.Adapters.Feeds;
import in.co.geekninja.charithranweshikal.Adapters.FeedsAdapter;
import in.co.geekninja.charithranweshikal.Services.Fetcher;
import in.co.geekninja.charithranweshikal.Storage.DbHandler;
import retrofit.RestAdapter;

/**
 * Created by PS on 2/26/2016.
 */
public class FeedsActivity extends FragmentActivity {
    private static final String TAG = "Chari";
    private static final int TITLE = 1;
    private static final int SHORT_DESC = 3;
    Fb graphApi;
    FeedsAdapter adapter;
    List<Feeds> feedses;
    private PullToRefreshView mPullToRefreshView;
    private String since="-1";
    PagingListView list_view;
    String token="NoN";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feeds_activity);

        token=getSharedPreferences("Chari",MODE_PRIVATE).getString("token","NoN");
        list_view=(PagingListView)findViewById(R.id.list_view);
        if (feedses==null)
            feedses=new ArrayList<>();

        adapter=new FeedsAdapter(FeedsActivity.this,R.layout.feeds_single_row,feedses);
        list_view.setAdapter(adapter);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(FeedsActivity.this,ReadActivity.class);
                intent.putExtra("feed",(Feeds)view.getTag());
                startActivity(intent);
            }
        });
        Initialize();
        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        look(token);
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

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcast, new IntentFilter(Fetcher.ACTION_CURRENT));
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
        List<Feeds> fx =new ArrayList<>();
        LinkedHashSet<Feeds> hashSet = new LinkedHashSet<>(feedses);

        for (Feeds f:feedse)
        {
            if (hashSet.add(f))
                feedses.add(f);
        }
        /*for (Feeds f : feedse){
            if (fx.size()>0) {
             for (Feeds ff: fx)
                if (!f.getTitle().equals(ff.getTitle()))
                    feedses.add(f);
            }else {

            }
        }*/
        if (feedse.size()<=0){
            look(token);
        }
        adapter.notifyDataSetChanged();

    }
    BroadcastReceiver broadcast= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            /*Graphfeed graphfeed= (Graphfeed) intent.getSerializableExtra("data");
            adapter.notifyDataSetChanged();
            for (Feed feed : graphfeed.getData()) {
                try {
                    Feeds feeds = new Feeds();
                    feeds.setImageUrl(feed.getPicture());
                    String[] feedSplitted = feed.getMessage().split("\\r?\\n");
                    feeds.setTitle(getReleavent(feedSplitted,TITLE));
                    feeds.setDesc(getReleavent(feedSplitted,SHORT_DESC));
                    feedses.add(feeds);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            adapter.notifyDataSetChanged();
            try {
                Map<String, List<String>> params = Boilerplate.getUrlParameters(graphfeed.getPaging().getPrevious());
                since = String.valueOf(params.get("since"));
                if (since.equals("-1"))
                    list_view.setHasMoreItems(false);
                else
                    list_view.setHasMoreItems(true);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                list_view.setHasMoreItems(false);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                list_view.setHasMoreItems(false);
            }*/
            Initialize();
        }
    };
}
