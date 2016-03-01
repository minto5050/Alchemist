package in.co.geekninja.charithranweshikal.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.co.geekninja.charithranweshikal.Misc.Boilerplate;
import in.co.geekninja.charithranweshikal.Misc.PicassoCache;
import in.co.geekninja.charithranweshikal.R;
import in.co.geekninja.charithranweshikal.ReadActivity;

/**
 * Created by PS on 2/27/2016.
 */
public class FeedRecyclerAdapter extends RecyclerView.Adapter<FeedRecyclerAdapter.FeedViewHolder> {
    public static final int TOP = 2;
    public static final int BOTTOM = 1;
    private final List<Feeds> feeds;
    private final Context context;

    public FeedRecyclerAdapter(List<Feeds> feeds, Context context) {
        this.feeds = feeds;
        this.context=context;
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feeds_single_row, parent, false);

        FeedViewHolder vh = new FeedViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        Feeds item =feeds.get(position);
        try {
            holder.itemView.setTag(item);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,ReadActivity.class);
                    intent.putExtra("feed",(Feeds)v.getTag());
                    context.startActivity(intent);
                }
            });
            holder.title.setText(Boilerplate.getReleavent(item.getDesc(),Boilerplate.TITLE));
            holder.description.setText(Boilerplate.getReleavent(item.getDesc(),Boilerplate.SHORT_DESC));
            PicassoCache.getPicassoInstance(context).load(item.getImageUrl()).into(holder.icon);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addItem(Feeds feed,int position)
    {
        try {
            for (Feeds mi:feeds) {
                if (mi.getTitle().equals(feed.getTitle())){
                    Log.e("..","exists");
                    return;
                }
            }
            if (position==TOP){
                feeds.add(0,feed);
            }
            else if (position==BOTTOM)
            {
                feeds.add(feed);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public int getItemCount() {
        return feeds.size();
    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder {

        TextView title, description;
        CircleImageView icon;

        /**
         * Constructs a new instance of {@code Object}.
         */
        public FeedViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.txt_title_feeds);
            title.setTypeface(Boilerplate.getFontSecondary(v.getContext()));
            description = (TextView) v.findViewById(R.id.txt_dec_feeds);
            description.setTypeface(Boilerplate.getFontPrimary(v.getContext()));
            icon = (CircleImageView) v.findViewById(R.id.img_image_feeds);
        }
    }
}
