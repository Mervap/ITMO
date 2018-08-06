package expression;

public strictfp class Multiply extends AbstractBinaryOperation {

    public Multiply(AnyExpression firstOpetator, AnyExpression secondOperator) {
        super(firstOpetator, secondOperator);
    }

    protected int apply(int x, int y) {
        return x * y;
    }

    protected double apply(double x, double y) {
        return x * y;
    }
}
