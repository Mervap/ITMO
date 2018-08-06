package expression.parser;

import expression.*;

public class ExpressionParser implements Parser {
    private String expression;
    private String name;
    private int ind = 0;
    private int value = 0;

    private enum Token {
        ADD, AND, BNOT, CLOSE_BRACE, CONST, COUNT, DIV, BEGIN, ERR, MUL, NOT, OPEN_BRACE, OR, SUB, VAR, XOR
    }

    private Token curToken = Token.ERR;

    private void skipSpace() {
        while (ind < expression.length() && Character.isWhitespace(expression.charAt(ind))) {
            ++ind;
        }
    }

    private void nextToken() {
        skipSpace();
        if (ind >= expression.length()) {
            curToken = Token.BEGIN;
            return;
        }

        char c = expression.charAt(ind);
        switch (expression.charAt(ind)) {
            case '+':
                curToken = Token.ADD;
                break;
            case '-':
                if (curToken == Token.CONST || curToken == Token.VAR || curToken == Token.CLOSE_BRACE) {
                    curToken = Token.SUB;
                } else {
                    curToken = Token.NOT;
                }
                break;
            case '/':
                curToken = Token.DIV;
                break;
            case '*':
                curToken = Token.MUL;
                break;
            case '(':
                curToken = Token.OPEN_BRACE;
                break;
            case ')':
                curToken = Token.CLOSE_BRACE;
                break;
            case '&':
                curToken = Token.AND;
                break;
            case '|':
                curToken = Token.OR;
                break;
            case '^':
                curToken = Token.XOR;
                break;
            case '~':
                curToken = Token.BNOT;
                break;
            default:
                if (Character.isDigit(c)) {
                    int l = ind;
                    while (ind < expression.length() && Character.isDigit(expression.charAt(ind))) {
                        ++ind;
                    }

                    value = Integer.parseUnsignedInt(expression.substring(l, ind));
                    curToken = Token.CONST;
                    --ind;
                } else if (ind + 5 <= expression.length() && expression.substring(ind, ind + 5).equals("count")) {
                    ind += 4;
                    curToken = Token.COUNT;
                } else if (Character.isLetter(expression.charAt(ind))) {
                    int l = ind;
                    while (ind < expression.length() && Character.isLetter(expression.charAt(ind))) {
                        ++ind;
                    }

                    name = expression.substring(l, ind);
                    curToken = Token.VAR;
                    --ind;
                } else {
                    curToken = Token.ERR;
                }
        }
        ++ind;
    }

    private TripleExpression unary() {
        nextToken();
        TripleExpression res;

        switch (curToken) {
            case CONST:
                res = new Const(value);
                nextToken();
                break;
            case VAR:
                res = new Variable(name);
                nextToken();
                break;
            case NOT:
                res = new Not(unary());
                break;
            case BNOT:
                res = new Bnot(unary());
                break;
            case COUNT:
                res = new Count(unary());
                break;
            case OPEN_BRACE:
                res = or();
                nextToken();
                break;
            default:
                return new Const(0);
        }
        return res;
    }

    private TripleExpression mulAndDiv() {
        TripleExpression res = unary();
        while (true) {
            switch (curToken) {
                case MUL:
                    res = new Multiply(res, unary());
                    break;
                case DIV:
                    res = new Divide(res, unary());
                    break;
                default:
                    return res;
            }
        }
    }

    private TripleExpression addAndSub() {
        TripleExpression res = mulAndDiv();
        while (true) {
            switch (curToken) {
                case ADD:
                    res = new Add(res, mulAndDiv());
                    break;
                case SUB:
                    res = new Subtract(res, mulAndDiv());
                    break;
                default:
                    return res;
            }
        }
    }

    private TripleExpression and() {
        TripleExpression res = addAndSub();
        while (true) {
            switch (curToken) {
                case AND:
                    res = new And(res, and());
                    break;
                default:
                    return res;
            }
        }
    }

    private TripleExpression xor() {
        TripleExpression res = and();
        while (true) {
            switch (curToken) {
                case XOR:
                    res = new Xor(res, and());
                    break;
                default:
                    return res;
            }
        }
    }

    private TripleExpression or() {
        TripleExpression res = xor();
        while (true) {
            switch (curToken) {
                case OR:
                    res = new Or(res, xor());
                    break;
                default:
                    return res;
            }
        }
    }

    public TripleExpression parse(final String expression) {
        assert expression != null : "Expression is null";
        ind = 0;
        this.expression = expression;
        curToken = Token.ERR;
        return or();
    }
}
