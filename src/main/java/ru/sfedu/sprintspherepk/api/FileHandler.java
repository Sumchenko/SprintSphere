package ru.sfedu.sprintspherepk.api;

import org.apache.log4j.Logger;
import ru.sfedu.sprintspherepk.exceptions.MyException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileHandler {

    private static final Logger logger = Logger.getLogger(FileHandler.class.getName());

    public void uploadFile(String fileName) {
        logger.info("Попытка загрузить файл: " + fileName);
        try (FileInputStream stream = new FileInputStream(fileName)) {
            logger.info("Файл успешно загружен: " + fileName);
        } catch (FileNotFoundException e) {
            throw new MyException("Файл не найден: " + fileName, e);
        } catch (IOException e) {
            throw new MyException("Ошибка ввода-вывода при работе с файлом: " + fileName, e);
        }
    }
}
