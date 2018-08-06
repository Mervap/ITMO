package expression;

import expression.exceptions.EvaluatingException;
import expression.exceptions.OverflowException;

public class CheckedNegate extends AbstractUnaryOperation {

    public CheckedNegate(TripleExpression firstOperator) {
        super(firstOperator);
    }

    protected void check(int x) throws EvaluatingException {
        if (x == Integer.MIN_VALUE) {
            throw new OverflowException();
        }
    }

    protected int apply(int x) throws EvaluatingException {
        check(x);
        return -x;
    }
}
