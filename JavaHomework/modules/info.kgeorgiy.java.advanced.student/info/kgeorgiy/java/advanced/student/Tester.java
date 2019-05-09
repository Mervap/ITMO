package info.kgeorgiy.java.advanced.student;

import info.kgeorgiy.java.advanced.base.BaseTester;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class Tester extends BaseTester {
    public static void main(final String... args) {
        new Tester()
                .add("StudentQuery", FullStudentQueryTest.class)
                .add("StudentGroupQuery", FullStudentGroupQueryTest.class)
                .add("AdvancedStudentGroupQuery", AdvancedStudentGroupQueryTest.class)
                .run(args);
    }
}
