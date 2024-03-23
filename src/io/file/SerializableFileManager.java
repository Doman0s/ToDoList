package io.file;

import data.Database;
import exception.FileReadException;
import exception.FileWriteException;

import java.io.*;


public class SerializableFileManager implements FileManager {
    public static final String FILE_NAME = "src/db/database.obj";

    @Override
    public Database readFromFile() {
        try (
                FileInputStream fis = new FileInputStream(FILE_NAME);
                ObjectInputStream ois = new ObjectInputStream(fis)
        ){
            return  (Database) ois.readObject();
        } catch (FileNotFoundException e) {
            throw new FileReadException("File " + FILE_NAME + " not found.");
        } catch (IOException | ClassNotFoundException e) {
            throw new FileReadException("Error while reading data from " + FILE_NAME);
        }
    }

    @Override
    public void saveToFile(Database database) {
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