package ua.com.juja.sqlcmd.model;

import java.sql.SQLException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String database = "sqlcmd";
        String user = "postgres";
        String password = "master";

        // CONNECT
        DataBaseManager manager = new JDBCDataBaseManager();
        manager.connect(database, user, password);

        // DELETE
        manager.clear("users");

        // INSERT
        DataSet input = new DataSet(3);
        input.put("name", "Gerbert");
        input.put("password", "123");
        input.put("id", 1);
        manager.create("users", input);

        // LIST TABLES
        String[] tables = manager.getTableNames();
        System.out.println(Arrays.toString(tables));

        // SELECT
        String tableName = "users";
        DataSet[] result = manager.getTableData(tableName);
        System.out.println(Arrays.toString(result));

        // UPDATE
        DataSet newValue = new DataSet(3);
        newValue.put("password", "12345");
        manager.update("users", 1, newValue);

        // SELECT AFTER UPDATE
        String tableNameUpdated = "users";
        DataSet[] resultUpdated = manager.getTableData(tableNameUpdated);
        System.out.println(Arrays.toString(resultUpdated));
    }
}
