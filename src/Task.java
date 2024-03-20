import java.time.LocalDate;
import java.util.UUID;

class Task {
    private final UUID id;
    private String name;
    private String description;
    private final LocalDate creationDate;
    private LocalDate deadline;
    private Status status;
    private Priority priority;

    public Task(String name, String description, LocalDate deadline, Priority priority) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.creationDate = LocalDate.now();
        this.deadline = deadline;
        this.status = Status.TO_DO;
        this.priority = priority;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
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
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", creationDate=" + creationDate +
                ", deadline=" + deadline +
                ", status=" + status +
                ", priority=" + priority +
                '}';
    }
}
