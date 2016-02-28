package in.co.geekninja.charithranweshikal;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import in.co.geekninja.charithranweshikal.Adapters.Feeds;
import in.co.geekninja.charithranweshikal.Misc.Boilerplate;
import in.co.geekninja.charithranweshikal.Misc.PicassoCache;

public class ReadActivity extends Activity {
    ImageView imageView;
    TextView content_text,author,original;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        imageView=(ImageView)findViewById(R.id.img_read);
        content_text=(TextView)findViewById(R.id.read_content);
        author=(TextView)findViewById(R.id.author_name);
        content_text.setTypeface(Boilerplate.getFontPrimary());
        author.setTypeface(Boilerplate.getFontSecondary());
        final Feeds feeds= (Feeds) getIntent().getExtras().getSerializable("feed");
        PicassoCache.getPicassoInstance(ReadActivity.this).load(feeds.getFull_image()).into(imageView);
        content_text.setText(feeds.getDesc());
        author.setText(feeds.getFrom());
        original=(TextView)findViewById(R.id.original);
        original.setTypeface(Boilerplate.getFontSecondary());
        original.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(feeds.getLink()));
                startActivity(i);
            }
        });
    }
}
