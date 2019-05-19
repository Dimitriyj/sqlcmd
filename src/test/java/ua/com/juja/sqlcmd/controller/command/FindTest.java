package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;
import ua.com.juja.sqlcmd.model.DataBaseManager;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.view.View;

import javax.lang.model.util.Types;

import static junit.framework.TestCase.assertEquals;

public class FindTest {

    private View view;
    private DataBaseManager manager;

    @Before
    public void setup() {
        manager = mock(DataBaseManager.class);
        view = mock(View.class);
    }

    @Test
    public void testPrintTableData() {
        // given
        Command command = new Find(manager, view);
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
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(
                "[------------, " +
                        "|id|name|password|, " +
                        "------------, " +
                        "|1|Gerbert|111|, " +
                        "|2|John|222|, " +
                        "------------]", captor.getAllValues().toString());
    }
}
