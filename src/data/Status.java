package data;

import exception.InvalidPriorityException;

public enum Status {
    TO_DO(1),
    DONE(2),
    FAILED(3);

    private final int value;

    Status(int value) {
        this.value = value;
    }

    static Status createFromInt(int value) {
        try {
            return values()[value - 1];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InvalidPriorityException("Invalid status number: " + value + ", try again.");
        }
    }

    @Override
    public String toString() {
        return value + " - " + name();
    }
}