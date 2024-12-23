package ru.sfedu.sprintspherepk.psql;

import ru.sfedu.sprintspherepk.models.HistoryContent;
import ru.sfedu.sprintspherepk.models.Status;

import java.sql.*;
import java.util.Map;

import org.apache.log4j.Logger;

public class DatabaseHandler {

    private static final Logger log = Logger.getLogger(DatabaseHandler.class); // Исправлено на правильный класс для логирования
    private Connection connection;

    // Конструктор для инициализации подключения
    public DatabaseHandler(String dbUrl, String user, String password) {
        try {
            log.info("Попытка подключения к базе данных...");
            this.connection = DriverManager.getConnection(dbUrl, user, password);
            log.info("Подключение к базе данных успешно установлено.");
        } catch (SQLException e) {
            log.error("Ошибка при установлении подключения к базе данных: " + e.getMessage(), e);
        }
    }

    // Метод для создания нового объекта HistoryContent в базе данных
    public void createHistoryContent(HistoryContent content) {
        String query = "INSERT INTO history_content(id, class_name, created_date, actor, method_name, object, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        log.info("Выполняется запрос на добавление HistoryContent с ID: " + content.getId());

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, content.getId());
            stmt.setString(2, content.getClassName());
            stmt.setTimestamp(3, new Timestamp(content.getCreatedDate().getTime()));
            stmt.setString(4, content.getActor());
            stmt.setString(5, content.getMethodName());
            stmt.setObject(6, content.getObject());  // Обработка Map может потребовать дополнительных шагов
            stmt.setString(7, content.getStatus().name());

            int rowsAffected = stmt.executeUpdate();
            log.info("HistoryContent добавлен. Затронуто строк: " + rowsAffected);
        } catch (SQLException e) {
            log.error("Ошибка при добавлении HistoryContent с ID: " + content.getId(), e);
        }
    }

    // Метод для получения объекта HistoryContent по ID
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
                content.setObject((Map<String, String>) rs.getObject("object"));  // Обработка Map
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

    // Метод для обновления объекта HistoryContent
    public void updateHistoryContent(HistoryContent content) {
        String query = "UPDATE history_content SET class_name = ?, created_date = ?, actor = ?, method_name = ?, object = ?, status = ? WHERE id = ?";
        log.info("Выполняется запрос на обновление HistoryContent с ID: " + content.getId());

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, content.getClassName());
            stmt.setTimestamp(2, new Timestamp(content.getCreatedDate().getTime()));
            stmt.setString(3, content.getActor());
            stmt.setString(4, content.getMethodName());
            stmt.setObject(5, content.getObject());  // Обработка Map
            stmt.setString(6, content.getStatus().name());
            stmt.setString(7, content.getId());

            int rowsAffected = stmt.executeUpdate();
            log.info("HistoryContent обновлен. Затронуто строк: " + rowsAffected);
        } catch (SQLException e) {
            log.error("Ошибка при обновлении HistoryContent с ID: " + content.getId(), e);
        }
    }

    // Метод для удаления объекта HistoryContent по ID
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

    // Метод для закрытия подключения
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
}
