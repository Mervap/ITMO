package cljtest.functional;

import cljtest.multi.MultiSinhCoshTests;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ClojureFunctionalSinhCoshTest extends ClojureFunctionalExpressionTest {
    protected ClojureFunctionalSinhCoshTest(final boolean testMulti) {
        super(new MultiSinhCoshTests(testMulti));
    }

    public static void main(final String... args) {
        new ClojureFunctionalSinhCoshTest(mode(args, ClojureFunctionalSinhCoshTest.class)).run();
    }
}
