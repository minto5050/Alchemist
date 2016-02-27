package in.co.geekninja.charithranweshikal.Adapters;

import android.widget.ListAdapter;

import com.commonsware.cwac.endless.EndlessAdapter;

/**
 * Created by PS on 2/26/2016.
 */
public class Endless extends EndlessAdapter {
    public Endless(ListAdapter wrapped) {
        super(wrapped);
    }

    @Override
    protected boolean cacheInBackground() throws Exception {
        return false;
    }

    @Override
    protected void appendCachedData() {

    }
}
