package ru.sfedu.sprintspherepk.DAO;

import org.junit.Before;
import org.junit.Test;
import ru.sfedu.sprintspherepk.models.Retrospective;
import ru.sfedu.sprintspherepk.psql.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class RetrospectiveDAOTest {
    private RetrospectiveDAO retrospectiveDAO;
    private Connection connection;

    @Before
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection(Config.getDbUrl(), Config.getDbUser(), Config.getDbPassword());
        retrospectiveDAO = new RetrospectiveDAO(connection);
        retrospectiveDAO.clearTable();
    }

    @Test
    public void testCreateRetrospective() {
        Retrospective retrospective = new Retrospective();
        retrospective.setSummary("Summary of the Sprint");
        retrospective.setImprovements("Improve communication");
        retrospective.setPositives("Completed all tasks");

        retrospectiveDAO.createRetrospective(retrospective);

        Retrospective retrieved = retrospectiveDAO.getRetrospectiveById(retrospective.getId());
        assertNotNull(retrieved);
        assertEquals(retrospective.getSummary(), retrieved.getSummary());
        assertEquals(retrospective.getImprovements(), retrieved.getImprovements());
        assertEquals(retrospective.getPositives(), retrieved.getPositives());
    }

    @Test
    public void testUpdateRetrospective() {
        Retrospective retrospective = new Retrospective();
        retrospective.setSummary("Summary of the Sprint");
        retrospective.setImprovements("Improve communication");
        retrospective.setPositives("Completed all tasks");

        retrospectiveDAO.createRetrospective(retrospective);

        retrospective.setSummary("Updated Summary");
        retrospective.setImprovements("Better task management");

        retrospectiveDAO.updateRetrospective(retrospective);

        Retrospective updated = retrospectiveDAO.getRetrospectiveById(retrospective.getId());
        assertNotNull(updated);
        assertEquals("Updated Summary", updated.getSummary());
        assertEquals("Better task management", updated.getImprovements());
    }

    @Test
    public void testDeleteRetrospective() {
        Retrospective retrospective = new Retrospective();
        retrospective.setSummary("Summary of the Sprint");
        retrospective.setImprovements("Improve communication");
        retrospective.setPositives("Completed all tasks");

        retrospectiveDAO.createRetrospective(retrospective);

        retrospectiveDAO.deleteRetrospective(retrospective.getId());

        Retrospective deleted = retrospectiveDAO.getRetrospectiveById(retrospective.getId());
        assertNull(deleted);
    }

    @Test
    public void testGetAllRetrospectives() {
        Retrospective retrospective1 = new Retrospective();
        retrospective1.setSummary("Summary 1");
        retrospective1.setImprovements("Improve communication");
        retrospective1.setPositives("Completed all tasks");

        Retrospective retrospective2 = new Retrospective();
        retrospective2.setSummary("Summary 2");
        retrospective2.setImprovements("Better planning");
        retrospective2.setPositives("Completed some tasks");

        retrospectiveDAO.createRetrospective(retrospective1);
        retrospectiveDAO.createRetrospective(retrospective2);

        List<Retrospective> retrospectives = retrospectiveDAO.getAllRetrospectives();
        assertNotNull(retrospectives);
        assertTrue(retrospectives.size() >= 2);
        assertTrue(retrospectives.stream().anyMatch(r -> r.getSummary().equals("Summary 1")));
        assertTrue(retrospectives.stream().anyMatch(r -> r.getSummary().equals("Summary 2")));
    }
}
