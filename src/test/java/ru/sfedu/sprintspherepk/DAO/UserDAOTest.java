package ru.sfedu.sprintspherepk.DAO;

import org.junit.Before;
import org.junit.Test;
import ru.sfedu.sprintspherepk.models.User;
import ru.sfedu.sprintspherepk.psql.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class UserDAOTest {
    private UserDAO userDAO;
    private Connection connection;

    @Before
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection(Config.getDbUrl(), Config.getDbUser(), Config.getDbPassword());
        userDAO = new UserDAO(connection);
        userDAO.clearTable();
    }

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setBio("Test Bio");
        user.setCountProject(5);
        user.setAvatarURL("http://example.com/avatar.png");
        user.setActive(true);
        user.setLastLogin(new Date());
        user.setDateJoined(new Date());

        userDAO.createUser(user);

        User retrieved = userDAO.getUserById(user.getId());
        assertNotNull(retrieved);
        assertEquals(user.getName(), retrieved.getName());
        assertEquals(user.getEmail(), retrieved.getEmail());
        assertEquals(user.getBio(), retrieved.getBio());
        assertEquals(user.getCountProject(), retrieved.getCountProject());
        assertEquals(user.getAvatarURL(), retrieved.getAvatarURL());
        assertEquals(user.isActive(), retrieved.isActive());
        assertEquals(user.getLastLogin(), retrieved.getLastLogin());
        assertEquals(user.getDateJoined(), retrieved.getDateJoined());
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setBio("Test Bio");
        user.setCountProject(5);
        user.setAvatarURL("http://example.com/avatar.png");
        user.setActive(true);
        user.setLastLogin(new Date());
        user.setDateJoined(new Date());

        userDAO.createUser(user);

        user.setName("Updated User");
        user.setBio("Updated Bio");
        user.setCountProject(10);

        userDAO.updateUser(user);

        User updated = userDAO.getUserById(user.getId());
        assertNotNull(updated);
        assertEquals("Updated User", updated.getName());
        assertEquals("Updated Bio", updated.getBio());
        assertEquals(10, updated.getCountProject());
    }

    @Test
    public void testDeleteUser() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setBio("Test Bio");
        user.setCountProject(5);
        user.setAvatarURL("http://example.com/avatar.png");
        user.setActive(true);
        user.setLastLogin(new Date());
        user.setDateJoined(new Date());

        userDAO.createUser(user);

        userDAO.deleteUser(user.getId());

        User deleted = userDAO.getUserById(user.getId());
        assertNull(deleted);
    }

    @Test
    public void testGetAllUsers() {
        User user1 = new User();
        user1.setName("User 1");
        user1.setEmail("user1@example.com");
        user1.setBio("Bio 1");
        user1.setCountProject(3);
        user1.setAvatarURL("http://example.com/avatar1.png");
        user1.setActive(true);
        user1.setLastLogin(new Date());
        user1.setDateJoined(new Date());

        User user2 = new User();
        user2.setName("User 2");
        user2.setEmail("user2@example.com");
        user2.setBio("Bio 2");
        user2.setCountProject(7);
        user2.setAvatarURL("http://example.com/avatar2.png");
        user2.setActive(false);
        user2.setLastLogin(new Date());
        user2.setDateJoined(new Date());

        userDAO.createUser(user1);
        userDAO.createUser(user2);

        List<User> users = userDAO.getAllUsers();
        assertNotNull(users);
        assertTrue(users.size() >= 2);
        assertTrue(users.stream().anyMatch(u -> u.getEmail().equals("user1@example.com")));
        assertTrue(users.stream().anyMatch(u -> u.getEmail().equals("user2@example.com")));
    }
}
