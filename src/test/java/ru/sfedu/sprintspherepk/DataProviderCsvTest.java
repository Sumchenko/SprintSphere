package ru.sfedu.sprintspherepk;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import ru.sfedu.sprintspherepk.models.HistoryContent;
import ru.sfedu.sprintspherepk.models.Status;
import ru.sfedu.sprintspherepk.api.DataProviderCsv;
import ru.sfedu.sprintspherepk.api.IDataProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

public class DataProviderCsvTest {

    private static final Logger LOGGER = Logger.getLogger(DataProviderCsvTest.class.getName());

    private IDataProvider csvProvider;

    @Before
    public void setUp() throws Exception {
        LOGGER.info("Инициализация поставщика данных для CSV...");
        csvProvider = new DataProviderCsv();

        LOGGER.info("Инициализация источника данных для CSV поставщика...");
        csvProvider.initDataSource();
        LOGGER.info("Источник данных для CSV инициализирован.");
    }

    @Test
    public void testCsvProvider() throws Exception {
        LOGGER.info("Начало тестирования CSV поставщика...");

        HistoryContent record = new HistoryContent();
        record.setClassName("Test");
        record.setActor("User");
        record.setMethodName("TestMethod");
        record.setStatus(Status.SUCCESS);

        Map<String, String> objectData = new HashMap<>();
        objectData.put("key1", "value1");
        objectData.put("key2", "value2");
        record.setObject(objectData);


        LOGGER.info("Сохранение записи в CSV...");
        csvProvider.saveRecord(record);
        LOGGER.info("Запись сохранена с ID: " + record.getId());

        LOGGER.info("Извлечение записи по ID из CSV...");
        Optional<HistoryContent> retrieved = csvProvider.getRecordById(record.getId());
        assertTrue(retrieved.isPresent());
        assertEquals(record.getClassName(), retrieved.get().getClassName());
        assertEquals(record.getObject(), retrieved.get().getObject());
        LOGGER.info("Запись успешно извлечена.");

        LOGGER.info("Удаление записи из CSV...");
        csvProvider.deleteRecord(record.getId());
        assertFalse(csvProvider.getRecordById(record.getId()).isPresent());
        LOGGER.info("Запись успешно удалена.");
    }
}
