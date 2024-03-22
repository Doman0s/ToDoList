package io.file;

import data.Priority;
import data.Status;
import data.Task;
import data.TaskDatabase;
import exception.FileReadException;
import exception.FileWriteException;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.List;

public class CsvFileManager implements FileManager {
    public static final String FILE_NAME = "src/db/database.csv";
    private static final String HISTORY_SEPARATOR = "HISTORY";

    @Override
    public TaskDatabase readFromFile() {
        TaskDatabase database = new TaskDatabase();

        try (
                FileReader fileReader = new FileReader(FILE_NAME);
                BufferedReader reader = new BufferedReader(fileReader)
        ){
            readTasksFromFile(reader, database);
            readHistoryFromFile(reader, database);
        } catch (FileNotFoundException e) {
            throw new FileReadException("File " + FILE_NAME + " not found.");
        } catch (IOException e) {
            throw new FileReadException("Error while reading data from " + FILE_NAME);
        } catch (DateTimeParseException | IllegalArgumentException e) {
            throw new FileReadException("Incorrect data in " + FILE_NAME);
        }

        return database;
    }

    private void readTasksFromFile(BufferedReader reader, TaskDatabase database) throws IOException {
        reader.lines()
                .takeWhile(line -> !line.equals(HISTORY_SEPARATOR)) // read until HISTORY_SEPARATOR
                .map(this::createTaskFromString)
                .forEach(database::addTask);
    }

    private void readHistoryFromFile(BufferedReader reader, TaskDatabase database) throws IOException {
        reader.lines()
                .skip(1) // ignoring HISTORY_SEPARATOR
                .map(this::createTaskFromString)
                .forEach(database::addToHistory);
    }

    private Task createTaskFromString(String line) {
        String[] splitLine = line.split(";");

        String name = splitLine[0];
        String description = splitLine[1];
        LocalDate creationDate = LocalDate.parse(splitLine[2]);
        LocalDate deadline = LocalDate.parse(splitLine[3]);
        Status status = Status.valueOf(splitLine[4]);
        Priority priority = Priority.valueOf(splitLine[5]);

        return new Task(name, description, creationDate, deadline, status, priority);
    }

    @Override
    public void saveToFile(TaskDatabase database) {
        try (
            FileWriter fileWriter = new FileWriter(FILE_NAME);
            BufferedWriter writer = new BufferedWriter(fileWriter)
        ){
            writeTasksToFile(writer, getAllTasksFromDatabase(database));
            writer.write(HISTORY_SEPARATOR);
            writeTasksToFile(writer, database.getTasksHistory());
        } catch (IOException e) {
            throw new FileWriteException("Error while writing data to " + FILE_NAME);
        }
    }

    private void writeTasksToFile(BufferedWriter writer, List<Task> tasks) throws IOException {
        StringBuilder builder = new StringBuilder();
        tasks.forEach(task ->
                builder.append(task.toCsv()).append(System.lineSeparator()));
        writer.write(builder.toString());
    }

    private List<Task> getAllTasksFromDatabase(TaskDatabase database) {
        return database.getTasks().values()
                .stream()
                .flatMap(Collection::stream)
                .toList();
    }
}
