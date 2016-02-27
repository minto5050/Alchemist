package in.co.geekninja.charithranweshikal;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Environment;

import java.io.File;

import in.co.geekninja.charithranweshikal.Misc.Boilerplate;

/**
 * Created by PS on 2/24/2016.
 */
public class Charithranweshikal extends Application {
    SharedPreferences sharedPreferences;
    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences=getSharedPreferences("Chari",MODE_PRIVATE);
        File outFile = new File(Environment.getExternalStorageDirectory()+"/.Charithranweshikal/fonts/");
        if (!outFile.exists())
        {
            outFile.mkdirs();
            Boilerplate.copyAssets(Charithranweshikal.this);
        }
        if (!sharedPreferences.getString("token","NoN").equals("NoN"))
        {
            Intent intent = new Intent(Charithranweshikal.this, FeedsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //startActivity(intent);
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
