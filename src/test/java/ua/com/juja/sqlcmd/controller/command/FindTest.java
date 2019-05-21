package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static junit.framework.TestCase.*;
import static org.mockito.Mockito.*;
import ua.com.juja.sqlcmd.model.DataBaseManager;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.view.View;

import javax.lang.model.util.Types;

public class FindTest {

    private View view;
    private DataBaseManager manager;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DataBaseManager.class);
        view = mock(View.class);
        command = new Find(manager, view);
    }

    @Test
    public void testPrintTableData() {
        // given
        when(manager.getTableColumns("users")).thenReturn(new String[] {"id", "name", "password"});

        DataSet user1 = new DataSet(3);
        user1.put("id", "1");
        user1.put("name", "Gerbert");
        user1.put("password", "111");

        DataSet user2 = new DataSet(3);
        user2.put("id", "2");
        user2.put("name", "John");
        user2.put("password", "222");

        DataSet[] data = new DataSet[] {user1, user2};
        when(manager.getTableData("users")).thenReturn(data);
        // when
        command.process("find|users");
        // then
        shouldPrint("[------------, " +
                            "|id|name|password|, " +
                            "------------, " +
                            "|1|Gerbert|111|, " +
                            "|2|John|222|, " +
                            "------------]");
    }

    @Test
    public void testPrintTableDataWithOneColumn() {
        // given
        when(manager.getTableColumns("test")).thenReturn(new String[] {"name"});

        DataSet user1 = new DataSet(1);
        user1.put("name", "Gerbert");

        DataSet user2 = new DataSet(1);
        user2.put("name", "John");

        DataSet[] data = new DataSet[] {user1, user2};
        when(manager.getTableData("test")).thenReturn(data);
        // when
        command.process("find|test");
        // then
        shouldPrint("[------------, " +
                            "|name|, " +
                            "------------, " +
                            "|Gerbert|, " +
                            "|John|, " +
                            "------------]");
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }

    @Test
    public void TestCanProcessFindString() {
        // when
        boolean canProcess = command.canProcess("find|users");
        // then
        assertTrue(canProcess);
    }

    @Test
    public void TestCannotProcessFindWithParametersString() {
        // when
        boolean canProcess = command.canProcess("find");
        // then
        assertFalse(canProcess);
    }

    @Test
    public void TestCannotProcessFindString() {
        // when
        boolean canProcess = command.canProcess("abrakadabra|users");
        // then
        assertFalse(canProcess);
    }

    @Test
    public void testPrintEmptyTableData() {
        // given
        when(manager.getTableColumns("users")).thenReturn(new String[] {"id", "name", "password"});

        DataSet[] data = new DataSet[0];
        when(manager.getTableData("users")).thenReturn(data);
        // when
        command.process("find|users");
        // then
        shouldPrint("[------------, " +
                            "|id|name|password|, " +
                            "------------, " +
                            "------------]");
    }

}
