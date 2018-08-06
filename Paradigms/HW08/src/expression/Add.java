package expression;

import expression.exceptions.OverflowException;
import expression.operations.Operation;

public class Add<T> extends AbstractBinaryOperation <T> {

    public Add(TripleExpression <T> firstOpetator, TripleExpression <T> secondOperator, Operation <T> op) {
        super(firstOpetator, secondOperator, op);
    }

    protected T apply(T x, T y) throws OverflowException {
        return op.add(x, y);
    }

}
