package expression.parser;

import expression.exceptions.*;
import expression.operations.*;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Tokenizer<T> {
    private String expression;
    private int ind = 0;

    private Operation <T> op;
    private static Set <Token> operations = EnumSet.of(Token.ADD, Token.DIV, Token.MUL, Token.NOT, Token.SUB, Token.MIN, Token.COUNT, Token.MAX);
    private static Set <Token> binaryOperations = EnumSet.of(Token.ADD, Token.DIV, Token.MUL, Token.SUB, Token.MIN, Token.MAX);

    private Token curToken;

    private static Map <String, Token> map = new HashMap <>();

    static {
        map.put("min", Token.MIN);
        map.put("max", Token.MAX);
        map.put("count", Token.COUNT);
        map.put("x", Token.VAR);
        map.put("y", Token.VAR);
        map.put("z", Token.VAR);
    }

    public Tokenizer(String s, Operation <T> operation) {
        expression = s;
        ind = 0;
        curToken = Token.BEGIN;
        balance = 0;
        op = operation;
    }

    private T value;
    private String name;
    private int balance;

    public Token getNextToken() throws ParsingException {
        nextToken();
        return curToken;
    }

    public Token getCurrentToken() {
        return curToken;
    }

    public T getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public int getInd() {
        return ind;
    }

    public String getExpression() {
        return expression;
    }

    private void checkForOperand(int pos) throws MissingOperandException {
        if (operations.contains(curToken) || curToken == Token.BEGIN || curToken == Token.OPEN_BRACE) {
            throw new MissingOperandException(expression, pos);
        }
    }

    private void checkForOperation(final int pos) throws MissingOperationException {
        if (curToken == Token.CLOSE_BRACE || curToken == Token.VAR || curToken == Token.CONST) {
            throw new MissingOperationException(expression, pos);
        }
    }

    private String getNumber() {
        int l = ind;
        while (ind < expression.length() && Character.isDigit(expression.charAt(ind))) {
            ind++;
        }
        return expression.substring(l, ind--);
    }

    private String getIdentifier() throws UnknownSymbolException {
        if (!Character.isLetter(expression.charAt(ind))) {
            throw new UnknownSymbolException(expression, ind);
        }
        int l = ind;
        while (ind < expression.length() && Character.isLetterOrDigit(expression.charAt(ind))) {
            ++ind;
        }
        return expression.substring(l, ind--);
    }

    private void skipSpace() {
        while (ind < expression.length() && Character.isWhitespace(expression.charAt(ind))) {
            ++ind;
        }
    }

    private void nextToken() throws ParsingException {
        skipSpace();
        if (ind >= expression.length()) {
            checkForOperand(ind);
            curToken = Token.END;
            return;
        }

        char c = expression.charAt(ind);
        switch (expression.charAt(ind)) {
            case '+':
                checkForOperand(ind);
                if (ind + 1 >= expression.length()) {
                    throw new MissingOperandException(expression, ind + 1);
                }
                curToken = Token.ADD;
                break;
            case '-':
                if (curToken == Token.CONST || curToken == Token.VAR || curToken == Token.CLOSE_BRACE) {
                    curToken = Token.SUB;
                } else {
                    if (ind + 1 >= expression.length()) {
                        throw new MissingOperandException(expression, ind + 1);
                    }
                    if (Character.isDigit(expression.charAt(ind + 1))) {
                        ++ind;
                        String temp = getNumber();
                        try {
                            value = op.parseNumber("-" + temp);
                        } catch (NumberFormatException NFE) {
                            throw new IllegalConstantException("-" + temp, expression, ind - temp.length());
                        }
                        curToken = Token.CONST;
                    } else {
                        curToken = Token.NOT;
                    }
                }
                break;
            case '/':
                checkForOperand(ind);
                if (ind + 1 >= expression.length()) {
                    throw new MissingOperandException(expression, ind + 1);
                }
                curToken = Token.DIV;
                break;
            case '*':
                checkForOperand(ind);
                if (ind + 1 >= expression.length()) {
                    throw new MissingOperandException(expression, ind + 1);
                }
                curToken = Token.MUL;
                break;
            case '(':
                checkForOperation(ind);
                ++balance;
                curToken = Token.OPEN_BRACE;
                break;
            case ')':
                if (operations.contains(curToken) || curToken == Token.OPEN_BRACE) {
                    throw new MissingOperandException(expression, ind);
                }
                --balance;
                if (balance < 0) {
                    throw new ClosingParenthesisException(expression, ind);
                }
                curToken = Token.CLOSE_BRACE;
                break;
            default:
                if (Character.isDigit(c)) {
                    checkForOperation(ind);
                    String s = getNumber();
                    try {
                        value = op.parseNumber(s);
                    } catch (NumberFormatException NFE) {
                        throw new IllegalConstantException(s, expression, ind - s.length() + 1);
                    }
                    curToken = Token.CONST;
                } else {
                    String curInd = getIdentifier();
                    if (!map.containsKey(curInd)) {
                        throw new UnknownIdentifierException(curInd, expression, ind - curInd.length() + 1);
                    }
                    if (binaryOperations.contains(map.get(curInd))) {
                        checkForOperand(ind - curInd.length() + 1);
                    } else {
                        checkForOperation(ind - curInd.length() + 1);
                    }
                    curToken = map.get(curInd);
                    if (curToken == Token.VAR) {
                        name = curInd.substring(0, 1);
                    }
                }
        }
        ++ind;
    }
}
