package jstest.object;

import jstest.ArithmeticTests;
import jstest.Language;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ObjectPowLogTest extends ObjectExpressionTest {
    public static final Dialect POW_LOG_OBJECT = ObjectExpressionTest.ARITHMETIC_OBJECT.copy()
            .rename("pow", "Power")
            .rename("log", "Log");

    public static class PowLogTests extends ArithmeticTests {{
        binary("pow", Math::pow);
        binary("log", (a, b) -> Math.log(Math.abs(b)) / Math.log(Math.abs(a)));
        tests(
                f("pow", vx, vy),
                f("log", vx, vy),
                f("pow", vx, f("-", vy, vz)),
                f(
                        "pow",
                        c(2),
                        f("+", c(1), f("*", c(2), f("-", vy, vz)))
                ),
                f(
                        "log",
                        f("+", c(2), f("*", c(4), f("-", vx, vz))),
                        f("+", c(1), f("*", c(2), f("-", vy, vz)))
                )
        );
    }}

    protected ObjectPowLogTest(final int mode, final Language language) {
        super(mode, language);
        simplifications.addAll(list(
                new int[]{25, 43, 11},
                new int[]{54, 37, 11},
                new int[]{33, 47, 52},
                new int[]{11, 54, 55},
                new int[]{106, 65, 166}
        ));
    }

    public static void main(final String... args) {
        test(ObjectPowLogTest.class, ObjectPowLogTest::new, new PowLogTests(), args, POW_LOG_OBJECT);
    }
}
