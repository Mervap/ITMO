package expression.operations;

import expression.exceptions.IllegalOperationException;
import expression.exceptions.IncorrectConstException;

public class ShortOperation implements Operation <Short> {
    public Short parseNumber(String number) throws IncorrectConstException {
        try {
            return (short) Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new IncorrectConstException();
        }
    }

    public Short add(Short x, Short y) {
        return (short) (x + y);
    }

    public Short sub(Short x, Short y) {
        return (short) (x - y);
    }

    public Short mul(Short x, Short y) {
        return (short) (x * y);
    }

    public Short div(Short x, Short y) throws IllegalOperationException {
        LongOperation.checkZero(Long.valueOf(y), "Division by zero");
        return (short) (x / y);
    }

    public Short not(Short x) {
        return (short) (-x);
    }

    public Short count(Short x) {
        return (short) Integer.bitCount(Short.toUnsignedInt(x));
    }

    public Short min(Short x, Short y) {
        return (short) Integer.min(x, y);
    }

    public Short max(Short x, Short y) {
        return (short) Integer.max(x, y);
    }
}
