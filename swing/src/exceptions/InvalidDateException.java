package exceptions;

/**
 * SpendEX — InvalidDateException
 * Custom exception for invalid date values in SpendEX
 */
public class InvalidDateException extends Exception {
    public InvalidDateException(String message) {
        super(message);
    }

    public InvalidDateException(String message, Throwable cause) {
        super(message, cause);
    }
}
