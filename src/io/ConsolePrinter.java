package io;

import data.Task;

import java.time.LocalDate;
import java.util.*;

public class ConsolePrinter {
    public void printLine(String line) {
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

    public void printTasks(Collection<Task> tasks) {
        if (tasks == null || tasks.isEmpty())
            printLine("No tasks found.");
        else
          tasks.stream().map(Task::toString).forEach(this::printLine);
    }

    public void printTasksWithIndex(Collection<Task> tasks) {
        if (tasks.isEmpty())
            printLine("No tasks found.");

        int index = 1;
        for (Task task : tasks) {
            printLine(index + ". " + task);
            index++;
        }
    }
}
