package ru.sfedu.sprintspherepk.DAO;

import org.apache.log4j.Logger;
import ru.sfedu.sprintspherepk.models.Retrospective;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RetrospectiveDAO {

    private static final Logger log = Logger.getLogger(RetrospectiveDAO.class);
    private final Connection connection;

    public RetrospectiveDAO(Connection connection) {
        this.connection = connection;
    }

    public void createRetrospective(Retrospective retrospective) {
        String query = "INSERT INTO retrospectives(summary, improvements, positives) VALUES (?, ?, ?)";
        log.info("Выполняется запрос на добавление Retrospective: " + retrospective);

        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, retrospective.getSummary());
            stmt.setString(2, retrospective.getImprovements());
            stmt.setString(3, retrospective.getPositives());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        retrospective.setId(generatedKeys.getInt(1));
                    }
                }
            }

            log.info("Retrospective добавлена. ID: " + retrospective.getId());
        } catch (SQLException e) {
            log.error("Ошибка при добавлении Retrospective: " + e.getMessage(), e);
        }
    }

    public Retrospective getRetrospectiveById(int id) {
        String query = "SELECT * FROM retrospectives WHERE id = ?";
        log.info("Выполняется запрос для получения Retrospective с ID: " + id);

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Retrospective retrospective = mapResultSetToRetrospective(rs);
                log.info("Retrospective найдена: " + retrospective);
                return retrospective;
            } else {
                log.warn("Retrospective с ID " + id + " не найдена.");
            }
        } catch (SQLException e) {
            log.error("Ошибка при получении Retrospective с ID: " + id, e);
        }

        return null;
    }

    public List<Retrospective> getAllRetrospectives() {
        String query = "SELECT * FROM retrospectives";
        log.info("Выполняется запрос для получения всех Retrospectives");

        List<Retrospective> retrospectives = new ArrayList<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                retrospectives.add(mapResultSetToRetrospective(rs));
            }
            log.info("Найдено " + retrospectives.size() + " ретроспектив.");
        } catch (SQLException e) {
            log.error("Ошибка при получении списка ретроспектив.", e);
        }

        return retrospectives;
    }

    public void updateRetrospective(Retrospective retrospective) {
        String query = "UPDATE retrospectives SET summary = ?, improvements = ?, positives = ? WHERE id = ?";
        log.info("Выполняется запрос на обновление Retrospective с ID: " + retrospective.getId());

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, retrospective.getSummary());
            stmt.setString(2, retrospective.getImprovements());
            stmt.setString(3, retrospective.getPositives());
            stmt.setInt(4, retrospective.getId());

            int rowsAffected = stmt.executeUpdate();
            log.info("Retrospective обновлена. Затронуто строк: " + rowsAffected);
        } catch (SQLException e) {
            log.error("Ошибка при обновлении Retrospective с ID: " + retrospective.getId(), e);
        }
    }

    public void deleteRetrospective(int id) {
        String query = "DELETE FROM retrospectives WHERE id = ?";
        log.info("Выполняется запрос на удаление Retrospective с ID: " + id);

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            log.info("Retrospective удалена. Затронуто строк: " + rowsAffected);
        } catch (SQLException e) {
            log.error("Ошибка при удалении Retrospective с ID: " + id, e);
        }
    }

    private Retrospective mapResultSetToRetrospective(ResultSet rs) throws SQLException {
        Retrospective retrospective = new Retrospective();
        retrospective.setId(rs.getInt("id"));
        retrospective.setSummary(rs.getString("summary"));
        retrospective.setImprovements(rs.getString("improvements"));
        retrospective.setPositives(rs.getString("positives"));
        return retrospective;
    }

    public void clearTable() {
        String query = "DELETE FROM retrospectives";
        log.info("Выполняется очистка таблицы retrospectives");

        try (Statement stmt = connection.createStatement()) {
            int rowsAffected = stmt.executeUpdate(query);
            log.info("Таблица retrospectives очищена. Удалено строк: " + rowsAffected);
        } catch (SQLException e) {
            log.error("Ошибка при очистке таблицы retrospectives", e);
        }
    }
}
