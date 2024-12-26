package ru.sfedu.sprintspherepk.DAO;

import org.apache.log4j.Logger;
import ru.sfedu.sprintspherepk.models.Sprint;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SprintDAO {

    private static final Logger log = Logger.getLogger(SprintDAO.class);
    private final Connection connection;

    public SprintDAO(Connection connection) {
        this.connection = connection;
    }

    public void createSprint(Sprint sprint) {
        String query = "INSERT INTO sprints(start_date, end_date, progress) VALUES (?, ?, ?)";
        log.info("Executing query to add Sprint: " + sprint);

        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, new java.sql.Date(sprint.getStartDate().getTime()));
            stmt.setDate(2, new java.sql.Date(sprint.getEndDate().getTime()));
            stmt.setDouble(3, sprint.getProgress());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        sprint.setId(generatedKeys.getInt(1));
                    }
                }
            }

            log.info("Sprint added. ID: " + sprint.getId());
        } catch (SQLException e) {
            log.error("Error adding Sprint: " + e.getMessage(), e);
        }
    }

    public Sprint getSprintById(int id) {
        String query = "SELECT * FROM sprints WHERE id = ?";
        log.info("Executing query to get Sprint with ID: " + id);

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Sprint sprint = mapResultSetToSprint(rs);
                log.info("Sprint found: " + sprint);
                return sprint;
            } else {
                log.warn("Sprint with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            log.error("Error getting Sprint with ID: " + id, e);
        }

        return null;
    }

    public List<Sprint> getAllSprints() {
        String query = "SELECT * FROM sprints";
        log.info("Executing query to get all Sprints");

        List<Sprint> sprints = new ArrayList<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                sprints.add(mapResultSetToSprint(rs));
            }
            log.info("Found " + sprints.size() + " sprints.");
        } catch (SQLException e) {
            log.error("Error getting list of sprints.", e);
        }

        return sprints;
    }

    public void updateSprint(Sprint sprint) {
        String query = "UPDATE sprints SET start_date = ?, end_date = ?, progress = ? WHERE id = ?";
        log.info("Executing query to update Sprint with ID: " + sprint.getId());

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDate(1, new java.sql.Date(sprint.getStartDate().getTime()));
            stmt.setDate(2, new java.sql.Date(sprint.getEndDate().getTime()));
            stmt.setDouble(3, sprint.getProgress());
            stmt.setInt(4, sprint.getId());

            int rowsAffected = stmt.executeUpdate();
            log.info("Sprint updated. Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            log.error("Error updating Sprint with ID: " + sprint.getId(), e);
        }
    }

    public void deleteSprint(int id) {
        String query = "DELETE FROM sprints WHERE id = ?";
        log.info("Executing query to delete Sprint with ID: " + id);

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            log.info("Sprint deleted. Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            log.error("Error deleting Sprint with ID: " + id, e);
        }
    }

    private Sprint mapResultSetToSprint(ResultSet rs) throws SQLException {
        Sprint sprint = new Sprint();
        sprint.setId(rs.getInt("id"));
        sprint.setStartDate(rs.getDate("start_date"));
        sprint.setEndDate(rs.getDate("end_date"));
        sprint.setProgress(rs.getDouble("progress"));
        return sprint;
    }

    public void clearTable() {
        String query = "DELETE FROM sprints";
        log.info("Executing query to clear sprints table");

        try (Statement stmt = connection.createStatement()) {
            int rowsAffected = stmt.executeUpdate(query);
            log.info("Sprints table cleared. Rows deleted: " + rowsAffected);
        } catch (SQLException e) {
            log.error("Error clearing sprints table", e);
        }
    }
}
