package in.co.geekninja.charithranweshikal.Storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import in.co.geekninja.dbgen.DbField;
import in.co.geekninja.dbgen.DbGen;
import in.co.geekninja.dbgen.Engine;

/**
 * Created by PS on 2/26/2016.
 */
public class DbHandler extends SQLiteOpenHelper {
    public DbHandler(Context context) {
        super(context, Database.name, null, Database.version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DbGen gen=new DbGen();
        ArrayList<DbField> feedDb=new ArrayList<>();
        feedDb.add(new DbField(Database.FEED_ROW_TITLE,DbGen.TEXT));
        feedDb.add(new DbField(Database.FEED_ROW_DESC,DbGen.TEXT));
        feedDb.add(new DbField(Database.FEED_ROW_THUMB,DbGen.TEXT));
        feedDb.add(new DbField(Database.FEED_ROW_FULLIMG,DbGen.TEXT));
        feedDb.add(new DbField(Database.FEED_ROW_ID,DbGen.TEXT,true,false,true));
        db.execSQL(Engine.getQuery(DbGen.CREATE_TABLE,Database.TAB_FEED,feedDb));
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

}
