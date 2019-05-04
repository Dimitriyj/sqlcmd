package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DataBaseManager;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.view.View;

public class Create implements Command {

    private final DataBaseManager manager;
    private final View view;

    public Create(DataBaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("create|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length % 2 != 0) {
            throw new IllegalArgumentException(String.format("There must be an even number of parameters in format " +
                    "'create|tableName|column1|value1|...|columnN|valueN', instead there is '%s'", command));
        }
        String tableName = data[1];
        int countColums = data.length / 2 - 1;
        DataSet dataSet = new DataSet(countColums);

        for (int index = 1; index < (data.length) / 2; index++) {
            String columnName = data[index * 2];
            String columnValue = data[index * 2 + 1];
            dataSet.put(columnName, columnValue);
        }
        manager.create(tableName, dataSet);
        view.write(String.format("The row %s was successfully created in the '%s' table", dataSet, tableName));
    }
}
