package in.co.geekninja.charithranweshikal.Misc;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PS on 2/26/2016.
 */
public class Boilerplate {
    /**
     * The constant SHORT_DESC.
     */
    public static final int SHORT_DESC = 1;
    /**
     * The constant TITLE.
     */
    public static final int TITLE = 2;
    /**
     * The Font primary.
     */
    Typeface fontPrimary, /**
     * The Font secondary.
     */
    fontSecondary;

    /**
     * Gets font primary.
     *
     * @param context the context
     * @return the font primary
     */
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

    /**
     * Stack Blur v1.0 from
     * http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
     * Java Author: Mario Klingemann <mario at quasimondo.com>
     * http://incubator.quasimondo.com
     * <p/>
     * created Feburary 29, 2004
     * Android port : Yahel Bouaziz <yahel at kayenko.com>
     * http://www.kayenko.com
     * ported april 5th, 2012
     * <p/>
     * This is a compromise between Gaussian Blur and Box blur
     * It creates much better looking blurs than Box Blur, but is
     * 7x faster than my Gaussian Blur implementation.
     * <p/>
     * I called it Stack Blur because this describes best how this
     * filter works internally: it creates a kind of moving stack
     * of colors whilst scanning through the image. Thereby it
     * just has to add one new block of color to the right side
     * of the stack and remove the leftmost color. The remaining
     * colors on the topmost layer of the stack are either added on
     * or reduced by one, depending on if they are on the right or
     * on the left side of the stack.
     * <p/>
     * If you are using this algorithm in your code please add
     * the following line:
     * Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>
     *
     * @param sentBitmap the sent bitmap
     * @param scale      the scale
     * @param radius     the radius
     * @return the bitmap
     * @throws IllegalStateException the illegal state exception
     */

    public static  Bitmap fastblur(Bitmap sentBitmap, float scale, int radius) throws IllegalStateException {

        int width = Math.round(sentBitmap.getWidth() * scale);
        int height = Math.round(sentBitmap.getHeight() * scale);
        if (width<=0||height<=0)
            return sentBitmap;
        //throw new IllegalArgumentException("Could be no image");
        else {
            sentBitmap = Bitmap.createScaledBitmap(sentBitmap, width, height, false);

            Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

            if (radius < 1) {
                return (null);
            }

            int w = bitmap.getWidth();
            int h = bitmap.getHeight();

            int[] pix = new int[w * h];
            Log.e("pix", w + " " + h + " " + pix.length);
            bitmap.getPixels(pix, 0, w, 0, 0, w, h);

            int wm = w - 1;
            int hm = h - 1;
            int wh = w * h;
            int div = radius + radius + 1;

            int r[] = new int[wh];
            int g[] = new int[wh];
            int b[] = new int[wh];
            int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
            int vmin[] = new int[Math.max(w, h)];

            int divsum = (div + 1) >> 1;
            divsum *= divsum;
            int dv[] = new int[256 * divsum];
            for (i = 0; i < 256 * divsum; i++) {
                dv[i] = (i / divsum);
            }

            yw = yi = 0;

            int[][] stack = new int[div][3];
            int stackpointer;
            int stackstart;
            int[] sir;
            int rbs;
            int r1 = radius + 1;
            int routsum, goutsum, boutsum;
            int rinsum, ginsum, binsum;

            for (y = 0; y < h; y++) {
                rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
                for (i = -radius; i <= radius; i++) {
                    p = pix[yi + Math.min(wm, Math.max(i, 0))];
                    sir = stack[i + radius];
                    sir[0] = (p & 0xff0000) >> 16;
                    sir[1] = (p & 0x00ff00) >> 8;
                    sir[2] = (p & 0x0000ff);
                    rbs = r1 - Math.abs(i);
                    rsum += sir[0] * rbs;
                    gsum += sir[1] * rbs;
                    bsum += sir[2] * rbs;
                    if (i > 0) {
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                    } else {
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                    }
                }
                stackpointer = radius;

                for (x = 0; x < w; x++) {

                    r[yi] = dv[rsum];
                    g[yi] = dv[gsum];
                    b[yi] = dv[bsum];

                    rsum -= routsum;
                    gsum -= goutsum;
                    bsum -= boutsum;

                    stackstart = stackpointer - radius + div;
                    sir = stack[stackstart % div];

                    routsum -= sir[0];
                    goutsum -= sir[1];
                    boutsum -= sir[2];

                    if (y == 0) {
                        vmin[x] = Math.min(x + radius + 1, wm);
                    }
                    p = pix[yw + vmin[x]];

                    sir[0] = (p & 0xff0000) >> 16;
                    sir[1] = (p & 0x00ff00) >> 8;
                    sir[2] = (p & 0x0000ff);

                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];

                    rsum += rinsum;
                    gsum += ginsum;
                    bsum += binsum;

                    stackpointer = (stackpointer + 1) % div;
                    sir = stack[(stackpointer) % div];

                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];

                    rinsum -= sir[0];
                    ginsum -= sir[1];
                    binsum -= sir[2];

                    yi++;
                }
                yw += w;
            }
            for (x = 0; x < w; x++) {
                rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
                yp = -radius * w;
                for (i = -radius; i <= radius; i++) {
                    yi = Math.max(0, yp) + x;

                    sir = stack[i + radius];

                    sir[0] = r[yi];
                    sir[1] = g[yi];
                    sir[2] = b[yi];

                    rbs = r1 - Math.abs(i);

                    rsum += r[yi] * rbs;
                    gsum += g[yi] * rbs;
                    bsum += b[yi] * rbs;

                    if (i > 0) {
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                    } else {
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                    }

                    if (i < hm) {
                        yp += w;
                    }
                }
                yi = x;
                stackpointer = radius;
                for (y = 0; y < h; y++) {
                    // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                    pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                    rsum -= routsum;
                    gsum -= goutsum;
                    bsum -= boutsum;

                    stackstart = stackpointer - radius + div;
                    sir = stack[stackstart % div];

                    routsum -= sir[0];
                    goutsum -= sir[1];
                    boutsum -= sir[2];

                    if (x == 0) {
                        vmin[y] = Math.min(y + r1, hm) * w;
                    }
                    p = x + vmin[y];

                    sir[0] = r[p];
                    sir[1] = g[p];
                    sir[2] = b[p];

                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];

                    rsum += rinsum;
                    gsum += ginsum;
                    bsum += binsum;

                    stackpointer = (stackpointer + 1) % div;
                    sir = stack[stackpointer];

                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];

                    rinsum -= sir[0];
                    ginsum -= sir[1];
                    binsum -= sir[2];

                    yi += w;
                }
            }

            Log.e("pix", w + " " + h + " " + pix.length);
            bitmap.setPixels(pix, 0, w, 0, 0, w, h);

            return (bitmap);
        }
    }

    /**
     * Gets releavent.
     *
     * @param feedSplitted the feed splitted
     * @param type         the type
     * @return the releavent
     */
    public static String getReleavent(String feedSplitted,int type)
    {
        return getReleavent(feedSplitted.split("\\r?\\n"),type);
    }

    /**
     * Average color int [ ].
     *
     * @param bmp the bmp
     * @return the int [ ]
     */
    public static int[] averageColor(Bitmap bmp){
        int intArray[] = new int[bmp.getWidth()*bmp.getHeight()];
        bmp.getPixels(intArray, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        return intArray;
    }

    /**
     * Gets releavent.
     *
     * @param feedSplitted the feed splitted
     * @param TyPE         the ty pe
     * @return the releavent
     */
    public static String getReleavent(String[] feedSplitted, int TyPE) {
        List<String> array=new ArrayList<String>();
        if (feedSplitted.length==1)
        {
            return feedSplitted[0].substring(0,getSafeEnd(feedSplitted[0],10));
        }
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

    private static int getSafeEnd(String s,int length) {
        char[] str = s.toCharArray();
        if (str.length>length)
            return length;
        else
            return str.length;
    }

    /**
     * Export databse.
     *
     * @param databaseName the database name
     * @param context      the context
     */
    public   void exportDatabse(String databaseName, Context context) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//" + context.getPackageName() + "//databases//" + databaseName + "";
                String backupDBPath = ".Charithranweshikal/"+databaseName+".db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets url parameters.
     *
     * @param url the url
     * @return the url parameters
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
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

    /**
     * Sets font primary.
     *
     * @param fontPrimary the font primary
     */
    public void setFontPrimary(Typeface fontPrimary) {
        this.fontPrimary = fontPrimary;
    }

    /**
     * Gets font secondary.
     *
     * @param context the context
     * @return the font secondary
     */
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

    /**
     * Sets font secondary.
     *
     * @param fontSecondary the font secondary
     */
    public void setFontSecondary(Typeface fontSecondary) {
        this.fontSecondary = fontSecondary;
    }

    /**
     * Copy assets.
     *
     * @param context the context
     */
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
