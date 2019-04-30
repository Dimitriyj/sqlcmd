package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DataBaseManager;
import ua.com.juja.sqlcmd.view.View;

public class isConnected implements Command {

    private final DataBaseManager manager;
    private final View view;

    public isConnected(DataBaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return !manager.isConnected();
    }

    @Override
    public void process(String command) {
        view.write(String.format("You cannot use the '%s' command until you connect using the command " +
                "connect|databaseName|userName|password", command));
    }
}
