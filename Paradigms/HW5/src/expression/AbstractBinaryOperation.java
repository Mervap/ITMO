package expression;

public strictfp abstract class AbstractBinaryOperation implements AnyExpression {
    private final AnyExpression firstOperator;
    private final AnyExpression secondOperator;

    public AbstractBinaryOperation(AnyExpression firstOperator, AnyExpression secondOperator) {
        assert firstOperator != null && secondOperator != null : "Some operator is null";
        this.firstOperator = firstOperator;
        this.secondOperator = secondOperator;
    }

    protected abstract int apply(int x, int y);

    protected abstract double apply(double x, double y);

    public int evaluate(int x) {
        return apply(firstOperator.evaluate(x), secondOperator.evaluate(x));
    }

    public double evaluate(double x) {
        return apply(firstOperator.evaluate(x), secondOperator.evaluate(x));
    }

    public int evaluate(int x, int y, int z) {
        return apply(firstOperator.evaluate(x, y, z), secondOperator.evaluate(x, y, z));
    }
}
