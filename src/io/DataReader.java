package io;

import exception.InvalidPriorityException;
import data.Priority;
import data.Task;
import io.file.FileManagerType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class DataReader {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final Scanner scanner = new Scanner(System.in);
    private final ConsolePrinter printer;

    public DataReader(ConsolePrinter printer) {
        this.printer = printer;
    }

    public Task readAndCreateTask() {
        printer.printLine("Create a new task");
        printer.printLine("Name");
        String name = scanner.nextLine();

        printer.printLine("Description");
        String description = scanner.nextLine();

        printer.printLine("Deadline - format (DD-MM-YYYY)");
        LocalDate deadline = readDate();

        printer.printLine("Priority");
        printAllPriorities();
        Priority priority = readPriority();

        return new Task(name, description, deadline, priority);
    }

    public boolean readAndEditTask(Task task) {
        boolean editedCorrectly = true;

        printer.printLine("");
        printer.printLine("Old task data");
        printer.printLine(task.getFullInfo());

        String[] taskData = readDataForTaskEditing();

        if (!taskData[2].isEmpty()) {
            try {
                LocalDate deadline = LocalDate.parse(taskData[2], FORMATTER);
                if (deadline.isAfter(LocalDate.now())) {
                    task.setDeadline(deadline);
                } else {
                    printer.printLine("Deadline date must be in the future.");
                }
            } catch (DateTimeParseException e) {
                printer.printLine("Incorrect date, required format (DD-MM-YYYY).");
                return !editedCorrectly;
            }
        }

        if (!taskData[3].isEmpty()) {
            try {
                int value = Integer.parseInt(taskData[3]);
                Priority priority = Priority.createFromInt(value);
                task.setPriority(priority);
            } catch (NumberFormatException e) {
                printer.printLine("Incorrect priority value.");
                return !editedCorrectly;
            } catch (InvalidPriorityException e) {
                printer.printLine(e.getMessage());
                return !editedCorrectly;
            }
        }

        if (!taskData[0].isEmpty())
            task.setName(taskData[0]);

        if (!taskData[1].isEmpty())
            task.setDescription(taskData[1]);

        return editedCorrectly;
    }

    //reading all data as String
    public String[] readDataForTaskEditing() {
        String[] taskData = new String[4];

        printer.printLine("\nEnter new data (leave empty to keep old value):");
        printer.printLine("Name");
        taskData[0] = scanner.nextLine();

        printer.printLine("Description");
        taskData[1] = scanner.nextLine();

        printer.printLine("Deadline - format (DD-MM-YYYY)");
        taskData[2] = scanner.nextLine();

        printer.printLine("Priority");
        printAllPriorities();
        taskData[3] = scanner.nextLine();

        return taskData;
    }

    private Priority readPriority() {
        boolean priorityOk = false;
        Priority priority = null;

        do {
            try {
                priority = Priority.createFromInt(getInt());
                priorityOk = true;
            } catch (InvalidPriorityException e) {
                printer.printLine(e.getMessage());
            }
        } while (!priorityOk);

        return priority;
    }

    private void printAllPriorities() {
        for (Priority priority : Priority.values()) {
            printer.printLine(priority.toString());
        }
    }

    public LocalDate readDate() {
        boolean dateOk = false;
        LocalDate date = null;

        do {
            try {
                date = LocalDate.parse(scanner.nextLine(), FORMATTER);
                if (date.isBefore(LocalDate.now())) {
                    printer.printLine("Deadline date must be in the future, try again.");
                } else {
                    dateOk = true;
                }
            } catch (DateTimeParseException e) {
                printer.printLine("Incorrect date, required format (DD-MM-YYYY), try again.");
            }
        } while (!dateOk);

        return date;
    }

    public FileManagerType readFileManagerType() {
        return FileManagerType.createFromInt(getInt());
    }

    public int getInt() {
        boolean intOk = false;
        int value = 0;

        do {
            try {
                value = scanner.nextInt();
                intOk = true;
            } catch (InputMismatchException e) {
                System.out.println("Value must be an integer, try again.");
            } finally {
                scanner.nextLine();
            }
        } while (!intOk);

        return value;
    }

    public String readLine() {
        return scanner.nextLine();
    }

    public void close() {
        scanner.close();
    }
}
