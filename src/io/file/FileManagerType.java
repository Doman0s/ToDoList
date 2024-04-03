package io.file;

import exception.InvalidFileManagerTypeException;

public enum FileManagerType {
    CSV(1, "Writing and reading files in csv form"),
    SERIALIZABLE(2, "Serialized file writing and reading");

    private final int value;
    private final String description;

    FileManagerType(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static FileManagerType createFromInt(int value) {
        try {
            return FileManagerType.values()[value - 1];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InvalidFileManagerTypeException(
                    "Invalid File manager number: " + value + ", can't read data from file, try again.");
        }
    }

    @Override
    public String toString() {
        return value + " - " + name() + " (" + description + ")";
    }
}
