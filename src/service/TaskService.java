package service;

import data.Database;
import data.Status;
import data.Task;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class TaskService {
    private final Database database;

    public Database getDatabase() {
        return database;
    }

    public TaskService(Database database) {
        this.database = database;
    }

    public Map<LocalDate, List<Task>> getTasks() {
        return database.getTasks();
    }

    public void addTask(Task task) {
        database.addTask(task);
    }

    public void removeTask(Task task) {
        database.removeTask(task.getDeadline(), task);
    }

    public List<Task> findTasksByDate(LocalDate key) {
        return database.getTasks().get(key);
    }

    public void endTask(Task task) {
        database.removeTask(task.getDeadline(), task);
        database.addToHistory(task);
        Database.tasksCompleted++;
    }

    public void failTask(Task task) {
        task.setStatus(Status.FAILED);
        database.removeTask(task.getDeadline(), task);
        database.addToHistory(task);
        Database.tasksFailed++;
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

    public Optional<Task> endAndReturnFirstTask(List<Task> tasks) {
        Task task = null;

        if (tasks != null && !tasks.isEmpty()) {
            task = tasks.removeFirst();
            task.setStatus(Status.DONE);
            database.addToHistory(task);
            Database.tasksCompleted++;
        }
        return Optional.ofNullable(task);
    }

    public String getStatistics() {
        return "Statistics" + "\n" +
                "Number of all created tasks: " + Database.tasksCreated + "\n" +
                "Number of completed tasks: " + Database.tasksCompleted + "\n" +
                "Number of failed tasks: " + Database.tasksFailed + "\n";
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
