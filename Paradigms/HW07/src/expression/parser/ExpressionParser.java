package expression.parser;

import expression.*;
import expression.exceptions.*;

public class ExpressionParser implements Parser {
    private Tokenizer tokenizer;

    private TripleExpression unary() throws ParsingException {
        TripleExpression res;

        switch (tokenizer.getNextToken()) {
            case CONST:
                res = new Const(tokenizer.getValue());
                tokenizer.getNextToken();
                break;
            case VAR:
                res = new Variable(tokenizer.getName());
                tokenizer.getNextToken();
                break;
            case NOT:
                res = new CheckedNegate(unary());
                break;
            case LOG10:
                res = new CheckedLog(unary(), new Const(10));
                break;
            case POW10:
                res = new CheckedPow(new Const(10), unary());
                break;
            case OPEN_BRACE:
                int pos = tokenizer.getInd();
                res = addAndSub();
                if (tokenizer.getCurrentToken() != Token.CLOSE_BRACE) {
                    throw new MissingClosingParenthesisException(tokenizer.getExpression(), pos - 1);
                }
                tokenizer.getNextToken();
                break;
            default:
                throw new ParsingException("Incorrect expression" + "\n" + tokenizer.getExpression());
        }
        return res;
    }

    private TripleExpression logAndPow() throws ParsingException {
        TripleExpression res = unary();
        while (true) {
            switch (tokenizer.getCurrentToken()) {
                case LOG:
                    res = new CheckedLog(res, unary());
                    break;
                case POW:
                    res = new CheckedPow(res, unary());
                    break;
                default:
                    return res;
            }
        }
    }

    private TripleExpression mulAndDiv() throws ParsingException {
        TripleExpression res = logAndPow();
        while (true) {
            switch (tokenizer.getCurrentToken()) {
                case MUL:
                    res = new CheckedMultiply(res, logAndPow());
                    break;
                case DIV:
                    res = new CheckedDivide(res, logAndPow());
                    break;
                default:
                    return res;
            }
        }
    }

    private TripleExpression addAndSub() throws ParsingException {
        TripleExpression res = mulAndDiv();
        while (true) {
            switch (tokenizer.getCurrentToken()) {
                case ADD:
                    res = new CheckedAdd(res, mulAndDiv());
                    break;
                case SUB:
                    res = new CheckedSubtract(res, mulAndDiv());
                    break;
                default:
                    return res;
            }
        }
    }


    public TripleExpression parse(final String expression) throws ParsingException {
        assert expression != null : "Expression is null";
        tokenizer = new Tokenizer(expression);
        return addAndSub();
    }
}
