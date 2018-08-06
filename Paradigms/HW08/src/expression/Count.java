package expression;

import expression.exceptions.EvaluatingException;
import expression.operations.Operation;

public class Count<T> extends AbstractUnaryOperation <T> {

    public Count(TripleExpression <T> firstOperator, Operation <T> op) {
        super(firstOperator, op);
    }

    protected T apply(T x) throws EvaluatingException {
        return op.count(x);
    }
}