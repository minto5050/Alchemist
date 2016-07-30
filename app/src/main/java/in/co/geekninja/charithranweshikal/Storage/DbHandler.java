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
    private static DbHandler sInstance;
    private final Context context;
    public static synchronized DbHandler getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DbHandler(context.getApplicationContext());
        }
        return sInstance;
    }
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
        feedDb.add(new DbField(Database.FEED_ROW_USER_ID,DbGen.TEXT));
        feedDb.add(new DbField(Database.FEED_ROW_CREATED_AT,DbGen.TEXT));
        feedDb.add(new DbField(Database.FEED_ROW_ID,DbGen.TEXT,true,false,true));
        String queryFeed=Engine.getQuery(DbGen.CREATE_TABLE,Database.TAB_FEED,feedDb);
        db.execSQL(queryFeed);
        Fetcher.startActionCurrent(context);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion==2) {
            db.execSQL("ALTER TABLE " + Database.TAB_FEED + " ADD " + Database.FEED_ROW_CREATED_AT + " TEXT");
            db.execSQL("ALTER TABLE " + Database.TAB_FEED + " ADD " + Database.FEED_ROW_USER_ID + " TEXT");
        }
    }
    public boolean Insert(ContentValues values,String tableName){
        try {
            SQLiteDatabase db=this.getWritableDatabase();
            db.insertOrThrow(tableName,null,values);
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
                Database.FEED_ROW_TITLE,Database.FEED_ROW_CREATED_AT,Database.FEED_ROW_USER_ID},null,null,null,null,null,null);
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
