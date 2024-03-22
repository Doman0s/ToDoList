package data;

import exception.InvalidPriorityException;

public enum Priority {
    LOW(1),
    MEDIUM(2),
    HIGH(3),
    URGENT(4);

    private final int value;

    Priority(int value) {
        this.value = value;
    }

    public static Priority createFromInt(int value) {
        try {
            return values()[value - 1];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InvalidPriorityException("Invalid priority number: " + value + ", try again.");
        }
    }

    @Override
    public String toString() {
        return value + " - " + name();
    }
}