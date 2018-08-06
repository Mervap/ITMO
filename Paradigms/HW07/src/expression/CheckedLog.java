package expression;

import expression.exceptions.*;

public class CheckedLog extends AbstractBinaryOperation {

    public CheckedLog(TripleExpression firstOpetator, TripleExpression secondOperator) {
        super(firstOpetator, secondOperator);
    }

    protected void check(int x, int y) throws EvaluatingException {
        if (x <= 0) {
            throw new IllegalOperationException("Log form not positive");
        }
        if (y <= 0 || y == 1) {
            throw new IllegalOperationException("Incorrect log base");
        }
    }

    protected int apply(int x, int y) throws EvaluatingException {
        check(x, y);
        int l = 0;
        int r = 31;
        while (r - l > 1) {
            int m = (l + r) / 2;
            try {
                int res = new CheckedPow(new Const(y), new Const(m)).evaluate(0, 0, 0);
                if (res <= x) {
                    l = m;
                } else {
                    r = m;
                }
            } catch (EvaluatingException e) {
                r = m;
            }
        }
        return l;
    }

}
