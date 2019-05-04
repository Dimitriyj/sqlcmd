package ua.com.juja.sqlcmd.integration;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.controller.Main;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static junit.framework.TestCase.assertEquals;

public class IntegrationTest {

    private ConfigurableInputStream in;
    private ByteArrayOutputStream out;

    @Before
    public void setup() {
        in = new ConfigurableInputStream();
        out = new ByteArrayOutputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

    @Test
    public void testHelp() {
        // given
        in.add("help");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Welcome (привет) !\r\n" +
                "Please enter username, database name and password in the format: connect|database|username|password\r\n" +
                // help
                "Existing commands:\r\n" +
                "\tconnect|databaseName|userName|password\r\n" +
                "\t\tfor connect to the database with which we will work\r\n" +
                "\tlist\r\n" +
                "\t\tfor a list of all the tables in the database to which you are connected\r\n" +
                "\tclear|tableName\r\n" +
                "\t\tfor clean the table named 'tableName'\r\n" +
                "\tcreate|tableName|column1|value1|...|columnN|valueN\r\n" +
                "\t\tfor create a row in a table named 'tableName'\r\n" +
                "\tfind|tableName\r\n" +
                "\t\tfor get the table contents 'tableName'\r\n" +
                "\thelp\r\n" +
                "\t\tfor display this list\r\n" +
                "\texit\r\n" +
                "\t\tfor exit the program\r\n" +
                "Enter the command (or help for reference):\r\n" +
                // exit
                "Session completed!\r\n", getData());
    }

    private String getData() {
        try {
            String result = new String(out.toByteArray(), "UTF-8");
            out.reset();
            return result;
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }

    @Test
    public void testExit() {
        // given
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Welcome (привет) !\r\n" +
                "Please enter username, database name and password in the format: connect|database|username|password\r\n" +
                // exit
                "Session completed!\r\n", getData());
    }

    @Test
    public void testListWithoutConnect() {
        // given
        in.add("list");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Welcome (привет) !\r\n" +
                "Please enter username, database name and password in the format: connect|database|username|password\r\n" +
                // list
                "You cannot use the 'list' command until you connect using the command connect|databaseName|userName|password\r\n" +
                "Enter the command (or help for reference):\r\n" +
                // exit
                "Session completed!\r\n", getData());
    }

    @Test
    public void testFindWithoutConnect() {
        // given
        in.add("find|users");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Welcome (привет) !\r\n" +
                "Please enter username, database name and password in the format: connect|database|username|password\r\n" +
                // find|users
                "You cannot use the 'find|users' command until you connect using the command connect|databaseName|userName|password\r\n" +
                "Enter the command (or help for reference):\r\n" +
                // exit
                "Session completed!\r\n", getData());
    }

    @Test
    public void testUnsupported() {
        // given
        in.add("abrakadabra");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Welcome (привет) !\r\n" +
                "Please enter username, database name and password in the format: connect|database|username|password\r\n" +
                // unsupported
                "You cannot use the 'abrakadabra' command until you connect using the command connect|databaseName|userName|password\r\n" +
                "Enter the command (or help for reference):\r\n" +
                // exit
                "Session completed!\r\n", getData());
    }

    @Test
    public void testUnsupportedAfterConnect() {
        // given
        in.add("connect|sqlcmd|postgres|master");
        in.add("abrakadabra");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Welcome (привет) !\r\n" +
                "Please enter username, database name and password in the format: connect|database|username|password\r\n" +
                // connect
                "Connection successful!\r\n" +
                "Enter the command (or help for reference):\r\n" +
                // unsupported
                "Invalid command: abrakadabra\r\n" +
                "Enter the command (or help for reference):\r\n" +
                // exit
                "Session completed!\r\n", getData());
    }

    @Test
    public void testListAfterConnect() {
        // given
        in.add("connect|sqlcmd|postgres|master");
        in.add("list");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Welcome (привет) !\r\n" +
                "Please enter username, database name and password in the format: connect|database|username|password\r\n" +
                // connect
                "Connection successful!\r\n" +
                "Enter the command (or help for reference):\r\n" +
                // list
                "[test, users]\r\n" +
                "Enter the command (or help for reference):\r\n" +
                // exit
                "Session completed!\r\n", getData());
    }

    @Test
    public void testFindAfterConnectWithData() {
        // given
        in.add("connect|sqlcmd|postgres|master");
        in.add("clear|users");
        in.add("create|users|id|1|name|Gerbert|password|111");
        in.add("create|users|id|2|name|John|password|222");
        in.add("find|users");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Welcome (привет) !\r\n" +
                "Please enter username, database name and password in the format: connect|database|username|password\r\n" +
                // connect
                "Connection successful!\r\n" +
                "Enter the command (or help for reference):\r\n" +
                // clear|users
                "Table 'users' was successfully cleared\r\n" +
                "Enter the command (or help for reference):\r\n" +
                // create|users|row1
                "The row {names: [id, name, password], values: [1, Gerbert, 111]} was successfully created in the 'users' table\r\n" +
                "Enter the command (or help for reference):\r\n" +
                // create|users|row2
                "The row {names: [id, name, password], values: [2, John, 222]} was successfully created in the 'users' table\r\n" +
                "Enter the command (or help for reference):\r\n" +
                // find|users
                "------------\r\n" +
                "|name|password|id|\r\n" +
                "------------\r\n" +
                "|Gerbert|111|1|\r\n" +
                "|John|222|2|\r\n" +
                "------------\r\n" +
                "Enter the command (or help for reference):\r\n" +
                // exit
                "Session completed!\r\n", getData());
    }

    @Test
    public void testClearWithError() {
        // given
        in.add("connect|sqlcmd|postgres|master");
        in.add("clear|abrakadabra|tadam");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Welcome (привет) !\r\n" +
                "Please enter username, database name and password in the format: connect|database|username|password\r\n" +
                // connect
                "Connection successful!\r\n" +
                "Enter the command (or help for reference):\r\n" +
                // clear|abrakadabra|tadam
                "failure due to reason: Invalid number of parameters separated by sign '|', expected 2, in stock: 3\r\n" +
                "Try again\r\n" +
                "Enter the command (or help for reference):\r\n" +
                // exit
                "Session completed!\r\n", getData());
    }

    @Test
    public void testConnectAfterConnect() {
        // given
        in.add("connect|sqlcmd|postgres|master");
        in.add("list");
        in.add("connect|test|postgres|master");
        in.add("list");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Welcome (привет) !\r\n" +
                "Please enter username, database name and password in the format: connect|database|username|password\r\n" +
                // connect|sqlcmd
                "Connection successful!\r\n" +
                "Enter the command (or help for reference):\r\n" +
                // list
                "[test, users]\r\n" +
                "Enter the command (or help for reference):\r\n" +
                // connect|test
                "Connection successful!\r\n" +
                "Enter the command (or help for reference):\r\n" +
                // list
                "[test]\r\n" +
                "Enter the command (or help for reference):\r\n" +
                // exit
                "Session completed!\r\n", getData());
    }

    @Test
    public void testConnectWithError() {
        // given
        in.add("connect|sqlcmd");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Welcome (привет) !\r\n" +
                "Please enter username, database name and password in the format: connect|database|username|password\r\n" +
                // connect|sqlcmd
                "failure due to reason: Invalid number of parameters separated by sign '|', expected 4, in stock: 2\r\n" +
                "Try again\r\n" +
                "Enter the command (or help for reference):\r\n" +
                // exit
                "Session completed!\r\n", getData());
    }

    @Test
    public void testCreateWithErrors() {
        // given
        in.add("connect|sqlcmd|postgres|master");
        in.add("create|users|abrakadabra");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Welcome (привет) !\r\n" +
                "Please enter username, database name and password in the format: connect|database|username|password\r\n" +
                // connect
                "Connection successful!\r\n" +
                "Enter the command (or help for reference):\r\n" +
                // create|users
                "failure due to reason: There must be an even number of parameters in format 'create|tableName|column1|value1|...|columnN|valueN', instead there is 'create|users|abrakadabra'\r\n" +
                "Try again\r\n" +
                "Enter the command (or help for reference):\r\n" +
                // exit
                "Session completed!\r\n", getData());
    }
}
