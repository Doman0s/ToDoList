import exception.DeadlineDateMustBeFuture;
import exception.InvalidOptionException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

class ToDoListController {
    private static final int QUICK_MENU_SIZE = 4;

    ConsolePrinter printer = new ConsolePrinter();
    DataReader reader = new DataReader();
    TaskDatabase database = new TaskDatabase();

    void mainLoop() {
        Option option;

        do {
            printMenu();
            option = readOption();
            processOption(option);
        } while (option != Option.EXIT);
    }

    private void printMenu() {
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
            case FIND_TASK -> findTask();
            case FILTER_TASKS -> filterTasks();
            case SHOW_STATISTICS -> showStatistics();
            case CLEAR_STATISTICS -> clearStatistics();
            case SHOW_HISTORY -> showHistory();
            case CLEAR_HISTORY -> clearHistory();
        }
    }

    private void exit() {
        reader.close();
        printer.printLine("Leaving the application, until next time!");
    }

    private void printTodaysTasks() {
        LocalDate CurrentDate = LocalDate.now();
        List<Task> tasks = database.getTasks().get(CurrentDate);
        printer.printTasks(tasks);
    }

    private void addTask() {
        try {
            Task task = reader.readAndCreateTask();
            database.addTask(task);
            printer.printLine("Task added successfully.");
        } catch (NullPointerException | DeadlineDateMustBeFuture e) {
            printer.printLine(e.getMessage());
        }
    }

    private void endTodaysTask() {
        LocalDate currentDate = LocalDate.now();
        List<Task> tasks = database.getTasks().get(currentDate);
        if (!tasks.isEmpty()) {
            Task task = tasks.removeFirst();
            database.addToHistory(task);
            printer.printLine("The task " + tasks.getFirst().getName() + " has been completed.");
        } else {
            printer.printLine("Today's task list is empty.");
        }
    }

    private void printFutureTasks() {
        database.getTasks().entrySet().stream()
                .filter(entry -> !entry.getKey().equals(LocalDate.now()))
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .map(Task::toString)
                .forEach(printer::printLine);
    }

    private void endCustomTask() {
        printer.printLine("Enter the date from which you want to complete the task.");
        LocalDate date = reader.readDate();
        List<Task> tasks = database.getTasks().get(date);

        for (int i = 0; i < tasks.size(); i++) {
            printer.printLine(i + ". " + tasks.get(i));
        }

        printer.printLine("Enter the task number.");
        int id = reader.getInt() - 1;

        try {
            Task task = database.getTasks().get(date).get(id);
            database.removeTask(date, task);
            database.addToHistory(task);
        } catch (ArrayIndexOutOfBoundsException e) {
            printer.printLine("Incorrect task id.");
        }
    }

    private void editTask() {
        printer.printLine("Enter the date from which you want to edit the task.");
        LocalDate date = reader.readDate();
        List<Task> tasks = database.getTasks().get(date);

        for (int i = 0; i < tasks.size(); i++) {
            printer.printLine(i + ". " + tasks.get(i));
        }

        printer.printLine("Enter the task number.");
        int id = reader.getInt() - 1;
        Task task;

        try {
            task = database.getTasks().get(date).get(id);
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
        printer.printLine("Enter the date from which you want to delete the task.");
        LocalDate date = reader.readDate();
        List<Task> tasks = database.getTasks().get(date);

        for (int i = 0; i < tasks.size(); i++) {
            printer.printLine(i + ". " + tasks.get(i));
        }

        printer.printLine("Enter the task number.");
        int id = reader.getInt() - 1;

        try {
            Task task = database.getTasks().get(date).get(id);
            database.removeTask(date, task);
        } catch (ArrayIndexOutOfBoundsException e) {
            printer.printLine("Incorrect task id.");
        }
    }

    private void findTask() {
        printer.printLine("Enter task name");
        String taskName = reader.readLine();
        Collection<Task> foundTasks = database.findTaskByName(taskName);
        printer.printTasks(foundTasks);
    }

    private void filterTasks() {
        printer.printLine("Enter the date of tasks to be displayed.");
        LocalDate filterDate = reader.readDate();
        Collection<Task> foundTasks = database.findTasksByDate(filterDate);
        printer.printTasks(foundTasks);
    }

    private void showStatistics() {
        printer.printLine("Statistics");
        printer.printLine("Number of all created tasks: " + TaskDatabase.tasksCreated);
        printer.printLine("Number of completed tasks: " + TaskDatabase.tasksCompleted);
        printer.printLine("Number of failed tasks: " + TaskDatabase.tasksFailed);
    }

    private void clearStatistics() {
        TaskDatabase.tasksCreated = 0;
        TaskDatabase.tasksCompleted = 0;
        TaskDatabase.tasksFailed = 0;
        printer.printLine("Statistics deleted successfully.");
    }

    private void showHistory() {
        printer.printLine("History");
        List<Task> tasksHistory = database.getTasksHistory();
        printer.printTasks(tasksHistory);
    }

    private void clearHistory() {
        database.getTasksHistory().clear();
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
