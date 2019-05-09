package info.kgeorgiy.java.advanced.student;

import info.kgeorgiy.java.advanced.base.BaseTest;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Tests for easy version
 * of <a href="https://www.kgeorgiy.info/courses/java-advanced/homeworks.html#homework-student">Student</a> homework
 * for <a href="https://www.kgeorgiy.info/courses/java-advanced/">Java Advanced</a> course.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StudentQueryTest extends BaseTest {
    private static final Random RANDOM = new Random(5252958235234523412L);
    private static final List<String> FIRST_NAMES = Arrays.asList("Александр", "Анвер");
    private static final List<String> LAST_NAMES = Arrays.asList("Абрамов", "Амиров");
    private static final List<String> GROUPS = Arrays.asList("M3138", "M3139");

    private static final List<Student> STUDENTS = IntStream.range(0, 4)
            .mapToObj(id -> new Student(id, random(FIRST_NAMES), random(LAST_NAMES), random(GROUPS)))
            .collect(Collectors.toList());

    private static final List<List<Student>> INPUTS = IntStream.range(1, STUDENTS.size())
            .mapToObj(size -> {
                final List<Student> students = new ArrayList<>(STUDENTS);
                Collections.shuffle(students, RANDOM);
                return List.copyOf(students.subList(0, size));
            })
            .collect(Collectors.toList());


    private static <T> T random(final List<T> values) {
        return values.get(RANDOM.nextInt(values.size()));
    }

    private final StudentQuery db = createCUT();

    @Test
    public void test01_testGetFirstNames() {
        testGet(
                db::getFirstNames,
                "Анвер", "Анвер,Александр", "Александр,Александр,Анвер"
        );
    }

    @Test
    public void test02_testGetLastNames() {
        testGet(
                db::getLastNames,
                "Амиров", "Амиров,Абрамов", "Абрамов,Абрамов,Амиров"
        );
    }

    @Test
    public void test03_testGetGroups() {
        testGet(
                db::getGroups,
                "M3138", "M3138,M3138", "M3139,M3139,M3138"
        );
    }

    @Test
    public void test04_testGetFullNames() {
        testGet(
                db::getFullNames,
                "Анвер Амиров", "Анвер Амиров,Александр Абрамов", "Александр Абрамов,Александр Абрамов,Анвер Амиров"
        );
    }

    @Test
    public void test05_testGetDistinctFirstNames() {
        testGet(
                db::getDistinctFirstNames,
                "Анвер", "Александр,Анвер", "Александр,Анвер"
        );
    }

    @Test
    public void test06_testGetMinStudentFirstName() {
        testString(
                db::getMinStudentFirstName,
                "Анвер", "Анвер", "Александр"
        );
    }

    @Test
    public void test07_testSortStudentsById() {
        testList(
                db::sortStudentsById,
                new int[]{0}, new int[]{0, 1}, new int[]{1, 2, 0}
        );
    }

    @Test
    public void test08_testSortStudentsByName() {
        testList(
                db::sortStudentsByName,
                new int[]{0}, new int[]{1, 0}, new int[]{1, 0, 2}
        );
    }

    @Test
    public void test09_testFindStudentsByFirstName() {
        testFind(
                db::findStudentsByFirstName,
                FIRST_NAMES,
                new int[]{0}, new int[]{1}, new int[]{2}
        );
    }

    @Test
    public void test10_testFindStudentsByLastName() {
        testFind(
                db::findStudentsByLastName,
                LAST_NAMES,
                new int[]{0}, new int[]{1}, new int[]{2}
        );
    }

    @Test
    public void test11_testFindStudentsByGroup() {
        testFind(
                db::findStudentsByGroup,
                GROUPS,
                new int[]{}, new int[]{1, 0}, new int[]{1, 0}
        );
    }

    @Test
    public void test12_findStudentNamesByGroup() {
        testString(
                students -> find(db::findStudentNamesByGroupList, students, GROUPS).toString(),
                "[]", "[Абрамов=Александр, Амиров=Анвер]", "[Абрамов=Александр]"
        );
    }

    private static void testGet(final Function<List<Student>, Collection<String>> query, final String... answers) {
        testString(query.andThen(vs -> String.join(",", vs)), answers);
    }

    protected static void testString(final Function<List<Student>, String> query, final String... answers) {
        test(query, (students, answer) -> answer, answers);
    }

    private static void testFind(final BiFunction<List<Student>, String, List<Student>> query, final List<String> values, final int[]... answers) {
        testList(students -> find(query, students, values), answers);
    }

    private static <T> T find(final BiFunction<List<Student>, String, T> query, final List<Student> students, final List<String> values) {
        return query.apply(students, values.get(students.size() % values.size()));
    }

    private static void testList(final Function<List<Student>, List<Student>> query, final int[]... answers) {
        test(query, Util::getStudents, answers);
    }

    @SafeVarargs @SuppressWarnings("varargs")
    public static <T, A> void test(final Function<List<Student>, T> query, final BiFunction<List<Student>, A, T> answer, final A... answers) {
        Util.test(INPUTS, query, answer, answers);
    }
}
