import java.time.LocalDate;
import java.util.*;

class ConsolePrinter {
    void printLine(String line) {
        System.out.println(line);
    }

    void printAllTasks(Map<LocalDate, List<Task>> tasks) {
        if (tasks.isEmpty())
            printLine("The task list is empty.");

        Set<Map.Entry<LocalDate, List<Task>>> entries = tasks.entrySet();
        for (Map.Entry<LocalDate, List<Task>> entry : entries) {
            printLine(entry.getKey().toString());
            entry.getValue().stream().map(Task::toString).forEach(this::printLine);
        }
    }

    void printTasks(Collection<Task> tasks) {
        if (tasks.isEmpty())
            printLine("No tasks found.");

        tasks.stream().map(Task::toString).forEach(this::printLine);
    }
}
