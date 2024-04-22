package service;

import data.Database;
import data.Status;
import data.Task;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class TaskService {
    private final Database database;

    public TaskService(Database database) {
        this.database = database;
    }

    public Database getDatabase() {
        return database;
    }

    public Map<LocalDate, List<Task>> getTasks() {
        return database.getTasks();
    }

    public void addTask(Task task) {
        database.addTask(task);
        database.setTasksCreated(database.getTasksCreated() + 1);
    }

    //adding tasks without increasing the number of created tasks
    public void addTaskSilently(Task task) {
        database.addTask(task);
    }

    public void removeTask(Task task) {
        LocalDate key = task.getDeadline();
        database.removeTask(key, task);

        List<Task> tasks = findTasksByDate(key);
        if (tasks != null && tasks.isEmpty())
            database.removeTasksFromDate(key);
    }

    public List<Task> findTasksByDate(LocalDate key) {
        return database.getTasks().get(key);
    }

    public void endTask(Task task) {
        removeTask(task);

        task.setStatus(Status.DONE);
        database.addToHistory(task);
        database.setTasksCompleted(database.getTasksCompleted() + 1);
    }

    public void failTask(Task task) {
        removeTask(task);

        task.setStatus(Status.FAILED);
        database.addToHistory(task);
        database.setTasksFailed(database.getTasksFailed() + 1);
    }

    public Task getTaskByDateAndIndex(LocalDate date, int index) {
        return database.getTasks().get(date).get(index);
    }

    public Collection<Task> findTaskByName(String taskName) {
        Collection<List<Task>> tasks = database.getTasks().values();

        return tasks.stream()
                .flatMap(Collection::stream)
                .filter(task -> task.getName().toLowerCase().contains(taskName.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Task endAndReturnFirstTask(List<Task> tasks) {
        Task task;
        task = tasks.removeFirst();
        task.setStatus(Status.DONE);
        database.addToHistory(task);
        database.setTasksCompleted(database.getTasksCompleted() + 1);

        return task;
    }

    public String getStatistics() {
        return "Statistics" + "\n" +
                "Number of all created tasks: " + database.getTasksCreated() + "\n" +
                "Number of completed tasks: " + database.getTasksCompleted() + "\n" +
                "Number of failed tasks: " + database.getTasksFailed() + "\n";
    }

    public void clearStatistics() {
        database.clearStatistics();
    }

    public List<Task> getHistory() {
        return database.getTasksHistory();
    }

    public void clearHistory() {
        database.clearHistory();
    }
}
