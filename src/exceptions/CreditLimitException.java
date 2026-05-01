package exceptions;

public class CreditLimitException extends Exception {
    public CreditLimitException(String message) {
        super(message);
    }
}
