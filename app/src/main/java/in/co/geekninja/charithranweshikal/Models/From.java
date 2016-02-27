package in.co.geekninja.charithranweshikal.Models;

import java.io.Serializable;

/**
 * Created by PS on 2/27/2016.
 */
public class From implements Serializable
{
    private String name;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
