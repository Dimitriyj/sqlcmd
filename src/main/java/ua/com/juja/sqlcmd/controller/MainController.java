package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.controller.command.*;
import ua.com.juja.sqlcmd.model.DataBaseManager;
import ua.com.juja.sqlcmd.view.View;

public class MainController {

    private Command[] commands;
    private View view;

    public MainController(View view, DataBaseManager manager) {
        this.view = view;
        this.commands = new Command[] {
                new Connect(manager, view),
                new Help(view),
                new Exit(view),
                new isConnected(manager, view),
                new List(manager, view),
                new Clear(manager, view),
                new Create(manager, view),
                new Find(manager, view),
                new Unsupported(view)
        };
    }

    public void run() {

        try {
            doWork();
        } catch (ExitExcept e) {
            // do nothing
        }
    }

    private void doWork() {
        view.write("Welcome (привет) !");
        view.write("Please enter username, database name and password in the format: connect|database|username|password");

        while (true) {
            String input = view.read();
            for (Command command : commands) {
                try {
                    if (command.canProcess(input)) {
                        command.process(input);
                        break;
                    }
                } catch (Exception e) {
                    if(e instanceof ExitExcept) {
                        throw new ExitExcept();
                    }
                    printError(e);
                    break;
                }
            }
            view.write("Enter the command (or help for reference):");
        }
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
