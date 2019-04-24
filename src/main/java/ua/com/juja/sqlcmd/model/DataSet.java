package ua.com.juja.sqlcmd.model;

import java.util.Arrays;

public class DataSet {

    static class Data {
        private String name;
        private Object value;

        public Data(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public Object getValue() {
            return value;
        }
    }

    private int freeIndex = 0;
    private Data[] data;

    public DataSet(int count) {
        this.data = new Data[count];
    }

    public void put(String name, Object value) {
        for (int index = 0; index < freeIndex; index++) {
            if (data[index].getName().equals(name)) {
                data[index].value = value;
                return;
            }
        }
        data[freeIndex++] = new Data(name, value);
    }

    public void updateFrom(DataSet dataSet) {
        String[] names = dataSet.getNames();
        Object[] values = dataSet.getValues();
        for (int index = 0; index < names.length; index++) {
            this.put(names[index], values[index]);
        }
    }

    public String[] getNames() {
        String[] columnNames = new String[freeIndex];
        for (int i = 0; i < freeIndex; i++) {
            columnNames[i] = data[i].getName();
        }
        return columnNames;
    }

    public Object[] getValues() {
        Object[] columnValues = new Object[freeIndex];
        for (int i = 0; i < freeIndex; i++) {
            columnValues[i] = data[i].getValue();
        }
        return columnValues;
    }

    public Object get(String name) {
        for (int index = 0; index < freeIndex; index++) {
            if (data[index].getName().equals(name)) {
                return data[index].getValue();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "DataSet{\n" +
                "names: " + Arrays.toString(getNames()) + "\n" +
                "values: " + Arrays.toString(getValues()) + "\n" +
                "}";
    }
}
