package ua.com.juja.sqlcmd.model;

import java.util.Arrays;

public class InMemoryDataBaseManager implements DataBaseManager {

    private DataSet[] data = new DataSet[100];
    private int freeIndex = 0;

    @Override
    public String[] getTableNames() {
        return new String[] {"test, users"};
    }

    @Override
    public DataSet[] getTableData(String tableName) {
        validateTable(tableName);
        return Arrays.copyOf(data, freeIndex);
    }

    private void validateTable(String tableName) {
        if (!"users".equals(tableName)) {
            throw new UnsupportedOperationException("Only for 'users' table, but you try to work with " + tableName);
        }
    }

    @Override
    public void connect(String database, String userName, String password) {
        // do nothing
    }

    @Override
    public void clear(String tableName) {
        validateTable(tableName);
        data = new DataSet[100];
        freeIndex = 0;
    }

    @Override
    public void create(String tableName, DataSet input) {
        validateTable(tableName);
        data[freeIndex] = input;
        freeIndex++;
    }

    @Override
    public void update(String tableName, int id, DataSet dataSet) {
        validateTable(tableName);
        for (int index = 0; index < freeIndex; index++) {
            if (data[index].get("id") == (Object) id) {
                data[index].updateFrom(dataSet);
            }
        }
    }
}
