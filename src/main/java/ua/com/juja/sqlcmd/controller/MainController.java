package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.model.DataBaseManager;
import ua.com.juja.sqlcmd.view.View;

public class MainController {

    private View view;
    private DataBaseManager manager;

    public MainController(View view, DataBaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    public void run() {
        connectToDb();
    }

    private void connectToDb() {
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
