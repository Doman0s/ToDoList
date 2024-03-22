package data;

import exception.DeadlineDateMustBeFuture;
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
        setDeadline(deadline);
        this.status = status;
        this.priority = priority;
    }

    public Task(String name, String description, LocalDate deadline, Priority priority) {
        this(name, description, LocalDate.now(), deadline, Status.TO_DO, priority);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty())
            throw new TaskNameCantBeEmptyOrNull("data.Task name cannot be empty.");
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        if (deadline.isBefore(LocalDate.now()))
            throw new DeadlineDateMustBeFuture("Deadline date must be in the future.");
        this.deadline = deadline;
    }

    public Status getStatus() {
        return status;
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

    @Override
    public int compareTo(Task task) {
        return -priority.compareTo(task.getPriority());
    }

    @Override
    public String toString() {
        return "Name: " + name + " | " +
                "Description: " + description + " | " +
                "Deadline: " + deadline + " | " +
                "Status: " + status.name() + " | " +
                "Priority: " + priority.name();
    }
}
