package expression;

public class Not extends AbstractUnaryOperation {

    public Not(TripleExpression firstOperator) {
        super(firstOperator);
    }

    protected int apply(int x) {
        return -x;
    }
}