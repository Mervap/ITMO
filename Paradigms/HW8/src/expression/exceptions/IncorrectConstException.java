package expression.exceptions;

public class IncorrectConstException extends ParsingException {
    public IncorrectConstException() {
        super("Bad const");
    }
}