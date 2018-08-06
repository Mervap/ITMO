package expression;

public class Bnot extends AbstractUnaryOperation {

    public Bnot(TripleExpression firstOperator) {
        super(firstOperator);
    }

    protected int apply(int x) {
        return ~x;
    }
}