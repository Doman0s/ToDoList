import exception.InvalidPriorityException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

class DataReader {
    private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final Scanner scanner = new Scanner(System.in);
    private final ConsolePrinter printer = new ConsolePrinter();

    public Task readAndCreateTask() {
        printer.printLine("Name");
        String name = scanner.nextLine();
        printer.printLine("Description");
        String description = scanner.nextLine();
        printer.printLine("Deadline - format (YYYY-MM-DD)");
        LocalDate deadline = readDate();
        printer.printLine("Priority");
        Priority priority = readPriority();

        return new Task(name, description, deadline, priority);
    }

    private Priority readPriority() {
        printAllPriorities();
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

    private LocalDate readDate() {
        boolean dateOk = false;
        LocalDate date = null;

        do {
            try {
                date = LocalDate.parse(scanner.nextLine(), FORMATTER);
                dateOk = true;
            } catch (DateTimeParseException e) {
                printer.printLine("Incorrect date, required format (YYYY-MM-DD), try again.");
            }
        } while (!dateOk);

        return date;
    }

    int getInt() {
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

    void close() {
        scanner.close();
    }
}
