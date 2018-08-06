package expression;

import expression.exceptions.*;

import static expression.OverflowCheck.overflowMultiplyCheck;

public class CheckedPow extends AbstractBinaryOperation {

    public CheckedPow(TripleExpression x, TripleExpression y) {
        super(x, y);
    }

    protected void check(int x, int y) throws EvaluatingException {
        if (x == 0 && y == 0) {
            throw new IllegalOperationException("0 ** 0 is not determinated");
        }
        if (y < 0) {
            throw new IllegalOperationException("Powering in negative");
        }
    }

    protected int binaryPow(int x, int y) throws OverflowException {

        if (y == 0) {
            return 1;
        }

        if (y % 2 == 0) {
            int res = binaryPow(x, y / 2);
            overflowMultiplyCheck(res, res);
            return res * res;
        } else {
            int res = binaryPow(x, y - 1);
            overflowMultiplyCheck(res, x);
            return res * x;
        }
    }

    protected int apply(int x, int y) throws EvaluatingException {
        check(x, y);
        return binaryPow(x, y);
    }

}
