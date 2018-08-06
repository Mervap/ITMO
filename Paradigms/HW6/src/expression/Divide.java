package expression;

public class Divide extends AbstractBinaryOperation {

    public Divide(TripleExpression firstOpetator, TripleExpression secondOperator) {
        super(firstOpetator, secondOperator);
    }

    protected int apply(int x, int y) {
        return x / y;
    }
}

