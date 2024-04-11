package io.file;

import data.Priority;
import data.Status;
import data.Task;
import data.Database;
import exception.FileReadException;
import exception.FileWriteException;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class CsvFileManager implements FileManager {
    public static final String FILE_NAME = "src/db/database.csv";
    private static final String HISTORY_SEPARATOR = "HISTORY";
    private static final String STATISTICS_SEPARATOR = "STATISTICS";

    @Override
    public Database readFromFile() {
        Database database = new Database();
        AtomicLong lineNumber = new AtomicLong(0);

        try (
                FileReader fileReader = new FileReader(FILE_NAME);
                BufferedReader reader = new BufferedReader(fileReader)
        ){
            readTasksFromFile(reader, database, lineNumber);
            readHistoryFromFile(reader, database, lineNumber);
            readStatisticsFromFile(reader, database, lineNumber);
        } catch (FileNotFoundException e) {
            throw new FileReadException("File " + FILE_NAME + " not found.");
        } catch (IOException e) {
            throw new FileReadException("Error while reading data from " + FILE_NAME);
        } catch (DateTimeParseException | IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            throw new FileReadException("Incorrect data in " + FILE_NAME + " in line " + lineNumber.get() + ".");
        }

        return database;
    }

    private void readTasksFromFile(BufferedReader reader, Database database, AtomicLong lineNumber) throws IOException {
        reader.lines()
                .takeWhile(line -> !line.equals(HISTORY_SEPARATOR)) // read until HISTORY_SEPARATOR
                .peek(line -> lineNumber.incrementAndGet())
                .map(this::createTaskFromString)
                .forEach(database::addTask);
    }

    private void readHistoryFromFile(BufferedReader reader, Database database, AtomicLong lineNumber) throws IOException {
        lineNumber.incrementAndGet(); // adding HISTORY_SEPARATOR to line counter

        reader.lines()
                .takeWhile(line -> !line.equals(STATISTICS_SEPARATOR))
                .peek(line -> lineNumber.incrementAndGet())
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

    private void readStatisticsFromFile(BufferedReader reader, Database database, AtomicLong lineNumber) throws IOException {
        lineNumber.incrementAndGet(); // adding STATISTICS_SEPARATOR to line counter
        lineNumber.incrementAndGet(); // adding line with statistics to the line counter

        String[] splitLine = reader.readLine().split(";");
        database.setTasksCreated(Integer.parseInt(splitLine[0]));
        database.setTasksCompleted(Integer.parseInt(splitLine[1]));
        database.setTasksFailed(Integer.parseInt(splitLine[2]));
    }

    @Override
    public void saveToFile(Database database) {
        try (
            FileWriter fileWriter = new FileWriter(FILE_NAME);
            BufferedWriter writer = new BufferedWriter(fileWriter)
        ){
            writeTasksToFile(writer, getAllTasksFromDatabase(database));
            writer.write(HISTORY_SEPARATOR);
            writer.newLine();
            writeTasksToFile(writer, database.getTasksHistory());
            writer.write(STATISTICS_SEPARATOR);
            writer.newLine();
            writeStatisticsToFile(writer, database);
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

    private List<Task> getAllTasksFromDatabase(Database database) {
        return database.getTasks().values()
                .stream()
                .flatMap(Collection::stream)
                .toList();
    }

    private void writeStatisticsToFile(BufferedWriter writer, Database database) throws IOException {
        String statistics = database.getTasksCreated() + ";" +
                database.getTasksCompleted() + ";" +
                database.getTasksFailed();
        writer.write(statistics);
    }
}