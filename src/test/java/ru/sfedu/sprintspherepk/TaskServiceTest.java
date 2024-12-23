package ru.sfedu.sprintspherepk;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TaskServiceTest {
    private TaskService taskService;
    private List<Task> taskList;

    @Before
    public void setUp() {
        taskService = new TaskService();
        taskList = new ArrayList<>();
    }

    @Test
    public void testAddTask() {
        Task task = new Task(1, "Создать класс");
        taskService.addTask(taskList, task);

        // Проверяем, что задача добавлена
        assertEquals(1, taskList.size());
        assertEquals("Создать класс", taskList.get(0).getTitle());
        assertEquals(1, taskList.get(0).getId());
    }

    @Test
    public void testGetAllTasks() {
        Task task1 = new Task(1, "Создать класс");
        Task task2 = new Task(2, "Добавить методы");
        taskService.addTask(taskList, task1);
        taskService.addTask(taskList, task2);

        // Проверяем, что получаем все добавленные задачи
        List<Task> tasks = taskService.getAllTasks(taskList);
        assertEquals(2, tasks.size());
        assertEquals("Создать класс", tasks.get(0).getTitle());
        assertEquals("Добавить методы", tasks.get(1).getTitle());
    }

    @Test
    public void testFindTaskByTitle() {
        Task task = new Task(1, "Создать класс");
        taskService.addTask(taskList, task);

        // Проверка на нахождение задачи по заголовку
        Task foundTask = taskService.findTaskByTitle(taskList, "Создать класс");
        assertEquals(task, foundTask);

        // Проверка на отсутствие задачи
        Task notFoundTask = taskService.findTaskByTitle(taskList, "Удалить пакет");
        assertNull(notFoundTask);
    }
}
