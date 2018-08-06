package cljtest.multi;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class MultiSinCosTests extends MultiTests {
    public MultiSinCosTests(final boolean testMulti) {
        super(testMulti);
        unary("sin", Math::sin);
        unary("cos", Math::cos);
        tests(
                f("sin", f("-", vx, vy)),
                f("cos", f("+", vx, vy)),
                f("cos", f("/", f("sin", vz), f("+", vx, vy))),
                f("+", f("cos", f("sin", f("+", vx, c(10)))), f("*", vz, f("*", vy, f("cos", c(0)))))
        );
    }
}
