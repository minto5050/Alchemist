package in.co.geekninja.charithranweshikal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import in.co.geekninja.charithranweshikal.Services.Fetcher;
import in.co.geekninja.charithranweshikal.Storage.SharedPrefs;

/**
 * Created by PS on 8/27/2016.
 */
public class InternetListener extends BroadcastReceiver {
    public   boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if (isNetworkAvailable(context)) {
            SharedPreferences sp = SharedPrefs.getInstance(context);
            String until = sp.getString(SharedPrefs.UNTIL, "NoN");
            String pagingToken = sp.getString(SharedPrefs.PAGING_TOKEN, "NoN");
            Fetcher.startActionNext(context,until,pagingToken);
        }
    }
}
