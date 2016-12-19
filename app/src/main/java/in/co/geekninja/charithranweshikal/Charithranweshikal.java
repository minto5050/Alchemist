package in.co.geekninja.charithranweshikal;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.io.File;

import in.co.geekninja.charithranweshikal.Misc.Boilerplate;
import in.co.geekninja.charithranweshikal.Storage.Database;
import in.co.geekninja.charithranweshikal.Storage.SharedPrefs;

/**
 * Created by PS on 2/24/2016.
 */
public class Charithranweshikal extends Application {
    /**
     * The Shared preferences.
     */
    SharedPreferences sharedPreferences;
    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences= SharedPrefs.getInstance(getApplicationContext());
        File outFile = new File(getFilesDir()+"/fonts/");
        if (!outFile.exists())
        {

            if (outFile.mkdirs()) {
                Boilerplate.copyAssets(Charithranweshikal.this);
            }
        }
        new Boilerplate().exportDatabse(Database.name, getApplicationContext());
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
