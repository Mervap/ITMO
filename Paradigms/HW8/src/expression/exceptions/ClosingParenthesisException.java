package expression.exceptions;

public class ClosingParenthesisException extends ParsingException {
    public ClosingParenthesisException(final String s, int ind) {
        super("Excess closing parenthesis at position: " + ind + "\n" + s + "\n" + getPlace(ind, 1));
    }
}
