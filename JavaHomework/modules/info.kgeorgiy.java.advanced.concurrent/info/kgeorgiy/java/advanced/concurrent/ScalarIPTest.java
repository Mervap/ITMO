package info.kgeorgiy.java.advanced.concurrent;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * Full tests for easy version
 * of <a href="https://www.kgeorgiy.info/courses/java-advanced/homeworks.html#homework-implementor">Implementor</a> homework
 * for <a href="https://www.kgeorgiy.info/courses/java-advanced/">Java Advanced</a> course.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ScalarIPTest<P extends ScalarIP> extends BaseIPTest<P> {
    public static final int PROCESSORS = Runtime.getRuntime().availableProcessors();

    public static final Comparator<Integer> BURN_COMPARATOR = (o1, o2) -> {
        int total = o1 + o2;
        for (int i = 0; i < 5_000_000; i++) {
            total += i;
        }
        if (total == o1 + o2) {
            throw new AssertionError();
        }
        return Integer.compare(o1, o2);
    };

    public static final Comparator<Integer> SLEEP_COMPARATOR = (o1, o2) -> {
        try {
            Thread.sleep(10);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
        return Integer.compare(o1, o2);
    };

    @Test
    public void test01_maximum() throws InterruptedException {
        test(Collections::max, ScalarIP::maximum, comparators);
    }

    @Test
    public void test02_minimum() throws InterruptedException {
        test(Collections::min, ScalarIP::minimum, comparators);
    }

    @Test
    public void test03_all() throws InterruptedException {
        test((data, predicate) -> data.stream().allMatch(predicate), ScalarIP::all, predicates);
    }

    @Test
    public void test04_any() throws InterruptedException {
        test((data, predicate) -> data.stream().anyMatch(predicate), ScalarIP::any, predicates);
    }

    @Test
    public void test05_sleepPerformance() throws InterruptedException {
        final List<Integer> data = randomList(100 * PROCESSORS);
        final double speedup = speedup(data, SLEEP_COMPARATOR, PROCESSORS * 2);
        Assert.assertTrue("Not parallel", speedup > 0.66);
        Assert.assertTrue("Too parallel", speedup < 1.5);
    }

    @Test
    public void test06_burnPerformance() throws InterruptedException {
        final List<Integer> data = randomList(100 * PROCESSORS);
        final double speedup = speedup(data, BURN_COMPARATOR, PROCESSORS);
        Assert.assertTrue("Not parallel", speedup > 0.66);
        Assert.assertTrue("Too parallel", speedup < 1.5);
    }

    protected double speedup(final List<Integer> data, final Comparator<Integer> comparator, final int threads) throws InterruptedException {
        System.err.println("    Warm up");
        final ConcurrentFunction<P, Integer, Comparator<Integer>> maximum = ScalarIP::maximum;
        for (int i = 0; i < 5; i++) {
            performance(threads, threads, data, maximum, comparator);
        }
        System.err.println("    Measurement");

        final double performance1 = performance(1, threads, data, maximum, comparator);
        final double performance2 = performance(threads, threads, data, maximum, comparator);
        final double speedup = performance2 / performance1;
        System.err.format("    Performance ratio %.1f for %d threads (%.1f %.1f ms/op)%n", speedup, threads, performance1, performance2);
        return speedup;
    }

    protected int getSubtasks(final int threads, final int totalThreads) {
        return threads;
    }

    private double performance(final int threads, final int totalThreads, final List<Integer> data, final ConcurrentFunction<P, Integer, Comparator<Integer>> f, final Comparator<Integer> comparator) throws InterruptedException {
        final int subtasks = getSubtasks(threads, totalThreads);
        final long start = System.nanoTime();
        f.apply(createInstance(threads), subtasks, data, comparator);
        final long time = System.nanoTime() - start;

        final double ops = (subtasks - 1) + (Math.ceil(data.size() / (double) threads) - 1);
        return time / 1e6 / ops;
    }

    protected final List<Named<Comparator<Integer>>> comparators = List.of(
            new Named<>("Natural order", Integer::compare),
            new Named<>("Reverse order", (l1, l2) -> Integer.compare(l2, l1)),
            new Named<>("Div 100", Comparator.comparingInt(v -> v / 100)),
            new Named<>("Even first", Comparator.<Integer>comparingInt(v -> v % 2).thenComparing(v -> v)),
            new Named<>("All equal", (v1, v2) -> 0)
    );

    protected final List<Named<Predicate<Integer>>> predicates = List.of(
            new Named<>("Equal 0", Predicate.isEqual(0)),
            new Named<>("Greater than 0", i -> i > 0),
            new Named<>("Even", i -> i % 2 == 0),
            new Named<>("True", i -> true),
            new Named<>("False", i -> false)
    );
}
