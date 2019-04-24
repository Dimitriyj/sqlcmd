package ua.com.juja.sqlcmd.model;

public class JDBCDataBaseManagerTest extends DataBaseManagerTest{

    @Override
    public DataBaseManager getDataBaseManager() {
        return new JDBCDataBaseManager();
    }
}
