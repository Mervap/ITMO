package expression;

public class Multiply extends AbstractBinaryOperation {

    public Multiply(TripleExpression firstOpetator, TripleExpression secondOperator) {
        super(firstOpetator, secondOperator);
    }

    protected int apply(int x, int y) {
        return x * y;
    }
}
