package expression.exceptions;

public class UnknownIdentifierException extends ParsingException {
    public UnknownIdentifierException(final String id, final String s, final int ind) {
        super("Unknown identifier '" + id + "' at position: " + ind + "\n" + s + "\n" + getPlace(ind, id.length()));
    }
}
