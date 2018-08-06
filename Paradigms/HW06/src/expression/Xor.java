package expression;

public class Xor extends AbstractBinaryOperation {

    public Xor(TripleExpression firstOpetator, TripleExpression secondOperator) {
        super(firstOpetator, secondOperator);
    }

    protected int apply(int x, int y) {
        return x ^ y;
    }
}

