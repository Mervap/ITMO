/**
 * Solution for <a href="https://www.kgeorgiy.info/courses/java-advanced/homeworks.html#homework-implementor">Implementor</a> homework
 * of <a href="https://www.kgeorgiy.info/courses/java-advanced/">Java Advanced</a> course.
 *
 * @author Teplyakov Valery
 */

module ru.ifmo.rain.teplyakov.implementor {
    requires info.kgeorgiy.java.advanced.concurrent;
    requires info.kgeorgiy.java.advanced.mapper;
    requires info.kgeorgiy.java.advanced.student;
    requires info.kgeorgiy.java.advanced.implementor;

    requires java.compiler;
    requires java.desktop;

    exports ru.ifmo.rain.teplyakov.implementor;
    opens ru.ifmo.rain.teplyakov.implementor;
}