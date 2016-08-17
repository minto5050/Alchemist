package in.co.geekninja.charithranweshikal.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Transformation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import in.co.geekninja.charithranweshikal.Misc.Boilerplate;
import in.co.geekninja.charithranweshikal.Misc.PicassoCache;
import in.co.geekninja.charithranweshikal.R;
import in.co.geekninja.charithranweshikal.ReadActivity;
import in.co.geekninja.charithranweshikal.Storage.DbHandler;
import in.co.geekninja.charithranweshikal.Storage.SharedPrefs;

/**
 * Created by PS on 2/27/2016.
 */
public class FeedRecyclerAdapter extends RecyclerView.Adapter<FeedRecyclerAdapter.FeedViewHolder> implements Filterable {
    /**
     * The constant TOP.
     */
    public static final int TOP = 2;
    /**
     * The constant BOTTOM.
     */
    public static final int BOTTOM = 1;
    private final List<Feeds> feeds;
    private final Context context;
    private final String token;
    private FeedsFilter feedsFilter;

    /**
     * Instantiates a new Feed recycler adapter.
     *
     * @param feeds   the feeds
     * @param context the context
     */
    public FeedRecyclerAdapter(List<Feeds> feeds, Context context) {
        this.feeds = feeds;
        this.context = context;
        this.token  = SharedPrefs.getInstance(context).getString(SharedPrefs.TOKEN,"NoN");
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
       /* View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feednew_single, parent, false);*/
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_single, parent, false);
        FeedViewHolder vh = new FeedViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final FeedViewHolder holder, int position) {
        final Feeds item = feeds.get(position);
        try {
            holder.itemView.setTag(item);
            holder.fav.setTag(item.getId());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ReadActivity.class);
                    intent.putExtra("feed", (Feeds) v.getTag());
                    context.startActivity(intent);
                }
            });
            holder.fav.setOnLikeListener(new OnLikeListener() {
                    @Override
                    public void liked(LikeButton likeButton) {
                        String feedRowId = (String) likeButton.getTag();
                        DbHandler.getInstance(context).setLiked(feedRowId,true);
                        item.setFavorite(true);
                    }

                    @Override
                    public void unLiked(LikeButton likeButton) {
                        String feedRowId = (String) likeButton.getTag();
                        DbHandler.getInstance(context).setLiked(feedRowId,false);
                        item.setFavorite(false);
                    }
                });
            holder.title.setText(Boilerplate.getReleavent(item.getDesc(), Boilerplate.TITLE));
            holder.author.setText(item.getFrom());
            holder.fav.setLiked(item.isFavorite());
            PicassoCache.getPicassoInstance(context).load(item.getUser_image()+token).into(holder.icon);
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
                if (item.getImageUrl()!=null&&!item.getImageUrl().equals("")){
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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        try {
            Date date = format.parse(item.getCreatedDate());
            long utcTimestamp = date.getTime();
            long gmtOffset = TimeZone.getDefault().getRawOffset();
            String s = (String) DateUtils.getRelativeTimeSpanString(utcTimestamp, gmtOffset, DateUtils.DAY_IN_MILLIS);
            holder.time.setText(s);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
            holder.time.setText("");
        }
    }

    /**
     * Add item.
     *
     * @param feed     the feed
     * @param position the position
     */
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
            Collections.sort(feeds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return feeds.size();
    }

    @Override
    public Filter getFilter() {
        if(feedsFilter == null)
            feedsFilter = new FeedsFilter(this, feeds);
        return feedsFilter;
    }

    /**
     * The type Feed view holder.
     */
    public static class FeedViewHolder extends RecyclerView.ViewHolder {

        /**
         * The Title.
         */
        TextView title, /**
         * The Description.
         */
        description, /**
         * The Author.
         */
        author;
        /**
         * The Time.
         */
        TextView time;
        /**
         * The Icon.
         */
        CircleImageView icon;
        /**
         * The Bg.
         */
        ImageView bg;
        /**
         * The Fav.
         */
        LikeButton fav;

        /**
         * Constructs a new instance of {@code Object}.
         *
         * @param v the v
         */
        public FeedViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.card_title);
            title.setTypeface(Boilerplate.getFontSecondary(v.getContext()));
            author = (TextView) v.findViewById(R.id.creater_id);
            author.setTypeface(Boilerplate.getFontPrimary(v.getContext()));
            bg = (ImageView) v.findViewById(R.id.card_image);
            icon = (CircleImageView) v.findViewById(R.id.from_img);
            fav = (LikeButton)v.findViewById(R.id.img_feed);
            time =(TextView)v.findViewById(R.id.hour_tv);
        }
    }
    private static class FeedsFilter extends Filter {

        private final FeedRecyclerAdapter adapter;

        private final List<Feeds> originalList;

        private final List<Feeds> filteredList;

        private FeedsFilter(FeedRecyclerAdapter adapter, List<Feeds> originalList) {
            super();
            this.adapter = adapter;
            this.originalList = new LinkedList<>(originalList);
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();

                for (final Feeds feed : originalList) {
                    if (feed.getTitle_transliterated().contains(filterPattern)||feed.getTitle().contains(filterPattern)) {
                        filteredList.add(feed);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.feeds.clear();
            adapter.feeds.addAll((ArrayList<Feeds>) results.values);
            adapter.notifyDataSetChanged();
        }
    }
}
