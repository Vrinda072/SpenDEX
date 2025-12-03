package exceptions;

/**
 * SpendEX — InvalidAmountException
 * Custom exception for invalid amount values in SpendEX
 */
public class InvalidAmountException extends Exception {
    public InvalidAmountException(String message) {
        super(message);
    }

    public InvalidAmountException(String message, Throwable cause) {
        super(message, cause);
    }
}
