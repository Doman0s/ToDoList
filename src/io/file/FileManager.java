package io.file;

import data.TaskDatabase;

public interface FileManager {
    TaskDatabase readFromFile();
    void saveToFile(TaskDatabase database);
}
