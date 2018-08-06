package expression;

public class Count extends AbstractUnaryOperation {

    public Count(TripleExpression firstOperator) {
        super(firstOperator);
    }

    protected int apply(int x) {
        return Integer.bitCount(x);
    }
}