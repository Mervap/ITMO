package expression;

import expression.exceptions.EvaluatingException;
import expression.operations.Operation;

public class Divide<T> extends AbstractBinaryOperation <T> {

    public Divide(TripleExpression <T> firstOpetator, TripleExpression <T> secondOperator, Operation <T> op) {
        super(firstOpetator, secondOperator, op);
    }

    protected T apply(T x, T y) throws EvaluatingException {
        return op.div(x, y);
    }
}

