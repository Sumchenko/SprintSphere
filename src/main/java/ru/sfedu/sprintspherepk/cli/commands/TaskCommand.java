package ru.sfedu.sprintspherepk.cli.commands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import ru.sfedu.sprintspherepk.DAO.TaskDAO;
import ru.sfedu.sprintspherepk.models.Task;
import ru.sfedu.sprintspherepk.psql.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

@Command(name = "task", description = "Управление задачами", subcommands = {CreateTaskCommand.class, UpdateTaskCommand.class, DeleteTaskCommand.class, ListTasksCommand.class})
public class TaskCommand implements Runnable {
    @Override
    public void run() {
        System.out.println("Используйте команды для управления задачами.");
    }
}

@Command(name = "create", description = "Создать новую задачу")
class CreateTaskCommand implements Runnable {

    @Option(names = {"-t", "--title"}, description = "Название задачи", required = true)
    private String title;

    @Option(names = {"-d", "--description"}, description = "Описание задачи", required = true)
    private String description;

    @Option(names = {"-s", "--status"}, description = "Статус задачи", defaultValue = "NEW")
    private String status;

    @Option(names = {"-p", "--priority"}, description = "Приоритет задачи", defaultValue = "1")
    private int priority;

    @Override
    public void run() {
        try {
            String url = Config.getDbUrl();
            String username = Config.getDbUser();
            String password = Config.getDbPassword();

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                TaskDAO taskDAO = new TaskDAO(connection);
                Task task = new Task(0, title, description, status, priority);
                taskDAO.createTask(task);
                System.out.println("Задача создана: " + task);
            }
        } catch (Exception e) {
            System.err.println("Ошибка при создании задачи: " + e.getMessage());
        }
    }
}

@Command(name = "update", description = "Обновить задачу")
class UpdateTaskCommand implements Runnable {

    @Option(names = {"-i", "--id"}, description = "ID задачи", required = true)
    private int id;

    @Option(names = {"-t", "--title"}, description = "Название задачи")
    private String title;

    @Option(names = {"-d", "--description"}, description = "Описание задачи")
    private String description;

    @Option(names = {"-s", "--status"}, description = "Статус задачи")
    private String status;

    @Option(names = {"-p", "--priority"}, description = "Приоритет задачи")
    private Integer priority;

    @Override
    public void run() {
        try {
            String url = Config.getDbUrl();
            String username = Config.getDbUser();
            String password = Config.getDbPassword();

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                TaskDAO taskDAO = new TaskDAO(connection);
                Task task = taskDAO.getTaskById(id);
                if (task == null) {
                    System.err.println("Задача с ID " + id + " не найдена.");
                    return;
                }

                if (title != null) task.setTitle(title);
                if (description != null) task.setDescription(description);
                if (status != null) task.setStatus(status);
                if (priority != null) task.setPriority(priority);

                taskDAO.updateTask(task);
                System.out.println("Задача обновлена: " + task);
            }
        } catch (Exception e) {
            System.err.println("Ошибка при обновлении задачи: " + e.getMessage());
        }
    }
}

@Command(name = "delete", description = "Удалить задачу")
class DeleteTaskCommand implements Runnable {

    @Option(names = {"-i", "--id"}, description = "ID задачи", required = true)
    private int id;

    @Override
    public void run() {
        try {
            String url = Config.getDbUrl();
            String username = Config.getDbUser();
            String password = Config.getDbPassword();

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                TaskDAO taskDAO = new TaskDAO(connection);
                taskDAO.deleteTask(id);
                System.out.println("Задача с ID " + id + " удалена.");
            }
        } catch (Exception e) {
            System.err.println("Ошибка при удалении задачи: " + e.getMessage());
        }
    }
}

@Command(name = "list", description = "Вывести список задач")
class ListTasksCommand implements Runnable {
    @Override
    public void run() {
        try {
            String url = Config.getDbUrl();
            String username = Config.getDbUser();
            String password = Config.getDbPassword();

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                TaskDAO taskDAO = new TaskDAO(connection);
                List<Task> tasks = taskDAO.getAllTasks();
                tasks.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.err.println("Ошибка при получении списка задач: " + e.getMessage());
        }
    }
}
