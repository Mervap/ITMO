package expression;

import expression.exceptions.EvaluatingException;
import expression.operations.Operation;

public class Max<T> extends AbstractBinaryOperation <T> {
    public Max(TripleExpression <T> x, TripleExpression <T> y, Operation <T> op) {
        super(x, y, op);
    }

    protected T apply(T x, T y) {
        return op.max(x, y);
    }
}