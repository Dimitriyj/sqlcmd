package ua.com.juja.sqlcmd.model;

import java.sql.*;

public class JDBCDataBaseManager implements DataBaseManager {
    private Connection connection;

    private String getNamesFormated(DataSet dataSet, String format) {
        String stringNames = "";
        for (String name : dataSet.getNames()) {
            stringNames += String.format(format, name);
        }
        stringNames = stringNames.substring(0, stringNames.length() - 1);
        return stringNames;
    }

    private String getValuesFormated(DataSet dataSet, String format) {
        String stringValues = "";
        for (Object value : dataSet.getValues()) {
            stringValues += String.format(format, value.toString());
        }
        stringValues = stringValues.substring(0, stringValues.length() - 1);
        return stringValues;
    }

    private int getCommonSize(String sql) {
        try (Statement stmt = connection.createStatement(); ResultSet rsCount = stmt.executeQuery(sql)) {
            rsCount.next();
            int size = rsCount.getInt(1);
            return size;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private int getCountTables() {
        String sql = "SELECT COUNT(*) " +
                    "FROM information_schema.tables " +
                    "WHERE table_schema='public' " +
                    "AND table_type='BASE TABLE'";
        return getCommonSize(sql);
    }

    private int getCountRow(String tableName) {
        String sql = "SELECT COUNT(*) FROM public." + tableName;
        return getCommonSize(sql);
    }

    private int getCountColumn(String tableName) {
        String sql = "SELECT COUNT(*) FROM information_schema.columns "
                + "WHERE table_schema= 'public' "
                 + "AND table_name = '" + tableName + "'"
                ;
        return getCommonSize(sql);
    }

    @Override
    public String[] getTableNames() {
        String sql = "SELECT table_name FROM information_schema.tables "
                + "WHERE table_schema = 'public' "
                + "AND table_type = 'BASE TABLE' "
                + "ORDER BY table_name"
                ;
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            String[] tables = new String[getCountTables()];
            int index = 0;
            while (rs.next()) {
                tables[index++] = rs.getString("table_name");
            }
            return tables;
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    @Override
    public String[] getTableColumns(String tableName) {
        String sql = "SELECT * FROM information_schema.columns "
                + "WHERE table_schema= 'public' "
                + "AND table_name = '" + tableName + "'"
                ;
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            String[] tables = new String[getCountColumn(tableName)];
            int index = 0;
            while (rs.next()) {
                tables[index++] = rs.getString("column_name");
            }
            return tables;
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    @Override
    public DataSet[] getTableData(String tableName) {
        int size = getCountRow(tableName);
        DataSet[] tableData = new DataSet[size];
        String sql = "SELECT * FROM public." + tableName;

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            ResultSetMetaData rsmd = rs.getMetaData();
            int index = 0;
            while (rs.next()) {
                DataSet dataSet = new DataSet(rsmd.getColumnCount());
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    dataSet.put(rsmd.getColumnName(i), rs.getObject(i));
                }
                tableData[index++] = dataSet;
            }
            return tableData;
        } catch (SQLException e) {
            e.printStackTrace();
            return new DataSet[0];
        }
    }

    @Override
    public void connect(String database, String userName, String password) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Please add jdbc jar to project", e);
        }
        try {
            if (connection != null) {
                connection.close();
            }
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/" + database, userName, password);
        } catch (SQLException e) {
            connection = null;
            throw new RuntimeException(String.format("Cannot connection for model: %s, user: %s", database, userName), e);
        }
    }

    @Override
    public void clear(String tableName) {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM public." + tableName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create(String tableName, DataSet input) {
        String columnNames = getNamesFormated(input, "%s,");
        String columnValues = getValuesFormated(input, "'%s',");
        String sql = "INSERT INTO public." + tableName + "(" + columnNames + ") VALUES(" + columnValues + ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(String tableName, int id, DataSet dataSet) {
        String tableNames = getNamesFormated(dataSet, "%s = ?,");
        String sql = "UPDATE public." + tableName + " SET " + tableNames + " WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            int index = 1;
            for (Object value : dataSet.getValues()) {
                pstmt.setObject(index++, value);
            }
            pstmt.setInt(index, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isConnected() {
        return connection != null;
    }

}
