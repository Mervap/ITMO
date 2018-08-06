package expression;

import expression.exceptions.*;

import static expression.OverflowCheck.overflowMultiplyCheck;

public class CheckedMultiply extends AbstractBinaryOperation {

    public CheckedMultiply(TripleExpression firstOpetator, TripleExpression secondOperator) {
        super(firstOpetator, secondOperator);
    }

    protected void check(int x, int y) throws OverflowException  {
        overflowMultiplyCheck(x, y);
    }

    protected int apply(int x, int y) throws OverflowException  {
        check(x, y);
        return x * y;
    }
}
