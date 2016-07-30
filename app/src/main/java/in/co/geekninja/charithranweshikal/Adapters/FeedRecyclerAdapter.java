package in.co.geekninja.charithranweshikal.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Transformation;

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
        this.context = context;
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
       /* View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feeds_single_row, parent, false);*/
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feednew_single, parent, false);
        FeedViewHolder vh = new FeedViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final FeedViewHolder holder, int position) {
        final Feeds item = feeds.get(position);
        try {
            holder.itemView.setTag(item);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ReadActivity.class);
                    intent.putExtra("feed", (Feeds) v.getTag());
                    context.startActivity(intent);
                }
            });
            holder.title.setText(Boilerplate.getReleavent(item.getDesc(), Boilerplate.TITLE));
            /*holder.description.setText(Boilerplate.getReleavent(item.getDesc(),Boilerplate.SHORT_DESC));
            PicassoCache.getPicassoInstance(context).load(item.getImageUrl()).into(holder.icon);*/
            holder.author.setText("by " + item.getFrom());
            Transformation transformation = new Transformation() {
                @Override
                public Bitmap transform(Bitmap source) {
                    Bitmap blurred = Boilerplate.fastblur(source, 0.2f, 5);
                    if (blurred!=source)
                    {
                        source.recycle();
                    }
                    return blurred;
                }

                @Override
                public String key() {
                    return "blur()";
                }
            };

            try {
                if (!item.getImageUrl().equals("")){
                    Log.e("..","not Null Image.."+item.getImageUrl());
                    PicassoCache.getPicassoInstance(context)
                        .load(item.getImageUrl()) // thumbnail url goes here
                        .transform(transformation).into(holder.bg, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        PicassoCache.getPicassoInstance(context)
                                .load(item.getFull_image()) // image url goes here
                                .placeholder(holder.bg.getDrawable())
                                .into(holder.bg);
                    }

                    @Override
                    public void onError() {

                    }
                });
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addItem(Feeds feed, int position) {
        try {
            for (Feeds mi : feeds) {
                if (mi.getTitle().equals(feed.getTitle())) {
                    Log.e("..", "exists");
                    return;
                }
            }
            if (position == TOP) {
                feeds.add(0, feed);
            } else if (position == BOTTOM) {
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

        TextView title, description, author;
        CircleImageView icon;
        ImageView bg;

        /**
         * Constructs a new instance of {@code Object}.
         */
        public FeedViewHolder(View v) {
            super(v);
            /*title = (TextView) v.findViewById(R.id.txt_title_feeds);
            title.setTypeface(Boilerplate.getFontSecondary(v.getContext()));
            description = (TextView) v.findViewById(R.id.txt_dec_feeds);
            description.setTypeface(Boilerplate.getFontPrimary(v.getContext()));
            icon = (CircleImageView) v.findViewById(R.id.img_image_feeds);*/
            title = (TextView) v.findViewById(R.id.txt_title);
            title.setTypeface(Boilerplate.getFontSecondary(v.getContext()));
            author = (TextView) v.findViewById(R.id.txt_author);
            author.setTypeface(Boilerplate.getFontPrimary(v.getContext()));
            bg = (ImageView) v.findViewById(R.id.img_bg);

        }
    }
}
