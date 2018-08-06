package expression.parser;

import expression.TripleExpression;
import expression.exceptions.ParsingException;

public interface Parser<T> {
    TripleExpression <T> parse(String expression) throws ParsingException;
}
