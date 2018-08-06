package expression;

import expression.exceptions.*;

public class CheckedSubtract extends AbstractBinaryOperation {

    public CheckedSubtract(TripleExpression firstOpetator, TripleExpression secondOperator) {
        super(firstOpetator, secondOperator);
    }

    protected void check(int x, int y) throws OverflowException {
        if (x >= 0 && y < 0 && x - Integer.MAX_VALUE > y) {
            throw new OverflowException();
        }
        if (x <= 0 && y > 0 && Integer.MIN_VALUE - x > -y) {
            throw new OverflowException();
        }
    }

    protected int apply(int x, int y) throws OverflowException {
        check(x, y);
        return x - y;
    }
}
