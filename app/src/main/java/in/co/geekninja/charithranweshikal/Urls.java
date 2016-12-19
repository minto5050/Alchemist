package in.co.geekninja.charithranweshikal;

/**
 * The class for keeping all the public static data such as the Group ID and Graph Urls
 * Created by PS on 2/26/2016.
 */
public class Urls {
    /**
     * The constant GrouypID.
     */
    public static final String GrouypID="416238708555189";
    /**
     * The constant GraphUrl.
     */
    public static final String GraphUrl = "https://graph.facebook.com/v2.7";


    /**
     * Gets base.
     *
     * @return the base
     */
    public static String getBase() {
        return GraphUrl+"/"+GrouypID;
    }
}
