package data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class TaskDatabase implements Serializable {
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
        Objects.requireNonNull(task, "data.Task cannot be null.");
        LocalDate key = task.getDeadline();

        if (!tasks.containsKey(key))
            tasks.put(key, new ArrayList<>());

        tasks.get(key).add(task);
        sortTasksByPriority(key);
        tasksCreated++;
    }

    public List<Task> findTasksByDate(LocalDate key) {
        return tasks.get(key);
    }

    private void sortTasksByPriority(LocalDate key) {
        Collections.sort(tasks.get(key));
    }

    public void addToHistory(Task task) {
        tasksHistory.add(task);
    }

    public void removeTask(LocalDate key, Task task) {
        tasks.get(key).remove(task);
    }

    public Collection<Task> findTaskByName(String taskName) {
        return tasks.values().stream()
                .flatMap(Collection::stream)
                .filter(task -> task.getName().contains(taskName))
                .collect(Collectors.toList());
    }

    public void clearHistory() {
        tasksHistory.clear();
    }

    public void clearStatistics() {
        tasksCreated = 0;
        tasksCompleted = 0;
        tasksFailed = 0;
    }
}
