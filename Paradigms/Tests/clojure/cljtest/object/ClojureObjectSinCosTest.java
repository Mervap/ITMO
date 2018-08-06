package cljtest.object;

import cljtest.multi.MultiSinCosTests;
import cljtest.functional.ClojureFunctionalExpressionTest;
import jstest.Language;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ClojureObjectSinCosTest extends ClojureObjectExpressionTest {
    public static final Dialect PARSED = ClojureObjectExpressionTest.PARSED.copy()
            .rename("sin", "Sin")
            .rename("cos", "Cos");

    protected ClojureObjectSinCosTest(final boolean testMulti) {
        super(new Language(PARSED, ClojureFunctionalExpressionTest.UNPARSED, new MultiSinCosTests(testMulti)));
    }

    public static void main(final String... args) {
        new ClojureObjectSinCosTest(mode(args, ClojureObjectSinCosTest.class)).run();
    }
}
