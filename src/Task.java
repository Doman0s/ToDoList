import exception.DeadlineDateMustBeFuture;
import exception.TaskNameCantBeEmptyOrNull;

import java.time.LocalDate;

class Task implements Comparable<Task> {
    private String name;
    private String description;
    private final LocalDate creationDate;
    private LocalDate deadline;
    private Status status;
    private Priority priority;

    public Task(String name, String description, LocalDate deadline, Priority priority) {
        setName(name);
        this.description = description;
        this.creationDate = LocalDate.now();
        setDeadline(deadline);
        this.status = Status.TO_DO;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty())
            throw new TaskNameCantBeEmptyOrNull("Task name cannot be empty.");
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

    @Override
    public int compareTo(Task task) {
        return -priority.compareTo(task.getPriority());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Name: ").append(name).append("\n")
                .append("Description: ").append(description).append("\n")
                .append("Deadline: ").append(deadline).append("\n")
                .append("Status: ").append(status.name()).append("\n")
                .append("Priority: ").append(priority.name());

        return builder.toString();
    }
}
