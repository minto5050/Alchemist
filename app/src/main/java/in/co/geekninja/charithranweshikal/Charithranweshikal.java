package in.co.geekninja.charithranweshikal;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Environment;

import java.io.File;

import in.co.geekninja.charithranweshikal.Misc.Boilerplate;
import in.co.geekninja.charithranweshikal.Storage.SharedPrefs;

/**
 * Created by PS on 2/24/2016.
 */
public class Charithranweshikal extends Application {
    SharedPreferences sharedPreferences;
    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences= SharedPrefs.getInstance(getApplicationContext());
        File outFile = new File(Environment.getExternalStorageDirectory()+"/.Charithranweshikal/fonts/");
        if (!outFile.exists())
        {
            outFile.mkdirs();
            Boilerplate.copyAssets(Charithranweshikal.this);
        }

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

}
