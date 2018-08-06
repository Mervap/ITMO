package expression.exceptions;

public class OverflowException extends EvaluatingException {
    public OverflowException() {
        super("overflow");
    }
}