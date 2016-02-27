package in.co.geekninja.dbgen;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class TestClass {
    @Test
    public void QueryGeneratorTest() throws Exception {
        ArrayList<DbField> fields=new ArrayList<>();
        //fields.add(new DbField("id",DbGen.INTEGER,true,true));
        //fields.add(new DbField("id",DbGen.INTEGER,false,true));
        fields.add(new DbField("id",DbGen.INTEGER));
        fields.add(new DbField("id",DbGen.INTEGER));
        fields.add(new DbField("id",DbGen.INTEGER));
        fields.add(new DbField("id",DbGen.INTEGER));
        String query=Engine.getQuery(DbGen.CREATE_TABLE,"TestTable",fields);
        assertNotNull("The query",query);
    }
}