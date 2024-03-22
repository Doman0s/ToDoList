package io.file;

import data.TaskDatabase;
import exception.FileWriteException;

import java.io.*;


public class SerializableFileManager implements FileManager {
    public static final String FILE_NAME = "src/db/database.obj";

    @Override
    public TaskDatabase readFromFile() {
        TaskDatabase database;

        try (
                FileInputStream fis = new FileInputStream(FILE_NAME);
                ObjectInputStream ois = new ObjectInputStream(fis)
        ){
            database = (TaskDatabase) ois.readObject();
        } catch (FileNotFoundException e) {
            throw new FileWriteException("File " + FILE_NAME + " not found.");
        } catch (IOException | ClassNotFoundException e) {
            throw new FileWriteException("Error while reading data from " + FILE_NAME);
        }

        return database;
    }

    @Override
    public void saveToFile(TaskDatabase database) {
        try (
                FileOutputStream fos = new FileOutputStream(FILE_NAME);
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ){
            oos.writeObject(database);
        } catch (FileNotFoundException e) {
            throw new FileWriteException("File " + FILE_NAME + " not found.");
        } catch (IOException e) {
            throw new FileWriteException("Error while writing data to " + FILE_NAME);
        }
    }
}