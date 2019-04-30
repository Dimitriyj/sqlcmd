package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.model.DataBaseManager;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.view.View;
import java.util.Arrays;

public class MainController {

    private View view;
    private DataBaseManager manager;

    public MainController(View view, DataBaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    public void run() {
        connectToDb();

        while (true) {
            view.write("Enter the command (or help for reference):");
            String command = view.read();
            if (command.equals("list")) {
                doList();
            } else if (command.startsWith("find|")) {
                doFind(command);
            } else if (command.equals("help")) {
                doHelp();
            } else if (command.equals("exit")) {
                view.write("Session completed");
                System.exit(0);
            } else {
                view.write("Invalid command: " + command);
            }
        }
    }

    private void connectToDb() {
        view.write("Welcome!");
        view.write("Please enter username, database name and password in the format: database|username|password");
        while (true) {
            try {
                String string = view.read();
                String[] data = string.split("[|]");
                if (data.length != 3) {
                    throw new IllegalArgumentException("Invalid number of parameters separated by sign '|', expected 3, in stock: " + data.length);
                }
                String databaseName = data[0];
                String userName = data[1];
                String password = data[2];

                manager.connect(databaseName, userName, password);
                break;
            } catch (Exception e) {
                printError(e);
            }
        }
        view.write("Connection successful!");
    }

    private void doList() {
        String[] tableNames = manager.getTableNames();
        String message = Arrays.toString(tableNames);
        view.write(message);
    }

    private void doFind(String command) {
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

    private void doHelp() {
        view.write("Existing commands:");
        view.write("\tlist");
        view.write("\t\tfor a list of all the tables in the database to which you are connected");

        view.write("\tfind|tableName");
        view.write("\t\tto get the table contents 'tableName'");

        view.write("\thelp");
        view.write("\t\tto display this list");

        view.write("\texit");
        view.write("\t\tto exit the program");
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
