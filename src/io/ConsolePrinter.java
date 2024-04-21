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
        print(PROMPT_CHARACTER + " ");
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

    private void printHeaderRow() {
        String indexLabel = "No";
        String nameLabel = "Name";
        String descriptionLabel = "Description";
        String deadlineLabel = "Deadline";
        String priorityLabel = "Priority";

        printLine(String.format("%-3s %-25s %-35s %-12s %-12s",
                indexLabel, nameLabel, descriptionLabel, deadlineLabel, priorityLabel));
    }

    public void printAllTasks(Map<LocalDate, List<Task>> tasks) {
        Set<Map.Entry<LocalDate, List<Task>>> entries = tasks.entrySet();

        for (Map.Entry<LocalDate, List<Task>> entry : entries) {
            printLine("");
            printLine(HORIZONTAL_SEPARATOR + "Tasks of " + entry.getKey());
            printTasksWithIndex(entry.getValue());
        }
    }

    public void printTasksWithIndex(Collection<Task> tasks) {
        printHeaderRow();

        int index = 1;
        for (Task task : tasks) {
            String formattedIndex = String.format("%2s", index++);
            printLine(formattedIndex + ". " + task);
        }
    }
}
