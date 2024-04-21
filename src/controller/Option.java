package controller;

import exception.InvalidOptionException;

public enum Option {
    EXIT(0, "Exit application"),
    PRINT_TODAYS_TASKS(1, "Print today's tasks"),
    PRINT_TOMORROWS_TASKS(2, "Print tomorrow's tasks"),
    ADD_TASK(3, "Add new task"),
    END_TODAYS_TASK(4, "End today's top task"),
    PRINT_TASK_FULL_DATA(5, "Print full task details"),
    PRINT_FUTURE_TASKS(6, "Print all tasks"),
    END_CUSTOM_TASK(7, "End custom task"),
    EDIT_TASK(8, "Edit task"),
    DELETE_TASK(9, "Delete task"),
    FIND_TASK_BY_NAME(10, "Find task by name"),
    FIND_TASKS_BY_DATE(11, "Find tasks by date"),
    SHOW_STATISTICS(12, "Show statistics"),
    CLEAR_STATISTICS(13, "Clear statistics"),
    SHOW_HISTORY(14, "Show task history"),
    CLEAR_HISTORY(15, "Clear history");

    private final int value;
    private final String description;

    Option(int value, String description) {
        this.value = value;
        this.description = description;
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
        String formattedValue = String.format("%2d", value);
        return formattedValue + " - " + description;
    }
}