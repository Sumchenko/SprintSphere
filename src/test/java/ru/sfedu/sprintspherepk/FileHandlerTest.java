package ru.sfedu.sprintspherepk;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import ru.sfedu.sprintspherepk.api.FileHandler;
import ru.sfedu.sprintspherepk.exceptions.MyException;

public class FileHandlerTest {

    private static final Logger logger = Logger.getLogger(FileHandler.class.getName());

    @Test
    public void checkValidFileName() {
        logger.info("Начало теста: checkValidFileName");
        FileHandler fileHandler = new FileHandler();
        fileHandler.uploadFile("src/test/resources/validFile.txt");
        logger.info("Тест checkValidFileName завершен успешно");
    }


    @Test
    public void checkInvalidFileName() {
        logger.info("Начало теста: checkInvalidFileName");
        FileHandler fileHandler = new FileHandler();
        try {
            fileHandler.uploadFile("src/test/resources/invalidFile.txt");
            Assert.fail("Исключение MyException не было выброшено!");
        } catch (MyException e) {
            logger.info("Исключение MyException успешно перехвачено");
            Assert.assertEquals("Файл не найден: src/test/resources/invalidFile.txt", e.getMessage());
        }
    }
}
