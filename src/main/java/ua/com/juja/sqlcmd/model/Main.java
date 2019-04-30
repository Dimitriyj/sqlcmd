package ua.com.juja.sqlcmd.model;

import java.sql.SQLException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        String database = "sqlcmd";
        String user = "postgres";
        String password = "master";

        // CONNECT
        DataBaseManager manager = new JDBCDataBaseManager();
        manager.connect(database, user, password);

        // LIST TABLES
        String[] tables = manager.getTableNames();
        System.out.println(Arrays.toString(tables));

        // LIST COLUMNS OF TABLES
        String[] columnNames = manager.getTableColumns("users");
        System.out.println(Arrays.toString(columnNames));

        // DELETE
        manager.clear("users");

        // INSERT
        DataSet input = new DataSet(3);
        input.put("name", "Gerbert");
        input.put("password", "123");
        input.put("id", 1);
        manager.create("users", input);

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
