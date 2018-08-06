package expression;

public class Subtract extends AbstractBinaryOperation {

    public Subtract(TripleExpression firstOpetator, TripleExpression secondOperator) {
        super(firstOpetator, secondOperator);
    }

    protected int apply(int x, int y) {
        return x - y;
    }
}
