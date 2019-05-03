package ua.com.juja.sqlcmd.integration;

import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.juja.sqlcmd.controller.Main;
import java.io.PrintStream;
import static junit.framework.TestCase.assertEquals;

public class IntegrationTest {

    private static ConfigurableInputStream in;
    private static LogOutputStream out;

    @BeforeClass
    public static void setup() {
        in = new ConfigurableInputStream();
        out = new LogOutputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

    @Test
    public void testExit() {
        // given
        in.add("help");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("", out.getData());
    }
}
