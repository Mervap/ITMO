package jstest.functional;

import jstest.ArithmeticTests;
import jstest.Language;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class FunctionalCubeTest extends FunctionalExpressionTest {
    public static class CubeTests extends ArithmeticTests {{
        unary("cube", FunctionalCubeTest::cube);
        unary("cuberoot", FunctionalCubeTest::cubeRoot);
        tests(
                f("+", f("cube", vx), vy),
                f("cuberoot", f("+", vx, vy))
        );
    }}

    protected FunctionalCubeTest(final Language language, final boolean testParsing) {
        super(language, testParsing);
    }

    private static double cube(final Double x) {
        return x * x * x;
    }

    private static double cubeRoot(final Double x) {
        return Math.pow(x, 1.0 / 3);
    }

    public static void main(final String... args) {
        test(FunctionalCubeTest.class, FunctionalCubeTest::new, args, new CubeTests());
    }
}
