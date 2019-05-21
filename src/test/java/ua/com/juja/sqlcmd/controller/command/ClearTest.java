package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.sqlcmd.model.DataBaseManager;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.view.View;

import static junit.framework.TestCase.*;
import static org.mockito.Mockito.*;

public class ClearTest {

    private View view;
    private DataBaseManager manager;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DataBaseManager.class);
        view = mock(View.class);
        command = new Clear(manager, view);
    }

    @Test
    public void testClearTable() {
        // given

        // when
        command.process("clear|users");
        // then
        verify(manager).clear("users");
        verify(view).write("Table 'users' was successfully cleared");
    }

    @Test
    public void testValidationErrorWhenCountParametersIsLessThan2() {
        // when
        try {
            command.process("clear");
            fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Invalid number of parameters separated by sign '|', " +
                    "expected 2, in stock: clear", e.getMessage());
        }
    }

    @Test
    public void testValidationErrorWhenCountParametersIsMoreThan2() {
        // when
        try {
            command.process("clear|users|abrakadabra");
            fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Invalid number of parameters separated by sign '|', " +
                    "expected 2, in stock: clear|users|abrakadabra", e.getMessage());
        }
    }

    @Test
    public void TestCanProcessClearString() {
        // when
        boolean canProcess = command.canProcess("clear|users");
        // then
        assertTrue(canProcess);
    }

    @Test
    public void TestCannotProcessClearString() {
        // when
        boolean canProcess = command.canProcess("abrakadabra");
        // then
        assertFalse(canProcess);
    }

    @Test
    public void TestCannotProcessClearWithParametersString() {
        // when
        boolean canProcess = command.canProcess("clear");
        // then
        assertFalse(canProcess);
    }

}
