package expression;

public abstract class AbstractBinaryOperation implements TripleExpression {
    private final TripleExpression firstOperator;
    private final TripleExpression secondOperator;

    public AbstractBinaryOperation(TripleExpression firstOperator, TripleExpression secondOperator) {
        assert firstOperator != null && secondOperator != null : "Some binary operator is null";
        this.firstOperator = firstOperator;
        this.secondOperator = secondOperator;
    }

    protected abstract int apply(int x, int y);

    public int evaluate(int x, int y, int z) {
        return apply(firstOperator.evaluate(x, y, z), secondOperator.evaluate(x, y, z));
    }
}
