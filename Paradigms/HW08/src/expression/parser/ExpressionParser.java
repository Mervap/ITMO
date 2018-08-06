package expression.parser;

import expression.*;
import expression.exceptions.*;
import expression.operations.Operation;

public class ExpressionParser<T> implements Parser <T> {
    private Tokenizer <T> tokenizer;
    private Operation <T> op;

    public ExpressionParser(final Operation <T> op) {
        this.op = op;
    }

    private TripleExpression <T> unary() throws ParsingException {
        TripleExpression <T> res;

        switch (tokenizer.getNextToken()) {
            case CONST:
                res = new Const <T>(tokenizer.getValue());
                tokenizer.getNextToken();
                break;
            case VAR:
                res = new Variable <T>(tokenizer.getName());
                tokenizer.getNextToken();
                break;
            case NOT:
                res = new Not <T>(unary(), op);
                break;
            case COUNT:
                res = new Count <T>(unary(), op);
                break;
            case OPEN_BRACE:
                int pos = tokenizer.getInd();
                res = minAndMax();
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

    private TripleExpression <T> mulAndDiv() throws ParsingException {
        TripleExpression <T> res = unary();
        while (true) {
            switch (tokenizer.getCurrentToken()) {
                case MUL:
                    res = new Multiply <T>(res, unary(), op);
                    break;
                case DIV:
                    res = new Divide <T>(res, unary(), op);
                    break;
                default:
                    return res;
            }
        }
    }

    private TripleExpression <T> addAndSub() throws ParsingException {
        TripleExpression <T> res = mulAndDiv();
        while (true) {
            switch (tokenizer.getCurrentToken()) {
                case ADD:
                    res = new Add <T>(res, mulAndDiv(), op);
                    break;
                case SUB:
                    res = new Subtract <T>(res, mulAndDiv(), op);
                    break;
                default:
                    return res;
            }
        }
    }

    private TripleExpression <T> minAndMax() throws ParsingException {
        TripleExpression <T> res = addAndSub();
        while (true) {
            switch (tokenizer.getCurrentToken()) {
                case MIN:
                    res = new Min <T>(res, addAndSub(), op);
                    break;
                case MAX:
                    res = new Max <T>(res, addAndSub(), op);
                    break;
                default:
                    return res;
            }
        }
    }


    public TripleExpression <T> parse(final String expression) throws ParsingException {
        assert expression != null : "Expression is null";
        tokenizer = new Tokenizer <T>(expression, op);
        return minAndMax();
    }
}
