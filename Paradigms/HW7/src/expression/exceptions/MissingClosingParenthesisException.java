package expression.exceptions;

public class MissingClosingParenthesisException extends ParsingException {
    public MissingClosingParenthesisException(final String s, final int ind){
        super("Missing closing parenthesis for opening one at position: " + ind + "\n" + s + "\n" + getPlace(ind, 1));
    }
}
