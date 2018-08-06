package expression;

import expression.exceptions.EvaluatingException;

public abstract class AbstractUnaryOperation implements TripleExpression {
    private final TripleExpression operand;

    protected AbstractUnaryOperation(final TripleExpression x) {
        operand = x;
    }

    protected abstract void check(int x) throws EvaluatingException;

    protected abstract int apply(int x) throws EvaluatingException;

    public int evaluate(int x, int y, int z) throws EvaluatingException {
        return apply(operand.evaluate(x, y, z));
    }
}