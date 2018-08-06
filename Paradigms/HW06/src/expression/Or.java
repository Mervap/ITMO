package expression;

public class Or extends AbstractBinaryOperation {

    public Or(TripleExpression firstOpetator, TripleExpression secondOperator) {
        super(firstOpetator, secondOperator);
    }

    protected int apply(int x, int y) {
        return x | y;
    }
}

