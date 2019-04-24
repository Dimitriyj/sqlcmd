package ua.com.juja.sqlcmd.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public abstract class DataBaseManagerTest {

    private DataBaseManager manager;
    public abstract DataBaseManager getDataBaseManager();

    @Before
    public void setup() {
        manager = getDataBaseManager();
        manager.connect("sqlcmd", "postgres", "master");
    }

    @Test
    public void testGetAllTableNames() {
        String[] tableNames = manager.getTableNames();
        assertEquals("[test, users]", Arrays.toString(tableNames));
    }

    @Test
    public void testGetTableData() {
        // given
        manager.clear("users");

        // when
        DataSet dataSet = new DataSet(3);
        dataSet.put("name", "Gerbert");
        dataSet.put("password", "123");
        dataSet.put("id", 1);
        manager.create("users", dataSet);

        // then
        DataSet[] users = manager.getTableData("users");
        assertEquals(1, users.length);
        DataSet user = users[0];
        assertEquals("[name, password, id]", Arrays.toString(user.getNames()));
        assertEquals("[Gerbert, 123, 1]", Arrays.toString(user.getValues()));
    }

    @Test
    public void testUpdateTableData() {
        // given
        manager.clear("users");

        DataSet dataSet = new DataSet(3);
        dataSet.put("name", "Gerbert");
        dataSet.put("password", "123");
        dataSet.put("id", 1);
        manager.create("users", dataSet);

        // when
        DataSet newDataSet = new DataSet(3);
        newDataSet.put("password", "12345");
        manager.update("users", 1, newDataSet);

        // then
        DataSet[] users = manager.getTableData("users");
        assertEquals(1, users.length);
        DataSet user = users[0];
        assertEquals("[name, password, id]", Arrays.toString(user.getNames()));
        assertEquals("[Gerbert, 12345, 1]", Arrays.toString(user.getValues()));
    }
}
