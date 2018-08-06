package expression.operations;

import expression.BaseTest;
import expression.exceptions.IllegalOperationException;
import expression.exceptions.IncorrectConstException;

import java.math.BigInteger;

public class BigIntegerOperation implements Operation <BigInteger> {
    public BigInteger parseNumber(String number) throws IncorrectConstException {
        try {
            return new BigInteger(number);
        } catch (NumberFormatException NFE) {
            throw new IncorrectConstException();
        }
    }

    private void checkZero(BigInteger x, String reason) throws IllegalOperationException {
        if (x.equals(BigInteger.ZERO)) {
            throw new IllegalOperationException(reason);
        }
    }

    public BigInteger add(BigInteger x, BigInteger y) {
        return x.add(y);
    }

    public BigInteger sub(BigInteger x, BigInteger y) {
        return x.subtract(y);
    }

    public BigInteger mul(BigInteger x, BigInteger y) {
        return x.multiply(y);
    }

    public BigInteger div(BigInteger x, BigInteger y) throws IllegalOperationException {
        checkZero(y, "Division by zero");
        return x.divide(y);
    }

    public BigInteger not(BigInteger x) {
        return x.negate();
    }

    public BigInteger count(BigInteger x) {
        return BigInteger.valueOf(x.bitCount());
    }

    public BigInteger min(BigInteger x, BigInteger y) {
        return x.min(y);
    }

    public BigInteger max(BigInteger x, BigInteger y) {
        return x.max(y);
    }
}
