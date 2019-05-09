package info.kgeorgiy.java.advanced.concurrent;

import info.kgeorgiy.java.advanced.base.BaseTest;
import org.junit.Assert;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class BaseIPTest<P extends ScalarIP> extends BaseTest {
    public static final List<Integer> SIZES = List.of(10_000, 5, 2, 1);
    private final Random random = new Random(3257083275083275083L);
    protected List<Integer> factors = Collections.singletonList(0);

    protected final <T, U> void test(final BiFunction<List<Integer>, U, T> fExpected, final ScalarIPTest.ConcurrentFunction<P, T, U> fActual, final List<ScalarIPTest.Named<U>> cases) throws InterruptedException {
        for (final int factor : factors) {
            final P instance = createInstance(factor);
            for (final int n : SIZES) {
                System.err.println("    --- Size " + n);
                final List<Integer> data = randomList(n);
                for (final ScalarIPTest.Named<U> named : cases) {
                    final T expected = fExpected.apply(data, named.value);
                    System.err.print("        " + named.name + ", threads: ");
                    for (int threads = 1; threads <= 10; threads++) {
                        System.err.print(" " + threads);
                        Assert.assertEquals(threads + " threads", expected, fActual.apply(instance, threads, data, named.value));
                    }
                    System.err.println();
                }
                System.err.println();
            }
        }
    }

    protected final List<Integer> randomList(final int size) {
        final int[] pool = random.ints(Math.min(size, 1000_000)).toArray();
        return IntStream.generate(() -> pool[random.nextInt(pool.length)]).limit(size).boxed().collect(Collectors.toList());
    }

    protected P createInstance(final int threads) {
        return createCUT();
    }

    public interface ConcurrentFunction<P, T, U> {
        T apply(P instance, int threads, List<Integer> data, U value) throws InterruptedException;
    }

    protected static final class Named<T> {
        public final String name;
        public final T value;

        public Named(final String name, final T value) {
            this.name = name;
            this.value = value;
        }
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
