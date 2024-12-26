package ru.sfedu.sprintspherepk.DAO;

import org.apache.log4j.Logger;
import ru.sfedu.sprintspherepk.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private static final Logger log = Logger.getLogger(UserDAO.class);
    private final Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public void createUser(User user) {
        String query = "INSERT INTO users(name, email, bio, count_project, avatar_url, is_active, last_login, date_joined) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        log.info("Выполняется запрос на добавление User: " + user);

        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getBio());
            stmt.setInt(4, user.getCountProject());
            stmt.setString(5, user.getAvatarURL());
            stmt.setBoolean(6, user.isActive());
            stmt.setTimestamp(7, user.getLastLogin() != null ? new Timestamp(user.getLastLogin().getTime()) : null);
            stmt.setTimestamp(8, new Timestamp(user.getDateJoined().getTime()));

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getInt(1));
                    }
                }
            }

            log.info("User добавлен. ID: " + user.getId());
        } catch (SQLException e) {
            log.error("Ошибка при добавлении User: " + e.getMessage(), e);
        }
    }

    public User getUserById(int id) {
        String query = "SELECT * FROM users WHERE id = ?";
        log.info("Выполняется запрос для получения User с ID: " + id);

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = mapResultSetToUser(rs);
                log.info("User найден: " + user);
                return user;
            } else {
                log.warn("User с ID " + id + " не найден.");
            }
        } catch (SQLException e) {
            log.error("Ошибка при получении User с ID: " + id, e);
        }

        return null;
    }

    public List<User> getAllUsers() {
        String query = "SELECT * FROM users";
        log.info("Выполняется запрос для получения всех Users");

        List<User> users = new ArrayList<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            log.info("Найдено " + users.size() + " пользователей.");
        } catch (SQLException e) {
            log.error("Ошибка при получении списка пользователей.", e);
        }

        return users;
    }

    public void updateUser(User user) {
        String query = "UPDATE users SET name = ?, email = ?, bio = ?, count_project = ?, avatar_url = ?, is_active = ?, last_login = ?, date_joined = ? WHERE id = ?";
        log.info("Выполняется запрос на обновление User с ID: " + user.getId());

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getBio());
            stmt.setInt(4, user.getCountProject());
            stmt.setString(5, user.getAvatarURL());
            stmt.setBoolean(6, user.isActive());
            stmt.setTimestamp(7, user.getLastLogin() != null ? new Timestamp(user.getLastLogin().getTime()) : null);
            stmt.setTimestamp(8, new Timestamp(user.getDateJoined().getTime()));
            stmt.setInt(9, user.getId());

            int rowsAffected = stmt.executeUpdate();
            log.info("User обновлен. Затронуто строк: " + rowsAffected);
        } catch (SQLException e) {
            log.error("Ошибка при обновлении User с ID: " + user.getId(), e);
        }
    }

    public void deleteUser(int id) {
        String query = "DELETE FROM users WHERE id = ?";
        log.info("Выполняется запрос на удаление User с ID: " + id);

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            log.info("User удален. Затронуто строк: " + rowsAffected);
        } catch (SQLException e) {
            log.error("Ошибка при удалении User с ID: " + id, e);
        }
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setBio(rs.getString("bio"));
        user.setCountProject(rs.getInt("count_project"));
        user.setAvatarURL(rs.getString("avatar_url"));
        user.setActive(rs.getBoolean("is_active"));
        user.setLastLogin(rs.getTimestamp("last_login"));
        user.setDateJoined(rs.getTimestamp("date_joined"));
        return user;
    }

    public void clearTable() {
        String query = "DELETE FROM users";
        log.info("Выполняется очистка таблицы users");

        try (Statement stmt = connection.createStatement()) {
            int rowsAffected = stmt.executeUpdate(query);
            log.info("Таблица users очищена. Удалено строк: " + rowsAffected);
        } catch (SQLException e) {
            log.error("Ошибка при очистке таблицы users", e);
        }
    }
}