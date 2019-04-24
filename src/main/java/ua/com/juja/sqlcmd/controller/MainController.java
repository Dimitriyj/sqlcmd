package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.model.DataBaseManager;
import ua.com.juja.sqlcmd.model.InMemoryDataBaseManager;
import ua.com.juja.sqlcmd.model.JDBCDataBaseManager;
import ua.com.juja.sqlcmd.view.Console;
import ua.com.juja.sqlcmd.view.View;

public class MainController {
    public static void main(String[] args) {
        View view = new Console();
        //DataBaseManager manager = new InMemoryDataBaseManager();
        DataBaseManager manager = new JDBCDataBaseManager();

        view.write("Привет юзер!");
        view.write("Введи пожалуйста имя пользователя, имя базы данных и пароль в формате: database|username|password");
        while (true) {
            String string = view.read();
            String[] data = string.split("[|]");
            String databaseName = data[0];
            String userName = data[1];
            String password = data[2];
            try {
                manager.connect(databaseName, userName, password);
                break;
            } catch (Exception e) {
                String message = e.getMessage();
                if (e.getCause() != null) {
                    message += " " + e.getCause().getMessage();
                }
                view.write("Неудача по причине: " + message);
                view.write("Повторите попытку");
            }
        }
        view.write("Успех!");
    }
}
