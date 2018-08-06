package jstest.object;

import jstest.ArithmeticTests;
import jstest.Language;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ObjectSquareTest extends ObjectExpressionTest {
    public static final Dialect SQUARE_SQRT_OBJECT = ObjectExpressionTest.ARITHMETIC_OBJECT.copy()
            .rename("square", "Square")
            .rename("sqrt", "Sqrt");

    public static class SquareSqrtTests extends ArithmeticTests {{
        unary("square", a -> a * a);
        unary("sqrt", a -> Math.sqrt(Math.abs(a)));
        tests(
                f("square", f("-", vx, vy)),
                f("sqrt", f("+", vx, vy)),
                f("sqrt", f("/", f("square", vz), f("+", vx, vy))),
                f("+", vx, f("sqrt", f("square", c(2)))),
                f("+", vx, f("sqrt", f("-", vy, f("/", f("square", c(3)), c(4))))),
                f("+", f("square", f("-", vx, c(3))), f("*", vz, f("*", vy, f("sqrt", c(-1)))))
        );
    }}

    protected ObjectSquareTest(final int mode, final Language language) {
        super(mode, language);
        simplifications.addAll(list(
                new int[]{9, 14, 1},
                new int[]{48, 48, 1},
                new int[]{126, 126, 124},
                new int[]{1, 1, 1},
                new int[]{1, 60, 1},
                new int[]{9, 1, 1}
        ));
    }

    public static void main(final String... args) {
        test(ObjectSquareTest.class, ObjectSquareTest::new, new SquareSqrtTests(), args, SQUARE_SQRT_OBJECT);
    }
}
