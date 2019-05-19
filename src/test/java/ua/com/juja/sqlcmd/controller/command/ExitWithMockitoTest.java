package ua.com.juja.sqlcmd.controller.command;

import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.sqlcmd.view.View;

import static junit.framework.TestCase.*;

public class ExitWithMockitoTest {

    private View view = Mockito.mock(View.class);

    @Test
    public void TestCanProcessExitString() {
        // given
        Command command = new Exit(view);
        // when
        boolean canProcess = command.canProcess("exit");
        // then
        assertTrue(canProcess);
    }
    @Test
    public void TestCannotProcessExitString() {
        // given
        Command command = new Exit(view);
        // when
        boolean canProcess = command.canProcess("abrakadabra");
        // then
        assertFalse(canProcess);
    }
    @Test()
    public void TestCanProcessExitCommand_throwExitExept() {
        // given
        Command command = new Exit(view);
        // when
        try {
            command.process("exit");
            fail("Expected ExitExcept");
        } catch (ExitExcept e) {
            // do nothing
        }
        // then
        Mockito.verify(view).write("Session completed!");
    }
}
