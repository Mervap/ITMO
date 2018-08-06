package expression.exceptions;

public class IllegalOperationException extends EvaluatingException {
    public IllegalOperationException(final String message) {
        super("Illegar operation: " + message);
    }
}