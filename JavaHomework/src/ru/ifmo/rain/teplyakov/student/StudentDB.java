package ru.ifmo.rain.teplyakov.student;

import info.kgeorgiy.java.advanced.student.AdvancedStudentGroupQuery;
import info.kgeorgiy.java.advanced.student.Group;
import info.kgeorgiy.java.advanced.student.Student;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StudentDB implements AdvancedStudentGroupQuery {

    private static final Comparator<Student> NAME_COMPARATOR = Comparator.comparing(Student::getLastName)
            .thenComparing(Student::getFirstName)
            .thenComparingInt(Student::getId);


    private <C extends Collection<String>> C mapStudentsListToCollection(List<Student> students,
                                                                         Function<Student, String> mapper,
                                                                         Supplier<C> collection) {
        return students.stream().map(mapper).collect(Collectors.toCollection(collection));
    }

    private List<String> mapStudentsListToList(List<Student> students, Function<Student, String> mapper) {
        return mapStudentsListToCollection(students, mapper, ArrayList::new);
    }

    @Override
    public List<String> getFirstNames(List<Student> list) {
        return mapStudentsListToList(list, Student::getFirstName);
    }

    @Override
    public List<String> getLastNames(List<Student> list) {
        return mapStudentsListToList(list, Student::getLastName);
    }

    @Override
    public List<String> getGroups(List<Student> list) {
        return mapStudentsListToList(list, Student::getGroup);
    }

    private String getFullName(Student student) {
        return student.getFirstName() + " " + student.getLastName();
    }

    @Override
    public List<String> getFullNames(List<Student> list) {
        return mapStudentsListToList(list, this::getFullName);
    }

    @Override
    public Set<String> getDistinctFirstNames(List<Student> list) {
        return mapStudentsListToCollection(list, Student::getFirstName, TreeSet::new);
    }

    @Override
    public String getMinStudentFirstName(List<Student> list) {
        return list.stream().min(Student::compareTo).map(Student::getFirstName).orElse("");
    }

    private Stream<Student> sortStudentsByCmpStream(Stream<Student> students, Comparator<Student> cmp) {
        return students.sorted(cmp);
    }

    private List<Student> sortStudentsByCmp(Collection<Student> students, Comparator<Student> cmp) {
        return students.stream().sorted(cmp).collect(Collectors.toList());
    }

    @Override
    public List<Student> sortStudentsById(Collection<Student> students) {
        return sortStudentsByCmp(students, Student::compareTo);
    }

    @Override
    public List<Student> sortStudentsByName(Collection<Student> students) {
        return sortStudentsByCmp(students, NAME_COMPARATOR);
    }

    private List<Student> findStudents(Collection<Student> students,
                                       String compared,
                                       Function<Student, String> mapper) {
        return sortStudentsByCmpStream(students
                        .stream()
                        .filter(student -> compared.equals(mapper.apply(student))),
                NAME_COMPARATOR)
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> findStudentsByFirstName(Collection<Student> students, String firstName) {
        return findStudents(students, firstName, Student::getFirstName);
    }

    @Override
    public List<Student> findStudentsByLastName(Collection<Student> students, String lastName) {
        return findStudents(students, lastName, Student::getLastName);
    }

    @Override
    public List<Student> findStudentsByGroup(Collection<Student> students, String group) {
        return findStudents(students, group, Student::getGroup);
    }

    @Override
    public Map<String, String> findStudentNamesByGroup(Collection<Student> students, String group) {
        return findStudentsByGroup(students, group)
                .stream()
                .collect(Collectors.toMap(Student::getLastName,
                        Student::getFirstName,
                        BinaryOperator.minBy(String::compareTo)));
    }

    private Stream<Map.Entry<String, List<Student>>> getSortedGroupsStream(Collection<Student> students) {
        return students.stream()
                .collect(Collectors.groupingBy(Student::getGroup, TreeMap::new, Collectors.toList()))
                .entrySet().stream();
    }

    private List<Group> getSortedGroups(Collection<Student> students,
                                        UnaryOperator<List<Student>> sorter) {
        return getSortedGroupsStream(students)
                .map(elem -> new Group(elem.getKey(), sorter.apply(elem.getValue())))
                .collect(Collectors.toList());
    }

    @Override
    public List<Group> getGroupsByName(Collection<Student> students) {
        return getSortedGroups(students, this::sortStudentsByName);
    }

    @Override
    public List<Group> getGroupsById(Collection<Student> students) {
        return getSortedGroups(students, this::sortStudentsById);
    }

    private String getFilteredLargestGroup(Collection<Student> students,
                                           ToIntFunction<List<Student>> filter) {
        return getSortedGroupsStream(students)
                .map(k -> Map.entry(k.getKey(), filter.applyAsInt(k.getValue())))
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey).orElse("");
    }

    @Override
    public String getLargestGroup(Collection<Student> students) {
        return getFilteredLargestGroup(students, List::size);
    }

    @Override
    public String getLargestGroupFirstName(Collection<Student> students) {
        return getFilteredLargestGroup(students, studentsList -> getDistinctFirstNames(studentsList).size());
    }

    @Override
    public String getMostPopularName(Collection<Student> collection) {
        return collection
                .stream()
                .collect(Collectors.groupingBy(this::getFullName,
                        HashMap::new,
                        Collectors.collectingAndThen(Collectors.toSet(),
                                set -> set.stream().map(Student::getGroup).distinct().count())))
                .entrySet().stream().max(Comparator.comparingLong(Map.Entry<String, Long>::getValue)
                        .thenComparing(Map.Entry::getKey))
                .map(Map.Entry::getKey).orElse("");
    }
}
