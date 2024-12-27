package ru.sfedu.sprintspherepk.cli.commands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import ru.sfedu.sprintspherepk.DAO.UserDAO;
import ru.sfedu.sprintspherepk.models.User;
import ru.sfedu.sprintspherepk.psql.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Date;
import java.util.List;

@Command(name = "user", description = "Управление пользователями", subcommands = {RegisterCommand.class, ListUsersCommand.class})
public class UserCommand implements Runnable {
    @Override
    public void run() {
        System.out.println("Используйте команды для управления пользователями.");
    }
}

@Command(name = "register", description = "Зарегистрировать нового пользователя")
class RegisterCommand implements Runnable {

    @Option(names = {"-n", "--name"}, description = "Имя пользователя", required = true)
    private String name;

    @Option(names = {"-e", "--email"}, description = "Email пользователя", required = true)
    private String email;

    @Option(names = {"-b", "--bio"}, description = "Биография пользователя")
    private String bio;

    @Override
    public void run() {
        try {
            String url = Config.getDbUrl();
            String username = Config.getDbUser();
            String password = Config.getDbPassword();

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                UserDAO userDAO = new UserDAO(connection);
                User user = new User(0, name, email, bio, 0, null, true, null, new Date());
                userDAO.createUser(user);
                System.out.println("Пользователь зарегистрирован: " + user);
            }
        } catch (Exception e) {
            System.err.println("Ошибка при регистрации пользователя: " + e.getMessage());
        }
    }
}

@Command(name = "list", description = "Вывести список пользователей")
class ListUsersCommand implements Runnable {
    @Override
    public void run() {
        try {
            String url = Config.getDbUrl();
            String username = Config.getDbUser();
            String password = Config.getDbPassword();

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                UserDAO userDAO = new UserDAO(connection);
                List<User> users = userDAO.getAllUsers();
                users.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.err.println("Ошибка при получении списка пользователей: " + e.getMessage());
        }
    }
}