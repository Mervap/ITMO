package expression;

import expression.exceptions.EvaluatingException;
import expression.operations.Operation;

public class Min<T> extends AbstractBinaryOperation <T> {
    public Min(TripleExpression <T> x, TripleExpression <T> y, Operation <T> op) {
        super(x, y, op);
    }

    protected T apply(T x, T y) throws EvaluatingException {
        return op.min(x, y);
    }
}