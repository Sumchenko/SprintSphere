package ru.sfedu.sprintspherepk.psql;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.sfedu.sprintspherepk.models.HistoryContent;
import ru.sfedu.sprintspherepk.models.Status;

import java.sql.*;
import java.util.Map;

import org.apache.log4j.Logger;

public class DatabaseHandler {

    private static final Logger log = Logger.getLogger(DatabaseHandler.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private Connection connection;

    public DatabaseHandler(String dbUrl, String user, String password) {
        try {
            log.info("Попытка подключения к базе данных...");
            this.connection = DriverManager.getConnection(dbUrl, user, password);
            log.info("Подключение к базе данных успешно установлено.");
        } catch (SQLException e) {
            log.error("Ошибка при установлении подключения к базе данных: " + e.getMessage(), e);
        }
    }

    public void createHistoryContent(HistoryContent content) {
        String query = "INSERT INTO history_content(id, class_name, created_date, actor, method_name, object, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        log.info("Выполняется запрос на добавление HistoryContent с ID: " + content.getId());

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, content.getId());
            stmt.setString(2, content.getClassName());
            stmt.setTimestamp(3, new Timestamp(content.getCreatedDate().getTime()));
            stmt.setString(4, content.getActor());
            stmt.setString(5, content.getMethodName());
            stmt.setString(6, serializeObject(content.getObject()));
            stmt.setString(7, content.getStatus().name());

            int rowsAffected = stmt.executeUpdate();
            log.info("HistoryContent добавлен. Затронуто строк: " + rowsAffected);
        } catch (SQLException e) {
            log.error("Ошибка при добавлении HistoryContent с ID: " + content.getId(), e);
        }
    }

    public HistoryContent getHistoryContentById(String id) {
        String query = "SELECT * FROM history_content WHERE id = ?";
        log.info("Выполняется запрос для получения HistoryContent с ID: " + id);
        HistoryContent content = null;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                content = new HistoryContent();
                content.setId(rs.getString("id"));
                content.setClassName(rs.getString("class_name"));
                content.setCreatedDate(rs.getTimestamp("created_date"));
                content.setActor(rs.getString("actor"));
                content.setMethodName(rs.getString("method_name"));
                content.setObject(deserializeObject(rs.getString("object")));
                content.setStatus(Status.valueOf(rs.getString("status")));

                log.info("HistoryContent успешно получен с ID: " + id);
            } else {
                log.warn("Не найдено HistoryContent с ID: " + id);
            }
        } catch (SQLException e) {
            log.error("Ошибка при получении HistoryContent с ID: " + id, e);
        }

        return content;
    }

    public void updateHistoryContent(HistoryContent content) {
        String query = "UPDATE history_content SET class_name = ?, created_date = ?, actor = ?, method_name = ?, object = ?, status = ? WHERE id = ?";
        log.info("Выполняется запрос на обновление HistoryContent с ID: " + content.getId());

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, content.getClassName());
            stmt.setTimestamp(2, new Timestamp(content.getCreatedDate().getTime()));
            stmt.setString(3, content.getActor());
            stmt.setString(4, content.getMethodName());
            stmt.setString(5, serializeObject(content.getObject()));
            stmt.setString(6, content.getStatus().name());
            stmt.setString(7, content.getId());

            int rowsAffected = stmt.executeUpdate();
            log.info("HistoryContent обновлен. Затронуто строк: " + rowsAffected);
        } catch (SQLException e) {
            log.error("Ошибка при обновлении HistoryContent с ID: " + content.getId(), e);
        }
    }

    public void deleteHistoryContent(String id) {
        String query = "DELETE FROM history_content WHERE id = ?";
        log.info("Выполняется запрос на удаление HistoryContent с ID: " + id);

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, id);
            int rowsAffected = stmt.executeUpdate();
            log.info("HistoryContent удален. Затронуто строк: " + rowsAffected);
        } catch (SQLException e) {
            log.error("Ошибка при удалении HistoryContent с ID: " + id, e);
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                log.info("Закрытие подключения к базе данных...");
                connection.close();
                log.info("Подключение к базе данных успешно закрыто.");
            }
        } catch (SQLException e) {
            log.error("Ошибка при закрытии подключения к базе данных: " + e.getMessage(), e);
        }
    }

    // Метод для сериализации объекта Map в строку (например, JSON)
    private String serializeObject(Map<String, Object> object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            log.error("Ошибка при сериализации объекта: " + e.getMessage(), e);
            return null;
        }
    }

    // Метод для десериализации строки в объект Map
    private Map<String, Object> deserializeObject(String objectStr) {
        try {
            return objectMapper.readValue(objectStr, Map.class);
        } catch (Exception e) {
            log.error("Ошибка при десериализации объекта: " + e.getMessage(), e);
            return null;
        }
    }
}
