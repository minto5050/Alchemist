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
 * This broadcast listener listens when the device is connected to the internet or disconnected from
 * To download the new articles and save them locally
 * Created by PS on 8/27/2016.
 */
public class InternetListener extends BroadcastReceiver {
    /**
     * The method for checking if the internet connection is available or not.
     * @param context The context
     * @return <code>true</code> if the network is available and <code>false</code> otherwise
     */
    public   boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Upon receiving the broadcast and if the network is available:-
     * <br>The app will start to download 400 articles from the last paging token</br>
     * @param context The context
     * @param intent The data sent for the broadcast receivers
     */
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
