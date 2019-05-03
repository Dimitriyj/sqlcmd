package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DataBaseManager;
import ua.com.juja.sqlcmd.view.View;

public class Connect implements Command {

    private static String COMMAND_SAMPLE = "connect|sqlcmd|postgres|master";
    private final DataBaseManager manager;
    private final View view;

    public Connect(DataBaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("connect|");
    }

    @Override
    public void process(String command) {

        try {
            String[] data = command.split("\\|");
            if (data.length != count()) {
                throw new IllegalArgumentException(String.format("Invalid number of parameters separated by sign '|', " +
                        "expected %s, in stock: %s", count(), data.length));
            }
            String databaseName = data[1];
            String userName = data[2];
            String password = data[3];

            manager.connect(databaseName, userName, password);
            view.write("Connection successful!");
        } catch (Exception e) {
            printError(e);
        }
    }

    private int count() {
        return COMMAND_SAMPLE.split("\\|").length;
    }

    private void printError(Exception e) {
        String message = e.getMessage();
        Throwable cause = e.getCause();
        if (cause != null) {
            message += " " + cause.getMessage();
        }
        view.write("failure due to reason: " + message);
        view.write("Try again");
    }

}
