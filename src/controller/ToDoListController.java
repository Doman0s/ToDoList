package controller;

import data.Status;
import data.Task;
import data.TaskDatabase;
import exception.*;
import io.ConsolePrinter;
import io.DataReader;
import io.file.CsvFileManager;
import io.file.FileManager;
import io.file.FileManagerFactory;
import io.file.FileManagerType;

import java.time.LocalDate;
import java.util.*;

public class ToDoListController {
    private static final int QUICK_MENU_SIZE = 4;

    private final ConsolePrinter printer = new ConsolePrinter();
    private final DataReader reader = new DataReader(printer);

    private FileManager fileManager = new CsvFileManager();
    private TaskDatabase database;

    public ToDoListController() {
        try {
            FileManagerType type = reader.readFileManagerType();
            fileManager = FileManagerFactory.getFileManager(type);
            database = fileManager.readFromFile();
            printer.printLine("Data from file loaded successfully.");
        } catch (FileReadException | InvalidFileManagerTypeException e) {
            printer.printLine(e.getMessage());
            printer.printLine("Initializes new database.");
            database = new TaskDatabase();
        }
    }

    public void mainLoop() {
        Option option;

        do {
            printMenu();
            option = readOption();
            processOption(option);
        } while (option != Option.EXIT);
    }

    private void printMenu() {
        printer.printLine("");
        printer.printLine("------QUICK-MENU------");
        printQuickMenuOptions();
        printer.printLine("---------MENU---------");
        printRemainingOptions();
    }

    private void printQuickMenuOptions() {
        for (int i = 0; i < QUICK_MENU_SIZE; i++) {
            printer.printLine(Option.values()[i].toString());
        }
    }

    private void printRemainingOptions() {
        for (int i = QUICK_MENU_SIZE; i < Option.values().length; i++) {
            printer.printLine(Option.values()[i].toString());
        }
    }

    private Option readOption() {
        boolean optionOk = false;
        Option option = null;
        printer.printLine(">");

        do {
            try {
                option = Option.createFromInt(reader.getInt());
                optionOk = true;
            } catch (InvalidOptionException e) {
                printer.printLine(e.getMessage());
            }
        } while (!optionOk);

        return option;
    }

    private void processOption(Option option) {
        switch (option) {
            case EXIT -> exit();
            case PRINT_TODAYS_TASKS -> printTodaysTasks();
            case ADD_TASK -> addTask();
            case END_TODAYS_TASK -> endTodaysTask();
            case PRINT_FUTURE_TASKS -> printFutureTasks();
            case END_CUSTOM_TASK -> endCustomTask();
            case EDIT_TASK -> editTask();
            case DELETE_TASK -> deleteTask();
            case FIND_TASK -> findTaskByName();
            case FILTER_TASKS -> filterTasks();
            case SHOW_STATISTICS -> showStatistics();
            case CLEAR_STATISTICS -> clearStatistics();
            case SHOW_HISTORY -> showHistory();
            case CLEAR_HISTORY -> clearHistory();
        }
    }

    private void exit() {
        reader.close();
        try {
            fileManager.saveToFile(database);
        } catch (FileWriteException e) {
            printer.printLine(e.getMessage());
        }
        printer.printLine("Leaving the application, until next time!");
    }

    private void printTodaysTasks() {
        LocalDate currentDate = LocalDate.now();
        List<Task> tasks = database.findTasksByDate(currentDate);
        printer.printTasks(tasks);
    }

    private void addTask() {
        try {
            Task task = reader.readAndCreateTask();
            database.addTask(task);
            printer.printLine("exception.Task added successfully.");
        } catch (NullPointerException | DeadlineDateMustBeFuture e) {
            printer.printLine(e.getMessage());
        }
    }

    private void endTodaysTask() {
        LocalDate currentDate = LocalDate.now();
        List<Task> tasks = database.findTasksByDate(currentDate);
        if (tasks != null && !tasks.isEmpty()) {
            Task task = tasks.removeFirst();
            task.setStatus(Status.DONE);
            database.addToHistory(task);
            TaskDatabase.tasksCompleted++;
            printer.printLine("The task " + tasks.getFirst().getName() + " has been completed.");
        } else {
            printer.printLine("Today's task list is empty.");
        }
    }

    private void printFutureTasks() {
        // get all tasks with dates after today's date
        database.getTasks().entrySet().stream()
                .filter(entry -> entry.getKey().isAfter(LocalDate.now()))
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .map(Task::toString)
                .forEach(printer::printLine);
    }

    private void endCustomTask() {
        LocalDate date = reader.readDate();
        printTasksFromDateWithIndex(date);

        int id = getTaskNumber();

        try {
            Task task = database.getTasks().get(date).get(id);
            database.removeTask(date, task);
            database.addToHistory(task);
        } catch (ArrayIndexOutOfBoundsException e) {
            printer.printLine("Incorrect task id.");
        }
    }

    private void editTask() {
        LocalDate date = reader.readDate();
        printTasksFromDateWithIndex(date);

        int id = getTaskNumber();

        try {
            Task task = database.getTasks().get(date).get(id);
            printer.printLine("Old task data");
            printer.printLine(task.toString());
            printer.printLine("Enter new task data");
            Task editedTask = reader.readAndCreateTask();
            database.removeTask(date, task);
            database.addTask(editedTask);
        } catch (ArrayIndexOutOfBoundsException e) {
            printer.printLine("Incorrect task id.");
        } catch (NullPointerException | DeadlineDateMustBeFuture e) {
            printer.printLine(e.getMessage());
        }
    }

    private void deleteTask() {
        LocalDate date = reader.readDate();
        printTasksFromDateWithIndex(date);

        int id = getTaskNumber();

        try {
            Task task = database.getTasks().get(date).get(id);
            database.removeTask(date, task);
        } catch (ArrayIndexOutOfBoundsException e) {
            printer.printLine("Incorrect task id.");
        }
    }

    private void printTasksFromDateWithIndex(LocalDate date) {
        printer.printLine("Enter the date for the task operation.");
        List<Task> tasksByDate = database.findTasksByDate(date);
        printer.printTasksWithIndex(tasksByDate);
    }

    private int getTaskNumber() {
        printer.printLine("Enter the task number.");
        return reader.getInt() - 1;
    }

    private void findTaskByName() {
        printer.printLine("Enter task name");
        String taskName = reader.readLine();
        printer.printTasks(database.findTaskByName(taskName));
    }

    private void filterTasks() {
        printer.printLine("Enter the date of tasks to be displayed.");
        LocalDate filterDate = reader.readDate();
        printer.printTasks(database.findTasksByDate(filterDate));
    }

    private void showStatistics() {
        String statistics = "Statistics" +
                "Number of all created tasks: " + TaskDatabase.tasksCreated +
                "Number of completed tasks: " + TaskDatabase.tasksCompleted +
                "Number of failed tasks: " + TaskDatabase.tasksFailed;
        printer.printLine(statistics);
    }

    private void clearStatistics() {
        database.clearStatistics();
        printer.printLine("Statistics cleared successfully.");
    }

    private void showHistory() {
        printer.printLine("History");
        printer.printTasks(database.getTasksHistory());
    }

    private void clearHistory() {
        database.clearHistory();
        printer.printLine("History deleted successfully.");
    }

    private enum Option {
        EXIT(0, "Exit application"),
        PRINT_TODAYS_TASKS(1, "Print today's tasks"),
        ADD_TASK(2, "Add new task"),
        END_TODAYS_TASK(3, "End today's top task"),
        PRINT_FUTURE_TASKS(4, "Print all tasks"),
        END_CUSTOM_TASK(5, "End custom task"),
        EDIT_TASK(6, "Edit task"),
        DELETE_TASK(7, "Delete task"),
        FIND_TASK(8, "Find task by name"),
        FILTER_TASKS(9, "Filter tasks"),
        SHOW_STATISTICS(10, "Show statistics"),
        CLEAR_STATISTICS(11, "Clear statistics"),
        SHOW_HISTORY(12, "Show task history"),
        CLEAR_HISTORY(13, "Clear history");

        private final int value;
        private final String description;

        Option(int value, String description) {
            this.value = value;
            this.description = description;
        }

        public int getValue() {
            return value;
        }

        public String getDescription() {
            return description;
        }

        static Option createFromInt(int value) {
            try {
                return Option.values()[value];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new InvalidOptionException("Invalid option number: " + value + ", try again.");
            }
        }

        @Override
        public String toString() {
            return value + " - " + description;
        }
    }
}
