package in.co.geekninja.charithranweshikal.Misc;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PS on 2/26/2016.
 */
public class Boilerplate {
    public static final int SHORT_DESC = 1;
    public static final int TITLE = 2;
    Typeface fontPrimary,fontSecondary;

    public static Typeface getFontPrimary(Context context) {
        //File file=new File(context.getFilesDir()+"/fonts/aruna-Normal.ttf");
        File file=new File(context.getFilesDir()+"/fonts/newshunt-bold.otf");
        if (!file.exists())
        {
            Boilerplate.copyAssets(context);
        }
        try {
            return Typeface.createFromFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getReleavent(String feedSplitted,int type)
    {
        return getReleavent(feedSplitted.split("\\r?\\n"),type);
    }
    public static String getReleavent(String[] feedSplitted, int TyPE) {
        List<String> array=new ArrayList<String>();
        for (String sin: feedSplitted)
            array.add(sin);
        if (TyPE==SHORT_DESC)
        {
            array.remove(0);
        }
        for (String line:array)
        {
            if (!line.equals(" ")
                    &&!line.equals("")
                    &&!line.contains("=====")
                    &&!line.contains("xxxxx")
                    &&!line.contains("......")
                    &&!line.contains(",,,,,")
                    &&!line.contains("-----")
                    &&!line.contains("#####")
                    &&!line.contains("\\u2605\\u2605\\u2605\\u2605")
                    &&!line.contains("****")
                    &&!line.contains("\\u2606\\u2606\\u2606\\u2606"))
                return line;
        }
        return null;
    }
    public static Map<String, List<String>> getUrlParameters(String url)
            throws UnsupportedEncodingException {
        Map<String, List<String>> params = new HashMap<String, List<String>>();
        String[] urlParts = url.split("\\?");
        if (urlParts.length > 1) {
            String query = urlParts[1];
            for (String param : query.split("&")) {
                String pair[] = param.split("=");
                String key = URLDecoder.decode(pair[0], "UTF-8");
                String value = "";
                if (pair.length > 1) {
                    value = URLDecoder.decode(pair[1], "UTF-8");
                }
                List<String> values = params.get(key);
                if (values == null) {
                    values = new ArrayList<String>();
                    params.put(key, values);
                }
                values.add(value);
            }
        }
        return params;
    }

    public void setFontPrimary(Typeface fontPrimary) {
        this.fontPrimary = fontPrimary;
    }

    public static Typeface getFontSecondary(Context context) {
        File file=new File(context.getFilesDir()+"/fonts/newshunt-regular.otf");
        if (!file.exists())
        {
            Boilerplate.copyAssets(context);
        }
        try {
            return Typeface.createFromFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setFontSecondary(Typeface fontSecondary) {
        this.fontSecondary = fontSecondary;
    }

    public static void copyAssets(Context context) {
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        if (files != null) for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(filename);
                File outFile = new File(context.getFilesDir()+"/fonts/", filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
            } catch(IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
                e.printStackTrace();
            }
            finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // NOOP

                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // NOOP
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
}
