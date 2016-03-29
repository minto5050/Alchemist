package in.co.geekninja.charithranweshikal;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Arrays;

import cn.pedant.SweetAlert.SweetAlertDialog;
import in.co.geekninja.charithranweshikal.Misc.Boilerplate;
import in.co.geekninja.charithranweshikal.Services.Fetcher;
import in.co.geekninja.charithranweshikal.Storage.SharedPrefs;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreen extends Activity implements FacebookCallback<LoginResult> {
    private static final int REQUEST_WRITE_STORAGE = 112;
    private CallbackManager callbackManager;
    private Button loginButton;
    private GoogleApiClient client;
    SharedPreferences sharedPreferences;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        boolean hasPermission = (ContextCompat.checkSelfPermission(SplashScreen.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

        if (sweetAlertDialog==null)
            sweetAlertDialog=new SweetAlertDialog(SplashScreen.this,SweetAlertDialog.NORMAL_TYPE);
        sharedPreferences= SharedPrefs.getInstance(SplashScreen.this);
        token=sharedPreferences.getString(SharedPrefs.TOKEN,"NoN");
        TextView tv=(TextView)findViewById(R.id.fullscreen_content);
        tv.setTypeface(Boilerplate.getFontPrimary(SplashScreen.this));
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        loginButton = (Button) findViewById(R.id.connect_facebook);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(SplashScreen.this, Arrays.asList("user_managed_groups"));
            }
        });
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        LoginManager.getInstance().registerCallback(callbackManager, this);
        loginButton.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if (!token.equals("NoN"))
                {
                    Intent intent = new Intent(SplashScreen.this, FeedsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    Fetcher.startActionLimit(800,SplashScreen.this);


                }
                else
                {
                 loginButton.setVisibility(View.VISIBLE);
                }


            }
        }, 3000);
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://in.co.geekninja.charithranweshikal/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://in.co.geekninja.charithranweshikal/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        String token=loginResult.getAccessToken().getToken();
        SharedPrefs.getInstance(SplashScreen.this).edit().putString(SharedPrefs.TOKEN,token).apply();

        finish();
        startActivity(new Intent(SplashScreen.this,FeedsActivity.class));
    }
    SweetAlertDialog sweetAlertDialog;
    @Override
    public void onCancel() {

        sweetAlertDialog.setContentText("Inorder to use the application,You need to connect with facebook");
        sweetAlertDialog.setTitleText("You cancelled It!");
        sweetAlertDialog.show();
    }

    @Override
    public void onError(FacebookException error) {

        sweetAlertDialog.setContentText(error.getMessage());
        sweetAlertDialog.setTitleText("Error Occured !");
        sweetAlertDialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        //manage login result
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
