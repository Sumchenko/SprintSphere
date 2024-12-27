package ru.sfedu.sprintspherepk.DAO;

import org.junit.Before;
import org.junit.Test;
import ru.sfedu.sprintspherepk.models.Project;
import ru.sfedu.sprintspherepk.psql.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class ProjectDAOTest {
    private ProjectDAO projectDAO;
    private Connection connection;

    @Before
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection(Config.getDbUrl(), Config.getDbUser(), Config.getDbPassword());
        projectDAO = new ProjectDAO(connection);
        projectDAO.clearTable();
    }

    @Test
    public void testCreateProject() {
        Project project = new Project();
        project.setName("Test Project");
        project.setDescription("Test Description");

        projectDAO.createProject(project);

        Project retrieved = projectDAO.getProjectById(project.getId());
        assertNotNull(retrieved);
        assertEquals(project.getName(), retrieved.getName());
        assertEquals(project.getDescription(), retrieved.getDescription());
    }

    @Test
    public void testUpdateProject() {
        Project project = new Project();
        project.setName("Test Project");
        project.setDescription("Test Description");

        projectDAO.createProject(project);

        project.setName("Updated Project");
        project.setDescription("Updated Description");

        projectDAO.updateProject(project);

        Project updated = projectDAO.getProjectById(project.getId());
        assertNotNull(updated);
        assertEquals("Updated Project", updated.getName());
        assertEquals("Updated Description", updated.getDescription());
    }

    @Test
    public void testDeleteProject() {
        Project project = new Project();
        project.setName("Test Project");
        project.setDescription("Test Description");

        projectDAO.createProject(project);

        projectDAO.deleteProject(project.getId());

        Project deleted = projectDAO.getProjectById(project.getId());
        assertNull(deleted);
    }

    @Test
    public void testGetAllProjects() {
        Project project1 = new Project();
        project1.setName("Project 1");
        project1.setDescription("Description 1");

        Project project2 = new Project();
        project2.setName("Project 2");
        project2.setDescription("Description 2");

        projectDAO.createProject(project1);
        projectDAO.createProject(project2);

        List<Project> projects = projectDAO.getAllProjects();
        assertNotNull(projects);
        assertTrue(projects.size() >= 2);
        assertTrue(projects.stream().anyMatch(p -> p.getName().equals("Project 1")));
        assertTrue(projects.stream().anyMatch(p -> p.getName().equals("Project 2")));
    }
}