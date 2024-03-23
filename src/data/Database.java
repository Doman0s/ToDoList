package data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Database implements Serializable {
    public static int tasksCreated;
    public static int tasksCompleted;
    public static int tasksFailed;

    // tasks grouped by date and sorted by priority
    private final Map<LocalDate, List<Task>> tasks = new TreeMap<>();
    private final List<Task> tasksHistory = new ArrayList<>();

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
        Database.tasksCreated++;
    }

    private void sortTasksByPriority(LocalDate key) {
        Collections.sort(tasks.get(key));
    }

    public void removeTask(LocalDate key, Task task) {
        tasks.get(key).remove(task);
    }

    public void addToHistory(Task task) {
        tasksHistory.add(task);
    }

    public void clearHistory() {
        tasksHistory.clear();
    }

    public void clearStatistics() {
        Database.tasksCreated = 0;
        Database.tasksCompleted = 0;
        Database.tasksFailed = 0;
    }
}