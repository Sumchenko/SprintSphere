package ru.sfedu.sprintspherepk;

import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.List;

public class TaskService {
    private static final Logger logger = Logger.getLogger(TaskService.class);

    // Метод для добавления задачи в список
    public void addTask(List<Task> tasks, Task task) {
        logger.debug("Attempting to add task: " + task);
        tasks.add(task);
        logger.debug("Task added successfully.");
    }

    // Метод для получения всех задач
    public List<Task> getAllTasks(List<Task> tasks) {
        logger.debug("Retrieving all tasks. Current count: " + tasks.size());
        return tasks;
    }

    // Метод для поиска задачи по имени
    public Task findTaskByTitle(List<Task> tasks, String title) {
        logger.debug("Searching for task with title: " + title);
        for (Task task : tasks) {
            if (task.getTitle().equalsIgnoreCase(title)) {
                logger.debug("Task found: " + task);
                return task;
            }
        }
        logger.debug("Task not found: " + title);
        return null;
    }
}
