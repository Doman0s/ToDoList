package io.file;

import exception.InvalidFileManagerTypeException;
import io.ConsolePrinter;
import io.DataReader;

public class FileManagerFactory {
    private final DataReader reader;
    private final ConsolePrinter printer;

    public FileManagerFactory(DataReader reader, ConsolePrinter printer) {
        this.reader = reader;
        this.printer = printer;
    }

    public FileManager getFileManager() {
        FileManagerType type = readCorrectFileManagerType();

        return switch (type) {
            case CSV -> getCsvFileManager();
            case SERIALIZABLE -> getSerializableFileManager();
            default -> throw new IllegalArgumentException("File manager like that doesn't exist");
        };
    }

    private FileManagerType readCorrectFileManagerType() {
        printer.printLine("Specify in what form you want to read and write data.");
        printFileManagerTypes();

        FileManagerType type = null;
        boolean typeOk = false;

        do {
            try {
                type = reader.readFileManagerType();
                typeOk = true;
            } catch (InvalidFileManagerTypeException e) {
                printer.printLine(e.getMessage());
            }
        } while (!typeOk);

        return type;
    }

    private void printFileManagerTypes() {
        for (FileManagerType value : FileManagerType.values()) {
            printer.printLine(value.toString());
        }
    }

    private static FileManager getCsvFileManager() {
        return new CsvFileManager();
    }

    private static FileManager getSerializableFileManager() {
        return new SerializableFileManager();
    }
}
