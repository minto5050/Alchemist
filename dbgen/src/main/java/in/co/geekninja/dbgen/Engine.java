package in.co.geekninja.dbgen;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by PS on 1/24/2016.
 */
public class Engine {

    private Context context;
    private SQLiteDatabase db;

    public static Engine newInstance(Context context, SQLiteOpenHelper db) {
        Engine engine = new Engine();
        engine.context=context;
        engine.db=db.getWritableDatabase();
        return engine;
    }

    public static Engine newInstance(Context context) {
        Engine engine = new Engine();
        engine.context=context;
        return engine;
    }
    @NonNull
    public static String getQuery(int type, String tableName, ArrayList<DbField> fileds)
    {
        StringBuilder query=new StringBuilder();
        switch (type)
        {
            case 0:
                query.append("Create table "+tableName+" (");
                int i=0;
                for (DbField df : fileds) {
                    i++;
                    query.append(" "
                            + df.getFieldName() + " " + df.getFieldType()
                            +" "+((df.isPrimaryKey()?"PRIMARY KEY ":""))
                            +" "+((df.isAutoIncrement()?"AUTOINCREMENT ":""))
                            +" "+((df.isNotNull())?"NOT NULL ":"")
                            +" "+((df.getDefault()!=null)?"DEFAULT "+df.getDefault():"")
                            +" "+((i==fileds.size())?")":","));
                }
                break;
            case 1:
                query.append("Drop table if exists "+tableName);
                break;
                default:
                    break;
        }
        query.append(";");
        return query.toString();
    }
}
