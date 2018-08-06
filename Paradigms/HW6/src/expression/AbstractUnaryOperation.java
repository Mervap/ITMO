package expression;

public abstract class AbstractUnaryOperation implements TripleExpression {
    private final TripleExpression firstOperator;

    protected AbstractUnaryOperation(TripleExpression firstOperator) {
        assert firstOperator != null : "Some unary operator is null";
        this.firstOperator = firstOperator;
    }

    protected abstract int apply(int x);

    public int evaluate(int x, int y, int z) {
        return apply(firstOperator.evaluate(x, y, z));
    }
}