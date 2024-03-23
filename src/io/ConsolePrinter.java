package io;

import controller.Option;
import data.Task;

import java.time.LocalDate;
import java.util.*;

public class ConsolePrinter {
    private static final int QUICK_MENU_START = 0;
    private static final int QUICK_MENU_END = 5;
    private static final String PROMPT_CHARACTER = ">";
    private static final String HORIZONTAL_SEPARATOR = "-".repeat(7);

    public void print(String line) {
        System.out.print(line);
    }

    public void printLine(String line) {
        print(line + "\n");
    }

    public void promptCharacter() {
        print(PROMPT_CHARACTER);
    }

    public void printMenu() {
        Option[] options = Option.values();
        String quickMenu = generateMenu(options, QUICK_MENU_START, QUICK_MENU_END);
        String remainingMenu = generateMenu(options, QUICK_MENU_END, options.length);

        String menu = "\n" + HORIZONTAL_SEPARATOR + "QUICK-MENU\n" + quickMenu +
                             HORIZONTAL_SEPARATOR + "MENU\n" + remainingMenu;
        printLine(menu);
    }

    private String generateMenu(Option[] options, int start, int end) {
        StringBuilder builder = new StringBuilder();

        for (int i = start; i < end; i++) {
            builder.append(options[i]).append("\n");
        }
        return builder.toString();
    }

    public void printAllTasks(Map<LocalDate, List<Task>> tasks) {
        Set<Map.Entry<LocalDate, List<Task>>> entries = tasks.entrySet();

        for (Map.Entry<LocalDate, List<Task>> entry : entries) {
            printLine(HORIZONTAL_SEPARATOR + "Tasks of " + entry.getKey());
            printTasksWithIndex(entry.getValue());
        }
    }

    public void printTasksWithIndex(Collection<Task> tasks) {
        int index = 1;
        for (Task task : tasks) {
            printLine(index++ + ". " + task);
        }
    }
    public void printTasks(Collection<Task> tasks) {
        tasks.stream().map(Task::toString).forEach(this::printLine);
    }
}
