package io.file;

import data.Database;

public interface FileManager {
    Database readFromFile();
    void saveToFile(Database database);
}
