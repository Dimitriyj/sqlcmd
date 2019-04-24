package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.model.DataBaseManager;
import ua.com.juja.sqlcmd.model.JDBCDataBaseManager;
import ua.com.juja.sqlcmd.view.Console;
import ua.com.juja.sqlcmd.view.View;

public class Main {
    public static void main(String[] args) {

        View view = new Console();
        DataBaseManager manager = new JDBCDataBaseManager();

        MainController controller = new MainController(view, manager);
        controller.run();

    }
}
