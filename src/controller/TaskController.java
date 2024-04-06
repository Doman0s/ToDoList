package controller;

import data.Task;
import data.Database;
import exception.*;
import io.ConsolePrinter;
import io.DataReader;
import io.file.FileManager;
import io.file.FileManagerFactory;
import service.TaskService;

import java.time.LocalDate;
import java.util.*;

public class TaskController {
    private final ConsolePrinter printer = new ConsolePrinter();
    private final DataReader reader = new DataReader(printer);

    private TaskService taskService;
    private FileManager fileManager;

    public TaskController() {
        try {
            fileManager = new FileManagerFactory(reader, printer).getFileManager();
            taskService = new TaskService(fileManager.readFromFile());
            checkDatabaseForFailedTasksAndFailThem();
            printer.printLine("Data from file loaded successfully.");
        } catch (FileReadException | IllegalArgumentException e) {
            printer.printLine(e.getMessage());
            printer.printLine("Initializes new database.");
            taskService = new TaskService(new Database());
        }
    }

    //TODO
    //1.change the listing style, instead sout use souf with custom length for all variables
    //2.is loop for reading a date from user when ending custom task really necessary?
    //3.checking if list with tasks is null or empty should be in service (only once), repeating this
    //operation in controller is bad
    //4.problems with reading empty statistics or date data
    //5.code is repeating in methods 7, 8, 9
    //6.NullPointers when trying to remove null instead of Task object
    //7.when error occur while reading statistics, history and tasks should still be loaded...
    //8.ArrayList to store sorted tasks (sorting them all the time) is a bad idea...
    //9.still problems with reading serializable object

    //TESTING: options 0, 1, 2, 3, 4, 5, 6, 8, 9, 10, 13, 14 are working just fine (including exception handling)

    public void mainLoop() {
        Option option;

        do {
            printer.printMenu();
            option = readOption();
            processOption(option);
        } while (option != Option.EXIT);
    }

    private void checkDatabaseForFailedTasksAndFailThem() {
        List<Task> tasksToFail = taskService.getTasks().entrySet().stream()
                // Find unfinished tasks whose deadline has passed and fail them
                .filter(entry -> entry.getKey().isBefore(LocalDate.now()))
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .toList();

        for (Task task : tasksToFail) {
            taskService.failTask(task);
        }
    }

    private Option readOption() {
        boolean optionOk = false;
        Option option = null;
        printer.promptCharacter();

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
            case PRINT_TOMORROWS_TASKS -> printTomorrowsTasks();
            case ADD_TASK -> addTask();
            case END_TODAYS_TASK -> endTodaysTopTask();
            case PRINT_FUTURE_TASKS -> printFutureTasks();
            case END_CUSTOM_TASK -> endCustomTask();
            case EDIT_TASK -> editTask();
            case DELETE_TASK -> deleteTask();
            case FIND_TASK_BY_NAME -> findTaskByName();
            case FIND_TASKS_BY_DATE -> filterTasks();
            case SHOW_STATISTICS -> showStatistics();
            case CLEAR_STATISTICS -> clearStatistics();
            case SHOW_HISTORY -> showHistory();
            case CLEAR_HISTORY -> clearHistory();
        }
    }

    private void exit() {
        try {
            fileManager.saveToFile(taskService.getDatabase());
            printer.printLine("Data to file exported successfully.");
        } catch (FileWriteException e) {
            printer.printLine(e.getMessage());
        }
        reader.close();
        printer.printLine("Leaving the application, until next time!");
    }

    private void printTodaysTasks() {
        printTasksForDate(LocalDate.now());
    }

    private void printTomorrowsTasks() {
        LocalDate tomorrowsDate = LocalDate.now().plusDays(1);
        printTasksForDate(tomorrowsDate);
    }

    private void printTasksForDate(LocalDate date) {
        List<Task> tasks = taskService.findTasksByDate(date);
        printer.printLine("Tasks of " + date);
        if (tasks == null || tasks.isEmpty()) {
            printer.printLine("No tasks found.");
        } else {
            printer.printTasksWithIndex(tasks);
        }
    }

    private void addTask() {
        Task task = reader.readAndCreateTask();
        taskService.addTask(task);
        printer.printLine("Task added successfully.");
    }

    private void endTodaysTopTask() {
        List<Task> tasks = taskService.findTasksByDate(LocalDate.now());

        taskService.endAndReturnFirstTask(tasks).ifPresentOrElse(
                task -> printer.printLine("The task \"" + task.getName() + "\" has been completed."),
                () -> printer.printLine("Today's task list is empty.")
        );
    }

    private void printFutureTasks() {
        Map<LocalDate, List<Task>> tasks = taskService.getTasks();

        if (tasks == null || tasks.isEmpty()) {
            printer.printLine("The task list is empty.");
        } else {
            printer.printAllTasks(tasks);
        }
    }

    //TODO test these 3 methods below for exceptions
    private void endCustomTask() {
        printer.printLine("Enter the date for the task operation, format (DD-MM-YYYY).");
        printer.promptCharacter();
        LocalDate date = reader.readDate();
        List<Task> tasks = taskService.findTasksByDate(date);

        if (tasks != null && !tasks.isEmpty()) {
            printer.printTasksWithIndex(tasks);
            int index = getTaskIndex();

            try {
                Task task = taskService.getTaskByDateAndIndex(date, index);
                taskService.endTask(task);
                printer.printLine("Task \"" + task.getName() + "\" completed successfully.");
            } catch (IndexOutOfBoundsException e) {
                printer.printLine("Incorrect task number.");
            }
        } else {
            printer.printLine("No tasks found for date " + date + ".");
        }
    }

    private void editTask() {
        LocalDate date = getDateAndPrintTasksForThisDate();
        int index = getTaskIndex();

        try {
            Task task = taskService.getTaskByDateAndIndex(date, index);
            boolean editedSuccessfully = reader.readAndEditTask(task);
            if (editedSuccessfully)
                printer.printLine("Task  \"" + task.getName() + "\" edited successfully.");
        } catch (ArrayIndexOutOfBoundsException e) {
            printer.printLine("Incorrect task number.");
        } catch (NullPointerException | DeadlineDateMustBeFuture e) {
            printer.printLine(e.getMessage());
        }
    }

    private void deleteTask() {
        printer.printLine("Enter the date for the task operation, format (DD-MM-YYYY).");
        printer.promptCharacter();
        LocalDate date = reader.readDate();
        List<Task> tasks = taskService.findTasksByDate(date);

        if (tasks != null && !tasks.isEmpty()) {
            printer.printTasksWithIndex(tasks);
            int index = getTaskIndex();

            try {
                Task task = taskService.getTaskByDateAndIndex(date, index);
                taskService.removeTask(task);
                printer.printLine("Task \"" + task.getName() + "\" deleted successfully.");
            } catch (IndexOutOfBoundsException e) {
                printer.printLine("Incorrect task number.");
            }
        } else {
            printer.printLine("No tasks found for date " + date + ".");
        }
    }

    private LocalDate getDateAndPrintTasksForThisDate() {
        printer.printLine("Enter the date for the task operation, format (DD-MM-YYYY).");
        printer.promptCharacter();
        LocalDate date = reader.readDate();
        printTasksForDate(date);

        return date;
    }

    private int getTaskIndex() {
        printer.printLine("Enter the task number.");
        printer.promptCharacter();

        return reader.getInt() - 1;
    }

    private void findTaskByName() {
        printer.printLine("Enter task name");
        printer.promptCharacter();
        String taskName = reader.readLine();
        Collection<Task> taskByName = taskService.findTaskByName(taskName);

        if (taskByName == null || taskByName.isEmpty()) {
            printer.printLine("No tasks found.");
        } else {
            printer.printTasks(taskByName);
        }
    }

    private void filterTasks() {
        printer.printLine("Enter the date of tasks to be displayed, format (DD-MM-YYYY).");
        printer.promptCharacter();
        LocalDate filterDate = reader.readDate();
        Collection<Task> taskByName = taskService.findTasksByDate(filterDate);

        if (taskByName == null || taskByName.isEmpty()) {
            printer.printLine("No tasks found.");
        } else {
            printer.printTasksWithIndex(taskByName);
        }
    }

    private void showStatistics() {
        printer.printLine(taskService.getStatistics());
    }

    private void clearStatistics() {
        taskService.clearStatistics();
        printer.printLine("Statistics cleared successfully.");
    }

    private void showHistory() {
        List<Task> history = taskService.getHistory();

        if (history == null || history.isEmpty()) {
            printer.printLine("History is empty.");
        } else {
            printer.printLine("History");
            printer.printTasks(taskService.getHistory());
        }
    }

    private void clearHistory() {
        taskService.clearHistory();
        printer.printLine("History deleted successfully.");
    }
}
