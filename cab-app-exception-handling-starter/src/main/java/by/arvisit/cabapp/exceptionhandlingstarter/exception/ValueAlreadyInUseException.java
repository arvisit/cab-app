package by.arvisit.cabapp.exceptionhandlingstarter.exception;

public class ValueAlreadyInUseException extends RuntimeException {

    public ValueAlreadyInUseException() {
        super();
    }

    public ValueAlreadyInUseException(String message) {
        super(message);
    }

    public ValueAlreadyInUseException(String message, Throwable cause) {
        super(message, cause);
    }
}
