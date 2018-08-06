package expression;

public class Const implements TripleExpression {
    private Number value;

    public Const(Number value) {
        assert value != null : "Value of const is null";
        this.value = value;
    }

    public int evaluate(int x, int y, int z) {
        return value.intValue();
    }
}
