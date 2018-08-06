package expression;

public strictfp class Variable implements AnyExpression {
    private String name;

    public Variable(String name) {
        assert name != null : "Name of variable is null";
        this.name = name;
    }

    public int evaluate(int x) {
        return x;
    }

    public double evaluate(double x) {
        return x;
    }

    public int evaluate(int x, int y, int z) {
        switch (name) {
            case "x":
                return x;
            case "y":
                return y;
            case "z":
                return z;
        }
        return 0;
    }
}
