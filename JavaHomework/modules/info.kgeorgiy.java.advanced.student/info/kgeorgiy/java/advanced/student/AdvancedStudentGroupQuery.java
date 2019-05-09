package info.kgeorgiy.java.advanced.student;

import java.util.Collection;

/**
 * Hard-version interface
 * for <a href="https://www.kgeorgiy.info/courses/java-advanced/homeworks.html#homework-student">Student</a> homework
 * of <a href="https://www.kgeorgiy.info/courses/java-advanced/">Java Advanced</a> course.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface AdvancedStudentGroupQuery extends StudentGroupQuery {
    /**
     * Returns the name of the student such that most number of groups has student with that name.
     * If there are more than one such name, the largest one is returned.
     */
    String getMostPopularName(Collection<Student> students);
}
