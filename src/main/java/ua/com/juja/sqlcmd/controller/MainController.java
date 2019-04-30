package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.controller.command.*;
import ua.com.juja.sqlcmd.model.DataBaseManager;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.view.View;

public class MainController {

    private Command[] commands;
    private View view;
    private DataBaseManager manager;

    public MainController(View view, DataBaseManager manager) {
        this.view = view;
        this.manager = manager;
        this.commands = new Command[] {new Exit(view),
                                        new Help(view),
                                        new List(manager, view),
                                        new Find(manager, view)
                                        };
    }

    public void run() {
        connectToDb();

        while (true) {
            view.write("Enter the command (or help for reference):");
            String command = view.read();
            if (commands[2].canProcess(command)) {
                commands[2].process(command);
            } else if (commands[3].canProcess(command)) {
                commands[3].process(command);
            } else if (commands[1].canProcess(command)) {
                commands[1].process(command);
            } else if (commands[0].canProcess(command)) {
                commands[0].process(command);
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
