package ua.com.juja.sqlcmd.model;

public class InMemoryDataBaseManagerTest extends DataBaseManagerTest{

    @Override
    public DataBaseManager getDataBaseManager() {
        return new InMemoryDataBaseManager();
    }
}
