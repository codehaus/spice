package jcontainer.exceptions;

public class JContainerException extends RuntimeException {
    private static long counter = 1;
    private final long id;

    public JContainerException(final String message) {
        super(message);
        id = counter++;
    }

    public String getMessage() {
        return "Exception #" + id + ": " + super.getMessage();
    }
}
