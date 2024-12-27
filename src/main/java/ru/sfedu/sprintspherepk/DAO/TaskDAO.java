package ru.sfedu.sprintspherepk.DAO;

import org.apache.log4j.Logger;
import ru.sfedu.sprintspherepk.models.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    private static final Logger log = Logger.getLogger(TaskDAO.class);
    private final Connection connection;

    public TaskDAO(Connection connection) {
        this.connection = connection;
    }

    public void createTask(Task task) {
        String query = "INSERT INTO tasks(title, description, status, priority) VALUES (?, ?, ?, ?)";
        log.info("Выполняется запрос на добавление Task: " + task);

        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setString(3, task.getStatus());
            stmt.setInt(4, task.getPriority());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        task.setId(generatedKeys.getInt(1));
                    }
                }
            }

            log.info("Task добавлен. ID: " + task.getId());
        } catch (SQLException e) {
            log.error("Ошибка при добавлении Task: " + e.getMessage(), e);
        }
    }

    public Task getTaskById(int id) {
        String query = "SELECT * FROM tasks WHERE id = ?";
        log.info("Выполняется запрос для получения Task с ID: " + id);

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Task task = mapResultSetToTask(rs);
                log.info("Task найден: " + task);
                return task;
            } else {
                log.warn("Task с ID " + id + " не найден.");
            }
        } catch (SQLException e) {
            log.error("Ошибка при получении Task с ID: " + id, e);
        }

        return null;
    }

    public List<Task> getAllTasks() {
        String query = "SELECT * FROM tasks";
        log.info("Выполняется запрос для получения всех Tasks");

        List<Task> tasks = new ArrayList<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                tasks.add(mapResultSetToTask(rs));
            }
            log.info("Найдено " + tasks.size() + " задач.");
        } catch (SQLException e) {
            log.error("Ошибка при получении списка задач.", e);
        }

        return tasks;
    }

    public void updateTask(Task task) {
        String query = "UPDATE tasks SET title = ?, description = ?, status = ?, priority = ? WHERE id = ?";
        log.info("Выполняется запрос на обновление Task с ID: " + task.getId());

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setString(3, task.getStatus());
            stmt.setInt(4, task.getPriority());
            stmt.setInt(5, task.getId());

            int rowsAffected = stmt.executeUpdate();
            log.info("Task обновлен. Затронуто строк: " + rowsAffected);
        } catch (SQLException e) {
            log.error("Ошибка при обновлении Task с ID: " + task.getId(), e);
        }
    }

    public void deleteTask(int id) {
        String query = "DELETE FROM tasks WHERE id = ?";
        log.info("Выполняется запрос на удаление Task с ID: " + id);

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            log.info("Task удален. Затронуто строк: " + rowsAffected);
        } catch (SQLException e) {
            log.error("Ошибка при удалении Task с ID: " + id, e);
        }
    }

    private Task mapResultSetToTask(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setId(rs.getInt("id"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setStatus(rs.getString("status"));
        task.setPriority(rs.getInt("priority"));
        return task;
    }

    public void clearTable() {
        String query = "DELETE FROM tasks";
        log.info("Выполняется очистка таблицы tasks");

        try (Statement stmt = connection.createStatement()) {
            int rowsAffected = stmt.executeUpdate(query);
            log.info("Таблица tasks очищена. Удалено строк: " + rowsAffected);
        } catch (SQLException e) {
            log.error("Ошибка при очистке таблицы tasks", e);
        }
    }
}
