//package ru.sfedu.sprintspherepk.DAO;
//
//import org.junit.Before;
//import org.junit.Test;
//import ru.sfedu.sprintspherepk.models.Sprint;
//import ru.sfedu.sprintspherepk.psql.Config;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.time.temporal.ChronoUnit;
//import java.util.Date;
//import java.util.List;
//
//import static org.junit.Assert.*;
//
//public class SprintDAOTest {
//    private SprintDAO sprintDAO;
//    private Connection connection;
//
//    @Before
//    public void setUp() throws SQLException {
//        connection = DriverManager.getConnection(Config.getDbUrl(), Config.getDbUser(), Config.getDbPassword());
//        sprintDAO = new SprintDAO(connection);
//        sprintDAO.clearTable();
//    }
//
//    @Test
//    public void testCreateSprint() {
//
//        Sprint sprint = new Sprint();
//        sprint.setStartDate(new Date());
//        sprint.setEndDate(new Date(System.currentTimeMillis() + 86400000)); // 1 day later
//        sprint.setProgress(50.0);
//
//        sprintDAO.createSprint(sprint);
//
//        Sprint retrieved = sprintDAO.getSprintById(sprint.getId());
//        assertNotNull(retrieved);
//        // Преобразуем java.sql.Date обратно в java.util.Date для сравнения
//        Date retrievedStartDate = new Date(retrieved.getStartDate().getTime());
//        Date retrievedEndDate = new Date(retrieved.getEndDate().getTime());
//
//        // Сравниваем только даты без времени
//        assertEquals(sprint.getStartDate().toInstant().truncatedTo(ChronoUnit.DAYS),
//                retrievedStartDate.toInstant().truncatedTo(ChronoUnit.DAYS));
//        assertEquals(sprint.getEndDate().toInstant().truncatedTo(ChronoUnit.DAYS),
//                retrievedEndDate.toInstant().truncatedTo(ChronoUnit.DAYS));
//
//        assertEquals(sprint.getProgress(), retrieved.getProgress(), 0.01);
//    }
//
//    @Test
//    public void testUpdateSprint() {
//        Sprint sprint = new Sprint();
//        sprint.setStartDate(new Date());
//        sprint.setEndDate(new Date(System.currentTimeMillis() + 86400000)); // 1 day later
//        sprint.setProgress(50.0);
//
//        sprintDAO.createSprint(sprint);
//
//        sprint.setProgress(75.0);
//        sprintDAO.updateSprint(sprint);
//
//        Sprint updated = sprintDAO.getSprintById(sprint.getId());
//        assertNotNull(updated);
//        assertEquals(75.0, updated.getProgress(), 0.01);
//    }
//
//    @Test
//    public void testDeleteSprint() {
//        Sprint sprint = new Sprint();
//        sprint.setStartDate(new Date());
//        sprint.setEndDate(new Date(System.currentTimeMillis() + 86400000)); // 1 day later
//        sprint.setProgress(50.0);
//
//        sprintDAO.createSprint(sprint);
//
//        sprintDAO.deleteSprint(sprint.getId());
//
//        Sprint deleted = sprintDAO.getSprintById(sprint.getId());
//        assertNull(deleted);
//    }
//
//    @Test
//    public void testGetAllSprints() {
//        Sprint sprint1 = new Sprint();
//        sprint1.setStartDate(new Date());
//        sprint1.setEndDate(new Date(System.currentTimeMillis() + 86400000));
//        sprint1.setProgress(50.0);
//
//        Sprint sprint2 = new Sprint();
//        sprint2.setStartDate(new Date());
//        sprint2.setEndDate(new Date(System.currentTimeMillis() + 86400000));
//        sprint2.setProgress(75.0);
//
//        sprintDAO.createSprint(sprint1);
//        sprintDAO.createSprint(sprint2);
//
//        List<Sprint> sprints = sprintDAO.getAllSprints();
//        assertNotNull(sprints);
//        assertTrue(sprints.size() >= 2);
//        assertTrue(sprints.stream().anyMatch(s -> s.getProgress() == 50.0));
//        assertTrue(sprints.stream().anyMatch(s -> s.getProgress() == 75.0));
//    }
//}
