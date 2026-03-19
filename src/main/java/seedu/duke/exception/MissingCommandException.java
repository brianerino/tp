package seedu.duke.exception;

public class MissingCommandException extends IllegalArgumentException {
    public MissingCommandException(String message) {
        super(message);
    }
}
