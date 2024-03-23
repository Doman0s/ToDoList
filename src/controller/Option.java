package controller;

import exception.InvalidOptionException;

public enum Option {
    EXIT(0, "Exit application"),
    PRINT_TODAYS_TASKS(1, "Print today's tasks"),
    PRINT_TOMORROWS_TASKS(2, "Print tomorrow's tasks"),
    ADD_TASK(3, "Add new task"),
    END_TODAYS_TASK(4, "End today's top task"),
    PRINT_FUTURE_TASKS(5, "Print all tasks"),
    END_CUSTOM_TASK(6, "End custom task"),
    EDIT_TASK(7, "Edit task"),
    DELETE_TASK(8, "Delete task"),
    FIND_TASK_BY_NAME(9, "Find task by name"),
    FIND_TASKS_BY_DATE(10, "Find tasks by date"),
    SHOW_STATISTICS(11, "Show statistics"),
    CLEAR_STATISTICS(12, "Clear statistics"),
    SHOW_HISTORY(13, "Show task history"),
    CLEAR_HISTORY(14, "Clear history");

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
