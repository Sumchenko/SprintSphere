package ru.sfedu.sprintspherepk.cli.commands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import ru.sfedu.sprintspherepk.DAO.ProjectDAO;
import ru.sfedu.sprintspherepk.models.Project;
import ru.sfedu.sprintspherepk.psql.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

@Command(name = "project", description = "Управление проектами", subcommands = {CreateProjectCommand.class, DeleteProjectCommand.class, ListProjectsCommand.class})
public class ProjectCommand implements Runnable {
    @Override
    public void run() {
        System.out.println("Используйте команды для управления проектами.");
    }
}

@Command(name = "create", description = "Создать новый проект")
class CreateProjectCommand implements Runnable {

    @Option(names = {"-n", "--name"}, description = "Название проекта", required = true)
    private String name;

    @Option(names = {"-d", "--description"}, description = "Описание проекта")
    private String description;

    @Override
    public void run() {
        try {
            String url = Config.getDbUrl();
            String username = Config.getDbUser();
            String password = Config.getDbPassword();

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                ProjectDAO projectDAO = new ProjectDAO(connection);
                Project project = new Project(0, name, description);
                projectDAO.createProject(project);
                System.out.println("Проект создан: " + project);
            }
        } catch (Exception e) {
            System.err.println("Ошибка при создании проекта: " + e.getMessage());
        }
    }
}

@Command(name = "delete", description = "Удалить проект")
class DeleteProjectCommand implements Runnable {

    @Option(names = {"-i", "--id"}, description = "ID проекта", required = true)
    private int id;

    @Override
    public void run() {
        try {
            String url = Config.getDbUrl();
            String username = Config.getDbUser();
            String password = Config.getDbPassword();

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                ProjectDAO projectDAO = new ProjectDAO(connection);
                projectDAO.deleteProject(id);
                System.out.println("Проект с ID " + id + " удален.");
            }
        } catch (Exception e) {
            System.err.println("Ошибка при удалении проекта: " + e.getMessage());
        }
    }
}

@Command(name = "list", description = "Вывести список проектов")
class ListProjectsCommand implements Runnable {
    @Override
    public void run() {
        try {
            String url = Config.getDbUrl();
            String username = Config.getDbUser();
            String password = Config.getDbPassword();

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                ProjectDAO projectDAO = new ProjectDAO(connection);
                List<Project> projects = projectDAO.getAllProjects();
                projects.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.err.println("Ошибка при получении списка проектов: " + e.getMessage());
        }
    }
}
