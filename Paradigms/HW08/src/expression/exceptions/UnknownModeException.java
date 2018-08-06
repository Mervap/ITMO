package expression.exceptions;

public class UnknownModeException extends Exception{
    public UnknownModeException(final String mode) {
        super("Unknown mode for tabulation: " + mode);
    }
}