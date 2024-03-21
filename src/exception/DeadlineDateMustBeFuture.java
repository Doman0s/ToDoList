package exception;

public class DeadlineDateMustBeFuture extends RuntimeException {
    public DeadlineDateMustBeFuture(String message) {
        super(message);
    }
}
