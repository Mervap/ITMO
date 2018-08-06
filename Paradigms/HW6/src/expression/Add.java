package expression;

public class Add extends AbstractBinaryOperation {

    public Add(TripleExpression firstOpetator, TripleExpression secondOperator) {
        super(firstOpetator, secondOperator);
    }

    protected int apply(int x, int y) {
        return x + y;
    }

}
