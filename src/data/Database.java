package data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

public class Database implements Serializable {
    private int tasksCreated;
    private int tasksCompleted;
    private int tasksFailed;

    // tasks grouped by date and sorted by priority
    private final Map<LocalDate, List<Task>> tasks = new TreeMap<>();
    private final List<Task> tasksHistory = new ArrayList<>();

    public int getTasksCreated() {
        return tasksCreated;
    }

    public void setTasksCreated(int tasksCreated) {
        this.tasksCreated = tasksCreated;
    }

    public int getTasksCompleted() {
        return tasksCompleted;
    }

    public void setTasksCompleted(int tasksCompleted) {
        this.tasksCompleted = tasksCompleted;
    }

    public int getTasksFailed() {
        return tasksFailed;
    }

    public void setTasksFailed(int tasksFailed) {
        this.tasksFailed = tasksFailed;
    }

    public Map<LocalDate, List<Task>> getTasks() {
        return tasks;
    }

    public List<Task> getTasksHistory() {
        return tasksHistory;
    }

    public void addTask(Task task) {
        LocalDate key = task.getDeadline();

        if (!tasks.containsKey(key))
            tasks.put(key, new ArrayList<>());

        tasks.get(key).add(task);
        sortTasksByPriority(key);
    }

    private void sortTasksByPriority(LocalDate key) {
        Collections.sort(tasks.get(key));
    }

    public void removeTask(LocalDate key, Task task) {
        tasks.get(key).remove(task);
    }

    public void removeTasksFromDate(LocalDate date) {
        tasks.remove(date);
    }

    public void addToHistory(Task task) {
        tasksHistory.add(task);
    }

    public void clearHistory() {
        tasksHistory.clear();
    }

    public void clearStatistics() {
        setTasksCreated(0);
        setTasksCompleted(0);
        setTasksFailed(0);
    }
}