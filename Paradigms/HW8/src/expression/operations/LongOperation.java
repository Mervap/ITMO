package expression.operations;

import expression.exceptions.IllegalOperationException;
import expression.exceptions.IncorrectConstException;

public class LongOperation implements Operation <Long> {
    public Long parseNumber(String number) throws IncorrectConstException {
        try {
            return Long.parseLong(number);
        } catch (NumberFormatException NFM) {
            throw new IncorrectConstException();
        }
    }

    public static void checkZero(Long y, String reason) throws IllegalOperationException {
        if (y == 0) {
            throw new IllegalOperationException(reason);
        }
    }

    public Long add(Long x, Long y) {
        return x + y;
    }

    public Long sub(Long x, Long y) {
        return x - y;
    }

    public Long mul(Long x, Long y) {
        return x * y;
    }

    public Long div(final Long x, final Long y) throws IllegalOperationException {
        checkZero(y, "Division by zero");
        return x / y;
    }

    public Long not(final Long x) {
        return -x;
    }

    public Long count(Long x) {
        return (long) Long.bitCount(x);
    }

    public Long min(Long x, Long y) {
        return Long.min(x, y);
    }

    public Long max(Long x, Long y) {
        return Long.max(x, y);
    }

}
