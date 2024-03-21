package exception;

public class TaskNameCantBeEmptyOrNull extends RuntimeException {
    public TaskNameCantBeEmptyOrNull(String message) {
        super(message);
    }
}
