package ru.sfedu.sprintspherepk.api;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.apache.log4j.Logger;
import ru.sfedu.sprintspherepk.models.HistoryContent;
import ru.sfedu.sprintspherepk.models.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MongoDBUtil {

    private static final Logger log = Logger.getLogger(MongoDBUtil.class);
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "HistoryDB";

    private MongoClient mongoClient;
    private MongoDatabase database;

    // подключение
    public MongoDBUtil() {
        try {
            mongoClient = MongoClients.create(CONNECTION_STRING);
            this.database = mongoClient.getDatabase(DATABASE_NAME);
            log.info("Успешно подключено к базе данных MongoDB: " + DATABASE_NAME);
        } catch (Exception e) {
            log.error("Ошибка при подключении к MongoDB", e);
            throw new RuntimeException("Не удалось подключиться к MongoDB", e);
        }
    }

    // Получение коллекции "history_content"
    private MongoCollection<Document> getCollection() {
        try {
            return database.getCollection("history_content");
        } catch (Exception e) {
            log.error("Ошибка при получении коллекции MongoDB: history_content", e);
            throw new RuntimeException("Не удалось получить коллекцию MongoDB", e);
        }
    }

    private Document toDocument(HistoryContent history) {
        try {
            return new Document("id", history.getId())
                    .append("className", history.getClassName())
                    .append("createdDate", history.getCreatedDate())
                    .append("actor", history.getActor())
                    .append("methodName", history.getMethodName())
                    .append("object", history.getObject())
                    .append("status", history.getStatus());
        } catch (Exception e) {
            log.error("Ошибка при преобразовании HistoryContent в Document", e);
            throw new RuntimeException("Ошибка при преобразовании HistoryContent в Document", e);
        }
    }

    private HistoryContent fromDocument(Document doc) {
        try {
            HistoryContent history = new HistoryContent();
            history.setId(doc.getString("id"));
            history.setClassName(doc.getString("className"));
            history.setCreatedDate(doc.getDate("createdDate"));
            history.setActor(doc.getString("actor"));
            history.setMethodName(doc.getString("methodName"));
            history.setObject(doc.get("object", Map.class));
            history.setStatus(Status.valueOf(doc.getString("status")));
            return history;
        } catch (Exception e) {
            log.error("Ошибка при преобразовании Document в HistoryContent", e);
            throw new RuntimeException("Ошибка при преобразовании Document в HistoryContent", e);
        }
    }

    public void save(HistoryContent history) {
        try {
            MongoCollection<Document> collection = getCollection();
            collection.insertOne(toDocument(history));
            log.info("Сохранен HistoryContent с ID: " + history.getId());
        } catch (Exception e) {
            log.error("Ошибка при сохранении HistoryContent", e);
            throw new RuntimeException("Ошибка при сохранении HistoryContent", e);
        }
    }

    public List<HistoryContent> findByActor(String actor) {
        try {
            MongoCollection<Document> collection = getCollection();
            List<HistoryContent> results = new ArrayList<>();
            collection.find(Filters.eq("actor", actor))
                    .forEach(doc -> results.add(fromDocument(doc)));
            log.info("Найдено " + results.size() + " записей HistoryContent для актера: " + actor);
            return results;
        } catch (Exception e) {
            log.error("Ошибка при поиске HistoryContent по актеру", e);
            throw new RuntimeException("Ошибка при поиске HistoryContent по актеру", e);
        }
    }

    public Optional<HistoryContent> findById(String id) {
        try {
            MongoCollection<Document> collection = getCollection();
            Document doc = collection.find(Filters.eq("id", id)).first();
            log.info("Найден HistoryContent с ID: " + id);
            return Optional.ofNullable(doc).map(this::fromDocument);
        } catch (Exception e) {
            log.error("Ошибка при поиске HistoryContent по ID", e);
            throw new RuntimeException("Ошибка при поиске HistoryContent по ID", e);
        }
    }

    public void update(String id, HistoryContent updatedHistory) {
        try {
            MongoCollection<Document> collection = getCollection();
            Document query = new Document("id", id);
            Document update = new Document("$set", new Document("className", updatedHistory.getClassName())
                    .append("createdDate", updatedHistory.getCreatedDate())
                    .append("actor", updatedHistory.getActor())
                    .append("methodName", updatedHistory.getMethodName())
                    .append("object", updatedHistory.getObject())
                    .append("status", updatedHistory.getStatus()));
            collection.updateOne(query, update);
            log.info("Обновлен HistoryContent с ID: " + id);
        } catch (Exception e) {
            log.error("Ошибка при обновлении HistoryContent с ID: " + id, e);
            throw new RuntimeException("Ошибка при обновлении HistoryContent", e);
        }
    }

    public void delete(String id) {
        try {
            MongoCollection<Document> collection = getCollection();
            collection.deleteOne(Filters.eq("id", id));
            log.info("Удален HistoryContent с ID: " + id);
        } catch (Exception e) {
            log.error("Ошибка при удалении HistoryContent с ID: " + id, e);
            throw new RuntimeException("Ошибка при удалении HistoryContent", e);
        }
    }

    public void close() {
        try {
            if (mongoClient != null) {
                mongoClient.close();
                log.info("Соединение с MongoDB закрыто.");
            }
        } catch (Exception e) {
            log.error("Ошибка при закрытии соединения с MongoDB", e);
        }
    }

}
