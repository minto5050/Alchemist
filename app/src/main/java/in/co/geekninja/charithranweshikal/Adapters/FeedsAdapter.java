package in.co.geekninja.charithranweshikal.Adapters;

/**
 * Created by PS on 2/26/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.co.geekninja.charithranweshikal.Misc.Boilerplate;
import in.co.geekninja.charithranweshikal.Misc.PicassoCache;
import in.co.geekninja.charithranweshikal.R;


/**
 * Created by PS on 7/14/2015.
 */
public class FeedsAdapter extends ArrayAdapter<Feeds> {
    private final int layout;
    private final Context context;
    private final List<Feeds> items;

    public FeedsAdapter(Context context, int resource, List<Feeds> objects) {
        super(context, resource, objects);
        this.context = context;
        this.items = objects;
        this.layout = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layout, null);
        TextView title = (TextView) v.findViewById(R.id.txt_title_feeds);
        TextView desc = (TextView) v.findViewById(R.id.txt_dec_feeds);
        desc.setTypeface(Boilerplate.getFontSecondary(context));
        title.setTypeface(Boilerplate.getFontPrimary(context));
        final CircleImageView image = (CircleImageView) v.findViewById(R.id.img_image_feeds);
        final Feeds feed = items.get(position);
        title.setText(feed.getTitle());
        desc.setText(feed.getDesc());
        PicassoCache.getPicassoInstance(context).load(feed.getImageUrl()).into(image);
        v.setTag(feed);
        return v;
    }

    @Override
    public Feeds getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }
}

