package expression;

import expression.exceptions.EvaluatingException;
import expression.operations.Operation;

public abstract class AbstractBinaryOperation<T> implements TripleExpression <T> {
    private final TripleExpression <T> firstOperator;
    private final TripleExpression <T> secondOperator;
    protected Operation <T> op;

    public AbstractBinaryOperation(TripleExpression <T> firstOperator, TripleExpression <T> secondOperator, Operation <T> operation) {
        assert firstOperator != null && secondOperator != null : "Some binary operator is null";
        this.firstOperator = firstOperator;
        this.secondOperator = secondOperator;
        op = operation;
    }

    protected abstract T apply(T x, T y) throws EvaluatingException;

    public T evaluate(T x, T y, T z) throws EvaluatingException {
        return apply(firstOperator.evaluate(x, y, z), secondOperator.evaluate(x, y, z));
    }
}
