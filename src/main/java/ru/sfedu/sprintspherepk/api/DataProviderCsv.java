package ru.sfedu.sprintspherepk.api;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import ru.sfedu.sprintspherepk.models.HistoryContent;
import ru.sfedu.sprintspherepk.models.Status;

import java.io.*;
import java.util.*;

public class DataProviderCsv implements IDataProvider {

    private static final String FILE_NAME = "data.csv";

    @Override
    public void initDataSource() throws Exception {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_NAME))) {
                // Записываем заголовки файла
                String[] header = {"id", "className", "createdDate", "actor", "methodName", "status", "object"};
                writer.writeNext(header);
            } catch (IOException e) {
                throw new IOException("Ошибка при создании CSV файла: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void saveRecord(HistoryContent record) throws Exception {
        try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_NAME, true))) {
            String[] data = {
                    record.getId(),
                    record.getClassName(),
                    String.valueOf(record.getCreatedDate().getTime()),
                    record.getActor(),
                    record.getMethodName(),
                    record.getStatus().name(),
                    mapToString(record.getObject())
            };
            writer.writeNext(data);
        } catch (IOException e) {
            throw new IOException("Ошибка при сохранении записи в CSV: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<HistoryContent> getRecordById(String id) throws Exception {
        try (CSVReader reader = new CSVReader(new FileReader(FILE_NAME))) {
            String[] line;
            reader.readNext(); // Пропускаем заголовок
            while ((line = reader.readNext()) != null) {
                if (line[0].equals(id)) {
                    return Optional.of(parseRecord(line));
                }
            }
        } catch (IOException e) {
            throw new IOException("Ошибка при чтении CSV файла: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public void deleteRecord(String id) throws Exception {
        File inputFile = new File(FILE_NAME);
        File tempFile = new File("temp.csv");

        try (CSVReader reader = new CSVReader(new FileReader(inputFile));
             CSVWriter writer = new CSVWriter(new FileWriter(tempFile))) {

            String[] line;
            writer.writeNext(reader.readNext());

            while ((line = reader.readNext()) != null) {
                if (!line[0].equals(id)) {
                    writer.writeNext(line);
                }
            }
        } catch (IOException e) {
            throw new IOException("Ошибка при чтении или записи в CSV файл: " + e.getMessage(), e);
        }

        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            throw new IOException("Не удалось удалить или переименовать временный файл.");
        }
    }

    private HistoryContent parseRecord(String[] data) {
        HistoryContent record = new HistoryContent();
        record.setId(data[0]);
        record.setClassName(data[1]);
        record.setCreatedDate(new Date(Long.parseLong(data[2])));
        record.setActor(data[3]);
        record.setMethodName(data[4]);
        record.setStatus(Status.valueOf(data[5]));
        record.setObject(stringToMap(data[6]));
        return record;
    }

    private String mapToString(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        map.forEach((key, value) -> sb.append(key).append("=").append(value).append(","));
        sb.setLength(sb.length() - 1); // Убираем последний запятую
        return sb.toString();
    }

    private Map<String, String> stringToMap(String data) {
        Map<String, String> map = new HashMap<>();
        if (data == null || data.isEmpty()) {
            return map;
        }
        String[] entries = data.split(",");
        for (String entry : entries) {
            String[] keyValue = entry.split("=");
            if (keyValue.length == 2) {
                map.put(keyValue[0], keyValue[1]);
            }
        }
        return map;
    }
}


