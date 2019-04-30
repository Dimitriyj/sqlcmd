package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DataBaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.util.Arrays;

public class List implements Command {

    private View view;
    private DataBaseManager manager;

    public List(DataBaseManager manager, View view) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("list");
    }

    @Override
    public void process(String command) {
        String[] tableNames = manager.getTableNames();
        String message = Arrays.toString(tableNames);
        view.write(message);
    }
}
