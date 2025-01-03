package ru.sfedu.sprintspherepk.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.simpleframework.xml.core.Persister;
import ru.sfedu.sprintspherepk.models.HistoryContent;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DataProviderXml implements IDataProvider {

    private static final String FILE_NAME = "data.xml";

    private final Persister persister = new Persister();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void initDataSource() throws Exception {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new IOException("Ошибка при создании XML файла: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void saveRecord(HistoryContent record) throws Exception {
        try {
            if (record.getObject() == null) {  // Обработка null
                record.setObject(new HashMap<>());
            }

            String jsonObject = objectMapper.writeValueAsString(record.getObject());
            record.setObject(Map.of("json", jsonObject));

            persister.write(record, new File(FILE_NAME));
        } catch (Exception e) {
            throw new Exception("Ошибка при записи записи в XML файл: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<HistoryContent> getRecordById(String id) throws Exception {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            HistoryContent record = persister.read(HistoryContent.class, file);
            if (record.getId().equals(id)) {
                String jsonString = (String) record.getObject().get("json");
                Map<String, Object> objectMap = objectMapper.readValue(jsonString, new TypeReference<>() {});
                record.setObject(objectMap);
                return Optional.of(record);
            }
        }
        return Optional.empty();
    }

    @Override
    public void deleteRecord(String id) throws Exception {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try {
                HistoryContent record = persister.read(HistoryContent.class, file);
                if (record.getId().equals(id)) {
                    if (!file.delete()) {
                        throw new IOException("Не удалось удалить XML файл.");
                    }
                }
            } catch (Exception e) {
                throw new Exception("Ошибка при удалении записи из XML файла: " + e.getMessage(), e);
            }
        }
    }
}
