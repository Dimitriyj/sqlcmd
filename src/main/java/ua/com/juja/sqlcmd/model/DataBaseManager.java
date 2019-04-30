package ua.com.juja.sqlcmd.model;

public interface DataBaseManager {

    String[] getTableNames();

    String[] getTableColumns(String tableName);

    DataSet[] getTableData(String tableName);

    void connect(String database, String userName, String password);

    void clear(String tableName);

    void create(String tableName, DataSet input);

    void update(String tableName, int id, DataSet dataSet);

    boolean isConnected();
}
