package info.kgeorgiy.java.advanced.student;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;
import java.util.function.Function;

/**
 * Tests for hard version
 * of <a href="https://www.kgeorgiy.info/courses/java-advanced/homeworks.html#homework-student">Student</a> homework
 * for <a href="https://www.kgeorgiy.info/courses/java-advanced/">Java Advanced</a> course.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StudentGroupQueryTest extends StudentQueryTest {
    private final StudentGroupQuery db = createCUT();

    @Test
    public void test21_testGetGroupsByName() {
        testGroups(
                db::getGroupsByName,
                new int[]{0}, new int[]{1, 0}, new int[]{2, 1, 0}
        );
    }

    @Test
    public void test22_testGetGroupsById() {
        testGroups(
                db::getGroupsById,
                new int[]{0}, new int[]{0, 1}, new int[]{2, 1, 0}
        );
    }

    @Test
    public void test23_testGetLargestGroup() {
        testString(
                db::getLargestGroup,
                "M3138", "M3138", "M3139"
        );
    }


    @Test
    public void test24_testGetLargestGroupByFirstName() {
        testString(
                db::getLargestGroupFirstName,
                "M3138", "M3138", "M3138"
        );
    }

    private static void testGroups(final Function<List<Student>, List<Group>> query, final int[]... answers) {
        test(query, Util::groups, answers);
    }
}
