package expression;

public strictfp class Const implements AnyExpression {
    private Number value;

    public Const(Number value) {
        assert value != null : "Value of const is null";
        this.value = value;
    }

    public int evaluate(int x) {
        return value.intValue();
    }

    public double evaluate(double x) {
        return value.doubleValue();
    }

    public int evaluate(int x, int y, int z) {
        return value.intValue();
    }
}
