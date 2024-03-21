import java.time.LocalDate;
import java.util.*;

class TaskDatabase {
    static int tasksCreated;
    static int tasksCompleted;
    static int tasksFailed;
    // tasks grouped by date and sorted by priority
    private Map<LocalDate, List<Task>> tasks = new TreeMap<>();
    private List<Task> tasksHistory = new ArrayList<>();

    public Map<LocalDate, List<Task>> getTasks() {
        return tasks;
    }

    public List<Task> getTasksHistory() {
        return tasksHistory;
    }

    void addTask(Task task) {
        Objects.requireNonNull(task, "Task cannot be null.");
        LocalDate key = task.getDeadline();

        if (!tasks.containsKey(key))
            tasks.put(key, new ArrayList<>());

        tasks.get(key).add(task);
        sortTasksByDate(key);
    }

    private void sortTasksByDate(LocalDate key) {
        Collections.sort(tasks.get(key));
    }

    void addToHistory(Task task) {
        tasksHistory.add(task);
    }

    void removeTask(LocalDate key, Task task) {
        tasks.get(key).remove(task);
    }

    void deleteTask(Task task) {
//        findTask(task.getName());
        LocalDate key = task.getDeadline();
        tasks.get(key).remove(task);
    }

    Collection<Task> findTaskByName(String taskName) {
        Collection<Task> foundTasks = new ArrayList<>();
        Collection<List<Task>> values = tasks.values();

        for (List<Task> value : values) {
            for (Task task : value) {
                if (task.getName().contains(taskName))
                    foundTasks.add(task);
            }
        }

        return foundTasks;
    }

    List<Task> findTasksByDate(LocalDate deadline) {
        Objects.requireNonNull(deadline);
        return tasks.get(deadline);
    }
}
