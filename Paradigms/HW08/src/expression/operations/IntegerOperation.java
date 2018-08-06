package expression.operations;

import expression.exceptions.EvaluatingException;
import expression.exceptions.IncorrectConstException;
import expression.exceptions.OverflowException;

public class IntegerOperation implements Operation <Integer> {
    private boolean flag;

    public IntegerOperation(boolean check) {
        flag = check;
    }

    public Integer parseNumber(String number) throws IncorrectConstException {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new IncorrectConstException();
        }
    }

    private void checkAdd(Integer x, Integer y) throws OverflowException {
        if (x > 0 && Integer.MAX_VALUE - x < y) {
            throw new OverflowException();
        }
        if (x < 0 && Integer.MIN_VALUE - x > y) {
            throw new OverflowException();
        }
    }

    private void checkSub(Integer x, Integer y) throws OverflowException {
        if (x >= 0 && y < 0 && x - Integer.MAX_VALUE > y) {
            throw new OverflowException();
        }
        if (x <= 0 && y > 0 && Integer.MIN_VALUE - x > -y) {
            throw new OverflowException();
        }
    }

    private void checkMul(Integer x, Integer y) throws OverflowException {
        if (x > 0 && y > 0 && Integer.MAX_VALUE / x < y) {
            throw new OverflowException();
        }
        if (x > 0 && y < 0 && Integer.MIN_VALUE / x > y) {
            throw new OverflowException();
        }
        if (x < 0 && y > 0 && Integer.MIN_VALUE / y > x) {
            throw new OverflowException();
        }
        if (x < 0 && y < 0 && Integer.MAX_VALUE / x > y) {
            throw new OverflowException();
        }
    }

    private void checkDiv(Integer x, Integer y) throws OverflowException {
        if (x == Integer.MIN_VALUE && y == -1) {
            throw new OverflowException();
        }
    }

    private void checkNot(Integer x) throws OverflowException {
        if (x == Integer.MIN_VALUE) {
            throw new OverflowException();
        }
    }

    public Integer add(Integer x, Integer y) throws OverflowException {
        if (flag) {
            checkAdd(x, y);
        }
        return x + y;
    }

    public Integer sub(Integer x, Integer y) throws OverflowException {
        if (flag) {
            checkSub(x, y);
        }
        return x - y;
    }

    public Integer mul(Integer x, Integer y) throws OverflowException {
        if (flag) {
            checkMul(x, y);
        }
        return x * y;
    }

    public Integer div(Integer x, Integer y) throws EvaluatingException {
        LongOperation.checkZero(Long.valueOf(y), "Division by zero");
        if (flag) {
            checkDiv(x, y);
        }
        return x / y;
    }

    public Integer not(Integer x) throws OverflowException {
        if (flag) {
            checkNot(x);
        }
        return -x;
    }

    public Integer count(Integer x) {
        return Integer.bitCount(x);
    }

    public Integer min(Integer x, Integer y) {
        return Integer.min(x, y);
    }

    public Integer max(Integer x, Integer y) {
        return Integer.max(x, y);
    }
}
