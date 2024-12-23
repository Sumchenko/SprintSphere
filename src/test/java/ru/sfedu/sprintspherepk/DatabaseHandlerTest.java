package ru.sfedu.sprintspherepk;

import org.junit.Before;
import org.junit.Test;
import ru.sfedu.sprintspherepk.models.HistoryContent;
import ru.sfedu.sprintspherepk.models.Status;
import ru.sfedu.sprintspherepk.psql.Config;
import ru.sfedu.sprintspherepk.psql.DatabaseHandler;

import static org.junit.Assert.*;

public class DatabaseHandlerTest {
    private DatabaseHandler handler;

    @Before
    public void setUp() {
        handler = new DatabaseHandler(Config.getDbUrl(), Config.getDbUser(), Config.getDbPassword());
    }

    @Test
    public void testCreateHistoryContent() {
        HistoryContent content = new HistoryContent();
        content.setClassName("TestClass");
        content.setActor("TestActor");
        content.setMethodName("TestMethod");
        content.setStatus(Status.SUCCESS);

        handler.createHistoryContent(content);

        HistoryContent retrieved = handler.getHistoryContentById(content.getId());
        assertNotNull(retrieved);
        assertEquals(content.getId(), retrieved.getId());
    }

    @Test
    public void testUpdateHistoryContent() {
        HistoryContent content = new HistoryContent();
        content.setClassName("TestClass");
        content.setActor("TestActor");
        content.setMethodName("TestMethod");
        content.setStatus(Status.SUCCESS);

        handler.createHistoryContent(content);

        content.setClassName("UpdatedClass");
        handler.updateHistoryContent(content);

        HistoryContent updated = handler.getHistoryContentById(content.getId());
        assertEquals("UpdatedClass", updated.getClassName());
    }

    @Test
    public void testDeleteHistoryContent() {
        HistoryContent content = new HistoryContent();
        content.setClassName("TestClass");
        content.setActor("TestActor");
        content.setMethodName("TestMethod");
        content.setStatus(Status.SUCCESS);

        handler.createHistoryContent(content);
        handler.deleteHistoryContent(content.getId());

        HistoryContent deleted = handler.getHistoryContentById(content.getId());
        assertNull(deleted);
    }
}