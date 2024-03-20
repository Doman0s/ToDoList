import exception.InvalidOptionException;

class ToDoListController {
    ConsolePrinter printer = new ConsolePrinter();
    DataReader reader = new DataReader();

    void mainLoop() {
        Option option;

        do {
            printMenu();
            option = readOption();
            processOption(option);
        } while (option != Option.EXIT);
    }

    private void printMenu() {
        printer.printLine("---------MENU---------");
        printOptions();
    }

    private void printOptions() {
        for (Option option : Option.values()) {
            printer.printLine(option.toString());
        }
    }

    private Option readOption() {
        boolean optionOk = false;
        Option option = null;
        printer.printLine("Choice: ");

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
            case ADD_TASK -> addTask();
        }
    }

    private void exit() {
        reader.close();
        printer.printLine("Leaving the application, until next time!");
    }

    private void addTask() {
        printer.printLine("Create a new task");
        Task task = reader.readAndCreateTask();
        printer.printLine("Task added successfully.");
    }

    private enum Option {
        EXIT(0, "Exit application"),
        PRINT_TASKS(1, "Print tasks to do"),
        ADD_TASK(2, "Add new task"),
        EDIT_TASK(3, "Edit task"),
        DELETE_TASK(4, "Delete task"),
        FIND_TASK(5, "Find task by name"),
        FILTER_TASKS(6, "Filter tasks"),
        CHANGE_TASK_STATUS(7, "Change task status"),
        END_TASK(8, "End task"),
        SHOW_STATISTICS(9, "Show statistics"),
        SHOW_HISTORY(10, "Show task history");

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
