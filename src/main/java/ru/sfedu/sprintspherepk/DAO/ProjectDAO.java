package ru.sfedu.sprintspherepk.DAO;

import org.apache.log4j.Logger;
import ru.sfedu.sprintspherepk.models.Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO {

    private static final Logger log = Logger.getLogger(ProjectDAO.class);
    private final Connection connection;

    public ProjectDAO(Connection connection) {
        this.connection = connection;
    }

    public void createProject(Project project) {
        String query = "INSERT INTO projects(name, description) VALUES (?, ?)";
        log.info("Выполняется запрос на добавление Project: " + project);

        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, project.getName());
            stmt.setString(2, project.getDescription());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        project.setId(generatedKeys.getInt(1));
                    }
                }
            }

            log.info("Project добавлен. ID: " + project.getId());
        } catch (SQLException e) {
            log.error("Ошибка при добавлении Project: " + e.getMessage(), e);
        }
    }

    public Project getProjectById(int id) {
        String query = "SELECT * FROM projects WHERE id = ?";
        log.info("Выполняется запрос для получения Project с ID: " + id);

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Project project = mapResultSetToProject(rs);
                log.info("Project найден: " + project);
                return project;
            } else {
                log.warn("Project с ID " + id + " не найден.");
            }
        } catch (SQLException e) {
            log.error("Ошибка при получении Project с ID: " + id, e);
        }

        return null;
    }

    public List<Project> getAllProjects() {
        String query = "SELECT * FROM projects";
        log.info("Выполняется запрос для получения всех Projects");

        List<Project> projects = new ArrayList<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                projects.add(mapResultSetToProject(rs));
            }
            log.info("Найдено " + projects.size() + " проектов.");
        } catch (SQLException e) {
            log.error("Ошибка при получении списка проектов.", e);
        }

        return projects;
    }

    public void updateProject(Project project) {
        String query = "UPDATE projects SET name = ?, description = ? WHERE id = ?";
        log.info("Выполняется запрос на обновление Project с ID: " + project.getId());

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, project.getName());
            stmt.setString(2, project.getDescription());
            stmt.setInt(3, project.getId());

            int rowsAffected = stmt.executeUpdate();
            log.info("Project обновлен. Затронуто строк: " + rowsAffected);
        } catch (SQLException e) {
            log.error("Ошибка при обновлении Project с ID: " + project.getId(), e);
        }
    }

    public void deleteProject(int id) {
        String query = "DELETE FROM projects WHERE id = ?";
        log.info("Выполняется запрос на удаление Project с ID: " + id);

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            log.info("Project удален. Затронуто строк: " + rowsAffected);
        } catch (SQLException e) {
            log.error("Ошибка при удалении Project с ID: " + id, e);
        }
    }

    private Project mapResultSetToProject(ResultSet rs) throws SQLException {
        Project project = new Project();
        project.setId(rs.getInt("id"));
        project.setName(rs.getString("name"));
        project.setDescription(rs.getString("description"));
        return project;
    }

    public void clearTable() {
        String query = "DELETE FROM projects";
        log.info("Выполняется очистка таблицы projects");

        try (Statement stmt = connection.createStatement()) {
            int rowsAffected = stmt.executeUpdate(query);
            log.info("Таблица projects очищена. Удалено строк: " + rowsAffected);
        } catch (SQLException e) {
            log.error("Ошибка при очистке таблицы projects", e);
        }
    }
}
