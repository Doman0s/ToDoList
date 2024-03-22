package exception;

public class InvalidFileManagerTypeException extends RuntimeException {
    public InvalidFileManagerTypeException(String message) {
        super(message);
    }
}
