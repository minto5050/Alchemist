package in.co.geekninja.charithranweshikal;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import cn.pedant.SweetAlert.SweetAlertDialog;
import in.co.geekninja.charithranweshikal.Adapters.Feeds;
import in.co.geekninja.charithranweshikal.Misc.ActionCallbackText;
import in.co.geekninja.charithranweshikal.Misc.Boilerplate;
import in.co.geekninja.charithranweshikal.Misc.PicassoCache;

public class ReadActivity extends Activity {
    ImageView imageView;
    EditText content_text;
    TextView author, original;
    SweetAlertDialog alert;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        imageView = (ImageView) findViewById(R.id.img_read);
        content_text = (EditText) findViewById(R.id.read_content);
        content_text.setCustomSelectionActionModeCallback(new ActionCallbackText(content_text, ReadActivity.this));
        author = (TextView) findViewById(R.id.author_name);
        content_text.setTypeface(Boilerplate.getFontPrimary(ReadActivity.this));
        alert = new SweetAlertDialog(ReadActivity.this, SweetAlertDialog.ERROR_TYPE);
        author.setTypeface(Boilerplate.getFontSecondary(ReadActivity.this));
        final Feeds feeds = (Feeds) getIntent().getExtras().getSerializable("feed");
        PicassoCache.getPicassoInstance(ReadActivity.this).load(feeds.getFull_image()).into(imageView);
        content_text.setText(feeds.getDesc());
        author.setText(feeds.getFrom());
        original = (TextView) findViewById(R.id.original);
        original.setTypeface(Boilerplate.getFontSecondary(ReadActivity.this));
        original.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(feeds.getLink()));
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                    alert.setTitleText("Sorry!");
                    alert.setContentText("Original post is unavailable");
                    alert.show();
                }
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Read Page", // TODO: Define a title for the content shown.
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
                "Read Page", // TODO: Define a title for the content shown.
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
}
