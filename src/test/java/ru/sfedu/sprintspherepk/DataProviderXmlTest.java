package ru.sfedu.sprintspherepk;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import ru.sfedu.sprintspherepk.models.HistoryContent;
import ru.sfedu.sprintspherepk.models.Status;
import ru.sfedu.sprintspherepk.api.DataProviderXml;
import ru.sfedu.sprintspherepk.api.IDataProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

public class DataProviderXmlTest {

    private static final Logger LOGGER = Logger.getLogger(DataProviderXmlTest.class.getName());

    private IDataProvider xmlProvider;

    @Before
    public void setUp() throws Exception {
        LOGGER.info("Инициализация поставщика данных для XML...");
        xmlProvider = new DataProviderXml();

        LOGGER.info("Инициализация источника данных для XML поставщика...");
        xmlProvider.initDataSource();
        LOGGER.info("Источник данных для XML инициализирован.");
    }

    @Test
    public void testXmlProvider() throws Exception {
        LOGGER.info("Начало тестирования XML поставщика...");

        HistoryContent record = new HistoryContent();
        record.setClassName("Test");
        record.setActor("User");

        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("key1", "value1");
        objectMap.put("key2", 12345);
        objectMap.put("key3", 67.89);
        record.setObject(objectMap);
        record.setMethodName("TestMethod");
        record.setStatus(Status.SUCCESS);

        LOGGER.info("Сохранение записи в XML...");
        xmlProvider.saveRecord(record);
        LOGGER.info("Запись сохранена с ID: " + record.getId());

        LOGGER.info("Извлечение записи по ID из XML...");
        Optional<HistoryContent> retrieved = xmlProvider.getRecordById(record.getId());
        assertTrue(retrieved.isPresent());
        assertNotNull(retrieved.get().getObject());
        assertEquals("value1", retrieved.get().getObject().get("key1"));
        assertEquals(12345, retrieved.get().getObject().get("key2"));
        LOGGER.info("Запись успешно извлечена.");

        LOGGER.info("Удаление записи из XML...");
        xmlProvider.deleteRecord(record.getId());
        assertFalse(xmlProvider.getRecordById(record.getId()).isPresent());
        LOGGER.info("Запись успешно удалена.");
    }
}
