package expression;

public class And extends AbstractBinaryOperation {

    public And(TripleExpression firstOpetator, TripleExpression secondOperator) {
        super(firstOpetator, secondOperator);
    }

    protected int apply(int x, int y) {
        return x & y;
    }
}

