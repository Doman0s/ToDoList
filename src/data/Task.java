package data;

import exception.TaskNameCantBeEmptyOrNull;

import java.io.Serializable;
import java.time.LocalDate;

public class Task implements Comparable<Task>, Serializable {
    private String name;
    private String description;
    private final LocalDate creationDate;
    private LocalDate deadline;
    private Status status;
    private Priority priority;

    public Task(String name, String description, LocalDate creationDate,
                LocalDate deadline, Status status, Priority priority) {
        setName(name);
        this.description = description;
        this.creationDate = creationDate;
        this.deadline = deadline;
        this.status = status;
        this.priority = priority;
    }

    public Task(String name, String description, LocalDate deadline, Priority priority) {
        this(name, description, LocalDate.now(), deadline, Status.TO_DO, priority);
    }

    public Task(Task original) {
        this.name = original.name;
        this.description = original.description;
        this.creationDate = original.creationDate;
        this.deadline = original.deadline;
        this.status = original.status;
        this.priority = original.priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty())
            throw new TaskNameCantBeEmptyOrNull("Task name cannot be empty.");
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String toCsv() {
        return name + ";" +
                description + ";" +
                creationDate + ";" +
                deadline + ";" +
                status.name() + ";" +
                priority.name() + ";";
    }

    public String getFullInfo() {
        return "Name: " + name + "\n" +
                "Description: " + description + "\n" +
                "Deadline: " + deadline + "\n" +
                "Priority: " + priority.name();
    }

    @Override
    public int compareTo(Task task) {
        return -priority.compareTo(task.getPriority());
    }

    @Override
    public String toString() {
        // Shorten name if it's longer than 20 characters
        String shortenedName = name.length() > 20 ? name.substring(0, 20) + "..." : name;

        // Shorten description if it's longer than 30 characters
        String shortenedDescription = description.length() > 30 ? description.substring(0, 30) + "..." : description;

        return String.format("%-23s | %-33s | %-10s | %-10s",
                shortenedName, shortenedDescription, deadline, priority.name());
    }
}
