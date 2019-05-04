package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.view.View;

public class Help implements Command {

    private View view;

    public Help(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("help");
    }

    @Override
    public void process(String command) {
        view.write("Existing commands:");

        view.write("\tconnect|databaseName|userName|password");
        view.write("\t\tfor connect to the database with which we will work");

        view.write("\tlist");
        view.write("\t\tfor a list of all the tables in the database to which you are connected");

        view.write("\tclear|tableName"); // TODO получить дополнительное подтверждение
        view.write("\t\tfor clean the table named 'tableName'");

        view.write("\tcreate|tableName|column1|value1|...|columnN|valueN");
        view.write("\t\tfor create a row in a table named 'tableName'");

        view.write("\tfind|tableName");
        view.write("\t\tfor get the table contents 'tableName'");

        view.write("\thelp");
        view.write("\t\tfor display this list");

        view.write("\texit");
        view.write("\t\tfor exit the program");
    }
}
