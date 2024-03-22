package io.file;

public class FileManagerFactory {
    public static FileManager getFileManager(FileManagerType type) {
        return switch (type) {
            case CSV -> getCsvFileManager();
            case SERIALIZABLE -> getSerializableFileManager();
        };
    }

    private static FileManager getCsvFileManager() {
        return new CsvFileManager();
    }

    private static FileManager getSerializableFileManager() {
        return new SerializableFileManager();
    }
}
