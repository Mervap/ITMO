package expression;

import expression.exceptions.OverflowException;
import expression.operations.Operation;

public class Multiply<T> extends AbstractBinaryOperation <T> {

    public Multiply(TripleExpression <T> firstOpetator, TripleExpression <T> secondOperator, Operation <T> op) {
        super(firstOpetator, secondOperator, op);
    }

    protected T apply(T x, T y) throws OverflowException {
        return op.mul(x, y);
    }
}
