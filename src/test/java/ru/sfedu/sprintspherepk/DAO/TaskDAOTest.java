package ru.sfedu.sprintspherepk.DAO;

import org.junit.Before;
import org.junit.Test;
import ru.sfedu.sprintspherepk.models.Task;
import ru.sfedu.sprintspherepk.psql.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class TaskDAOTest {
    private TaskDAO taskDAO;
    private Connection connection;

    @Before
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection(Config.getDbUrl(), Config.getDbUser(), Config.getDbPassword());
        taskDAO = new TaskDAO(connection);
        taskDAO.clearTable();
    }

    @Test
    public void testCreateTask() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus("OPEN");
        task.setPriority(3);

        taskDAO.createTask(task);

        Task retrieved = taskDAO.getTaskById(task.getId());
        assertNotNull(retrieved);
        assertEquals(task.getTitle(), retrieved.getTitle());
        assertEquals(task.getDescription(), retrieved.getDescription());
        assertEquals(task.getStatus(), retrieved.getStatus());
        assertEquals(task.getPriority(), retrieved.getPriority());
    }

    @Test
    public void testUpdateTask() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus("OPEN");
        task.setPriority(3);

        taskDAO.createTask(task);

        task.setTitle("Updated Task");
        task.setDescription("Updated Description");
        task.setStatus("IN_PROGRESS");
        task.setPriority(4);

        taskDAO.updateTask(task);

        Task updated = taskDAO.getTaskById(task.getId());
        assertNotNull(updated);
        assertEquals("Updated Task", updated.getTitle());
        assertEquals("Updated Description", updated.getDescription());
        assertEquals("IN_PROGRESS", updated.getStatus());
        assertEquals(4, updated.getPriority());
    }

    @Test
    public void testDeleteTask() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus("OPEN");
        task.setPriority(3);

        taskDAO.createTask(task);

        taskDAO.deleteTask(task.getId());

        Task deleted = taskDAO.getTaskById(task.getId());
        assertNull(deleted);
    }

    @Test
    public void testGetAllTasks() {
        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");
        task1.setStatus("OPEN");
        task1.setPriority(3);

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setStatus("IN_PROGRESS");
        task2.setPriority(4);

        taskDAO.createTask(task1);
        taskDAO.createTask(task2);

        List<Task> tasks = taskDAO.getAllTasks();
        assertNotNull(tasks);
        assertTrue(tasks.size() >= 2);
        assertTrue(tasks.stream().anyMatch(t -> t.getTitle().equals("Task 1")));
        assertTrue(tasks.stream().anyMatch(t -> t.getTitle().equals("Task 2")));
    }
}
