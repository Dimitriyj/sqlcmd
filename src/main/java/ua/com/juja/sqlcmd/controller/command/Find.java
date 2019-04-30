package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DataBaseManager;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.view.View;

public class Find implements Command {

    private final View view;
    private final DataBaseManager manager;

    public Find(DataBaseManager manager, View view) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("find|");
    }

    @Override
    public void process(String command) {
        try {
            String[] data = command.split("\\|");
            if (data.length != 2) {
                throw new IllegalArgumentException("Invalid number of parameters separated by sign '|', expected 2, in stock: " + data.length);
            }
            String tableName = data[1];

            String[] tableColumns = manager.getTableColumns(tableName);
            printHeader(tableColumns);

            DataSet[] tableData = manager.getTableData(tableName);
            printTable(tableData);
        } catch (Exception e) {
            printError(e);
        }
    }

    private void printHeader(String[] tableColumns) {
        String result = "|";
        for (String name : tableColumns) {
            result += name + "|";
        }
        view.write("------------");
        view.write(result);
        view.write("------------");
    }

    private void printTable(DataSet[] tableData) {
        for (DataSet row : tableData) {
            printRow(row);
        }
    }

    private void printRow(DataSet row) {
        Object[] values = row.getValues();
        String result = "|";
        for (Object value : values) {
            result += value + "|";
        }
        view.write(result);
    }

    private void printError(Exception e) {
        String message = /*e.getClass().getSimpleName() + ": " +*/ e.getMessage();
        Throwable cause = e.getCause();
        if (cause != null) {
            message += " " + /*cause.getClass().getSimpleName() + ": " +*/ cause.getMessage();
        }
        view.write("failure due to reason: " + message);
        view.write("Try again");
    }
}
