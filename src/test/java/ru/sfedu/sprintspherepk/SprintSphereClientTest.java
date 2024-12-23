package ru.sfedu.sprintspherepk;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import static org.junit.Assert.*;

public class SprintSphereClientTest {
    private static final Logger log = Logger.getLogger(SprintSphereClientTest.class);

    @Before
    public void setUp() {
        log.info("Setting up test environment...");
    }

    @Test
    public void testLogging() {
        // Создаем экземпляр класса и вызываем метод для логирования
        SprintSphereClient client = new SprintSphereClient();
        client.logBasicSystemInfo();

        // Проверяем, происходят ли записи в лог
        File logFile = new File("logs/application.log");
        assertTrue("Log file should exist.", logFile.exists());

    }

}
