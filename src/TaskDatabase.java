import exception.TaskNotFoundException;

import java.time.LocalDate;
import java.util.*;

class TaskDatabase {
    // tasks grouped by date and sorted by priority
    private Map<LocalDate, PriorityQueue<Task>> tasks = new HashMap<>();
    private List<Task> tasksHistory = new ArrayList<>();

    public Map<LocalDate, PriorityQueue<Task>> getTasks() {
        return tasks;
    }

    public List<Task> getTasksHistory() {
        return tasksHistory;
    }

    void addTask(Task task) {
        Objects.requireNonNull(task, "Task cannot be null.");
        LocalDate key = task.getDeadline();

        if (!tasks.containsKey(key))
            tasks.put(key, new PriorityQueue<>());

        tasks.get(key).add(task);
    }

    void deleteTask(Task task) {
        findTask(task);
        LocalDate key = task.getDeadline();
        tasks.get(key).remove(task);
    }

    Task findTask(Task task) {
        Objects.requireNonNull(task, "Task cannot be null.");
        LocalDate key = task.getDeadline();

        if(tasks.containsKey(key) && tasks.get(key).contains(task))
            return tasks.get(key).peek();
        else
            throw new TaskNotFoundException("Task doesn't exist in the database.");
    }
}
