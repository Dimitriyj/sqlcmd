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
            if (input == null) {
                new Exit(view).process(input);
            }
            for (Command command : commands) {
                if (command.canProcess(input)) {
                    command.process(input);
                    break;
                }
            }
            view.write("Enter the command (or help for reference):");
        }
    }
}
