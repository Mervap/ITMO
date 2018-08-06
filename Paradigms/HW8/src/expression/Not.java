package expression;

import expression.exceptions.OverflowException;
import expression.operations.Operation;

public class Not<T> extends AbstractUnaryOperation <T> {

    public Not(TripleExpression <T> firstOperator, Operation <T> op) {
        super(firstOperator, op);
    }

    protected T apply(T x) throws OverflowException {
        return op.not(x);
    }
}