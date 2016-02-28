package in.co.geekninja.charithranweshikal.Storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by PS on 2/28/2016.
 */
public class SharedPrefs {
    public static final String UNTIL = "until";
    public static final String PAGING_TOKEN = "pagingToken";
    public static final String TOKEN = "token";
    public static String SINCE="since";
    SharedPreferences sp;
    static SharedPrefs s;
    public static SharedPreferences getInstance(Context context)
    {
        if (s==null)
            s=new SharedPrefs(context);
        return s.sp;
    }

    public SharedPrefs(Context context) {
        this.sp=context.getSharedPreferences("Alchemist",Context.MODE_PRIVATE);
    }

}
