package in.co.geekninja.charithranweshikal.Storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import in.co.geekninja.charithranweshikal.Adapters.Feeds;
import in.co.geekninja.charithranweshikal.Services.Fetcher;
import in.co.geekninja.dbgen.DbField;
import in.co.geekninja.dbgen.DbGen;
import in.co.geekninja.dbgen.Engine;

/**
 * Created by PS on 2/26/2016.
 */
public class DbHandler extends SQLiteOpenHelper {
    private final Context context;

    public DbHandler(Context context) {
        super(context, Database.name, null, Database.version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DbGen gen=new DbGen();
        ArrayList<DbField> feedDb=new ArrayList<>();
        feedDb.add(new DbField(Database.FEED_ROW_TITLE,DbGen.TEXT));
        feedDb.add(new DbField(Database.FEED_ROW_DESC,DbGen.TEXT));
        feedDb.add(new DbField(Database.FEED_ROW_THUMB,DbGen.TEXT));
        feedDb.add(new DbField(Database.FEED_ROW_FULLIMG,DbGen.TEXT));
        feedDb.add(new DbField(Database.FEED_ROW_LINK,DbGen.TEXT));
        feedDb.add(new DbField(Database.FEED_ROW_FROM,DbGen.TEXT));
        feedDb.add(new DbField(Database.FEED_ROW_SINCE,DbGen.TEXT));
        feedDb.add(new DbField(Database.FEED_ROW_UNTILL,DbGen.TEXT));
        feedDb.add(new DbField(Database.FEED_ROW_PAGING_TOKEN,DbGen.TEXT));
        feedDb.add(new DbField(Database.FEED_ROW_ID,DbGen.TEXT,true,false,true));
        String queryFeed=Engine.getQuery(DbGen.CREATE_TABLE,Database.TAB_FEED,feedDb);
        db.execSQL(queryFeed);
        Fetcher.startActionCurrent(context);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Engine.getQuery(DbGen.DROP_TABLE,"Feed",null));
    }
    public boolean Insert(ContentValues values,String tableName){
        try {
            SQLiteDatabase db=this.getWritableDatabase();
            db.insert(tableName,null,values);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Feeds> getFeeds() {
        List<Feeds> array=new ArrayList<>();
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cur=db.query(true,Database.TAB_FEED,new String[]{
                Database.FEED_ROW_LINK,
                Database.FEED_ROW_FROM,
                Database.FEED_ROW_FULLIMG,
                Database.FEED_ROW_DESC,
                Database.FEED_ROW_THUMB,
                Database.FEED_ROW_TITLE},null,null,null,null,null,null);
        if (cur.moveToFirst())
        {
            do {
                Feeds feed=new Feeds();
                feed.setTitle(cur.getString(cur.getColumnIndex(Database.FEED_ROW_TITLE)));
                feed.setImageUrl(cur.getString(cur.getColumnIndex(Database.FEED_ROW_THUMB)));
                feed.setDesc(cur.getString(cur.getColumnIndex(Database.FEED_ROW_DESC)));
                feed.setFull_image(cur.getString(cur.getColumnIndex(Database.FEED_ROW_FULLIMG)));
                feed.setLink(cur.getString(cur.getColumnIndex(Database.FEED_ROW_LINK)));
                feed.setFrom(cur.getString(cur.getColumnIndex(Database.FEED_ROW_FROM)));
                array.add(feed);
            }while (cur.moveToNext());
        }
        return array;
    }

}
