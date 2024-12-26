package ru.sfedu.sprintspherepk;

import org.junit.Before;
import org.junit.Test;
import ru.sfedu.sprintspherepk.models.HistoryContent;
import ru.sfedu.sprintspherepk.models.Status;
import ru.sfedu.sprintspherepk.psql.Config;
import ru.sfedu.sprintspherepk.psql.DatabaseHandler;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class DatabaseHandlerTest {
    private DatabaseHandler handler;

    @Before
    public void setUp() {
        handler = new DatabaseHandler(Config.getDbUrl(), Config.getDbUser(), Config.getDbPassword());
    }

    @Test
    public void testCreateHistoryContent() {
        Map<String, Object> object = new HashMap<>();
        object.put("key1", "value1");
        object.put("key2", 123);

        HistoryContent content = new HistoryContent();
        content.setClassName("TestClass");
        content.setActor("TestActor");
        content.setMethodName("TestMethod");
        content.setStatus(Status.SUCCESS);
        content.setObject(object);

        handler.createHistoryContent(content);

        HistoryContent retrieved = handler.getHistoryContentById(content.getId());
        assertNotNull(retrieved);
        assertEquals(content.getId(), retrieved.getId());

        assertEquals(content.getClassName(), retrieved.getClassName());
        assertEquals(content.getActor(), retrieved.getActor());
        assertEquals(content.getMethodName(), retrieved.getMethodName());
        assertEquals(content.getStatus(), retrieved.getStatus());

        assertNotNull(retrieved.getObject());
        assertEquals(object, retrieved.getObject());
    }

    @Test
    public void testUpdateHistoryContent() {
        Map<String, Object> object = new HashMap<>();
        object.put("key1", "value1");

        HistoryContent content = new HistoryContent();
        content.setClassName("TestClass");
        content.setActor("TestActor");
        content.setMethodName("TestMethod");
        content.setStatus(Status.SUCCESS);
        content.setObject(object);

        handler.createHistoryContent(content);

        object.put("key2", "value2");
        content.setClassName("UpdatedClass");
        content.setObject(object);

        handler.updateHistoryContent(content);

        HistoryContent updated = handler.getHistoryContentById(content.getId());
        assertNotNull(updated);
        assertEquals("UpdatedClass", updated.getClassName());

        assertNotNull(updated.getObject());
        assertEquals(object, updated.getObject());
    }

    @Test
    public void testDeleteHistoryContent() {
        Map<String, Object> object = new HashMap<>();
        object.put("key1", "value1");

        HistoryContent content = new HistoryContent();
        content.setClassName("TestClass");
        content.setActor("TestActor");
        content.setMethodName("TestMethod");
        content.setStatus(Status.SUCCESS);
        content.setObject(object);

        handler.createHistoryContent(content);

        handler.deleteHistoryContent(content.getId());

        HistoryContent deleted = handler.getHistoryContentById(content.getId());
        assertNull(deleted);
    }
}
