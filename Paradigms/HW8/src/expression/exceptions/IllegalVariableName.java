package expression.exceptions;

public class IllegalVariableName extends ParsingException {
    public IllegalVariableName(final String id, final String s, final int ind) {
        super("Illegal variable name '" + id + "' at position: " + ind + "\n" + s + "\n" + getPlace(ind, id.length()));
    }
}
