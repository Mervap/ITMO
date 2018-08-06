package expression;

import expression.exceptions.EvaluatingException;
import expression.operations.Operation;

public abstract class AbstractUnaryOperation<T> implements TripleExpression <T> {
    private final TripleExpression <T> firstOperator;
    protected Operation <T> op;

    protected AbstractUnaryOperation(TripleExpression <T> firstOperator, Operation <T> operation) {
        assert firstOperator != null : "Some unary operator is null";
        this.firstOperator = firstOperator;
        op = operation;
    }

    protected abstract T apply(T x) throws EvaluatingException;

    public T evaluate(T x, T y, T z) throws EvaluatingException {
        return apply(firstOperator.evaluate(x, y, z));
    }
}