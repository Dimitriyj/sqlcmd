package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DataBaseManager;
import ua.com.juja.sqlcmd.view.View;

public class Clear implements Command {

    private final DataBaseManager manager;
    private final View view;

    public Clear(DataBaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("clear|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length != 2) {
            throw new IllegalArgumentException("Invalid number of parameters separated by sign '|', " +
                    "expected 2, in stock: " + data.length);
        }
        manager.clear(data[1]);
        view.write(String.format("Table '%s' was successfully cleared", data[1]));
    }
}
