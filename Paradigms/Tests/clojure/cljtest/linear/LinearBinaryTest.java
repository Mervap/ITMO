package cljtest.linear;

import cljtest.ClojureScript;
import clojure.lang.IPersistentVector;
import expression.BaseTest;
import jstest.Engine;

import java.util.Arrays;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class LinearBinaryTest extends BaseTest {
    static {
        ClojureScript.loadScript("linear.clj");
    }

    public static final ClojureScript.F<IPersistentVector> VECTOR = vectorFunction("vector");

    public static final ClojureScript.F<IPersistentVector> V_ADD = vectorFunction("v+");
    public static final ClojureScript.F<IPersistentVector> V_SUB = vectorFunction("v-");
    public static final ClojureScript.F<IPersistentVector> V_MUL = vectorFunction("v*");

    public static final ClojureScript.F<Number> SCALAR = numberFunction("scalar");
    public static final ClojureScript.F<IPersistentVector> VECT = vectorFunction("vect");
    public static final ClojureScript.F<IPersistentVector> V_BY_S = vectorFunction("v*s");

    public static final ClojureScript.F<IPersistentVector> M_ADD = vectorFunction("m+");
    public static final ClojureScript.F<IPersistentVector> M_SUB = vectorFunction("m-");
    public static final ClojureScript.F<IPersistentVector> M_MUL = vectorFunction("m*");

    public static final ClojureScript.F<IPersistentVector> M_BY_S = vectorFunction("m*s");
    public static final ClojureScript.F<IPersistentVector> M_BY_V = vectorFunction("m*v");
    public static final ClojureScript.F<IPersistentVector> M_BY_M = vectorFunction("m*m");

    public static final ClojureScript.F<IPersistentVector> TRANSPOSE = vectorFunction("transpose");

    public static final double EPS = 1e-10;

    static ClojureScript.F<IPersistentVector> vectorFunction(final String name) {
        return ClojureScript.function(name, IPersistentVector.class);
    }

    static ClojureScript.F<Number> numberFunction(final String name) {
        return ClojureScript.function(name, Number.class);
    }

    protected static Engine.Result<IPersistentVector> vector(final Number... xs) {
        final Engine.Result[] items = Arrays.stream(xs).map(LinearBinaryTest::number).toArray(Engine.Result[]::new);
        return vector(items);
    }

    protected static Engine.Result<IPersistentVector> matrix(final Number[]... m) {
        final Engine.Result[] items = Arrays.stream(m).map(LinearBinaryTest::vector).toArray(Engine.Result[]::new);
        return vector(items);
    }

    protected static Engine.Result<Number> number(final Number x) {
        return new Engine.Result<>(x.toString(), x);
    }

    protected static Engine.Result<IPersistentVector> vector(final Engine.Result<?>... xs) {
        return VECTOR.call(xs);
    }

    protected static Number[] row(final Number... numbers) {
        return numbers;
    }

    @Override
    protected void test() {
        final Engine.Result<IPersistentVector> v123 = vector(1L, 2L, 3L);
        final Engine.Result<IPersistentVector> v456 = vector(4L, 5L, 6L);

        assertVector(V_ADD.call(v123, v456), 5L, 7L, 9L);
        assertVector(V_SUB.call(v123, v456), -3L, -3L, -3L);
        assertVector(V_MUL.call(v123, v456), 4L, 10L, 18L);
        assertVector(V_BY_S.call(v123, number(10L)), 10L, 20L, 30L);

        assertScalar(SCALAR.call(v123, v456), 32L);
        assertVector(VECT.call(v123, v456), -3L, 6L, -3L);

        final Engine.Result<IPersistentVector> m12_34_56 = matrix(row(1L, 2L), row(3L, 4L), row(5L, 6L));
        final Engine.Result<IPersistentVector> m78_90_12 = matrix(row(7L, 8L), row(9L, 0L), row(1L, 2L));

        assertMatrix(M_ADD.call(m12_34_56, m78_90_12), row(8L, 10L), row(12L, 4L), row(6L, 8L));
        assertMatrix(M_SUB.call(m12_34_56, m78_90_12), row(-6L, -6L), row(-6L, 4L), row(4L, 4L));
        assertMatrix(M_MUL.call(m12_34_56, m78_90_12), row(7L, 16L), row(27L, 0L), row(5L, 12L));
        assertMatrix(M_BY_S.call(m12_34_56, number(10)), row(10L, 20L), row(30L, 40L), row(50L, 60L));

        assertVector(M_BY_V.call(m12_34_56, vector(10L, 20L)), 50L, 110L, 170L);
        assertMatrix(TRANSPOSE.call(m12_34_56), row(1L, 3L, 5L), row(2L, 4L, 6L));

        assertMatrix(M_BY_M.call(m12_34_56, matrix(row(10L), row(20L))),
                row(50L), row(110L), row(170L));
        assertMatrix(M_BY_M.call(m12_34_56, matrix(row(10L, 100L), row(20L, 200L))),
                row(50L, 500L), row(110L, 1100L), row(170L, 1700L));
        assertMatrix(M_BY_M.call(m12_34_56, matrix(row(10L, 100L, 1L), row(20L, 200L, 2L))),
                row(50L, 500L, 5L), row(110L, 1100L, 11L), row(170L, 1700L, 17L));
    }

    private void assertScalar(final Engine.Result<Number> result, final Number value) {
        testing(result);
        assertEquals(result.context, value, result.value);
        counter.passed();
    }

    protected void testing(final Engine.Result<?> result) {
        System.out.println("Testing " + result.context);
        counter.nextTest();
    }

    protected void assertVector(final Engine.Result<IPersistentVector> result, final Number... values) {
        testing(result);
        assertVector(result.context, result.value, values);
        counter.passed();
    }

    private static void assertVector(final String context, final IPersistentVector vector, final Number... values) {
        assertEquals(context + ": length", values.length, vector.count());
        for (int i = 0; i < values.length; i++) {
            assertEquals(context + ":" + i, EPS, values[i].doubleValue(), ((Number) vector.nth(i)).doubleValue());
        }
    }

    protected void assertMatrix(final Engine.Result<IPersistentVector> result, final Number[]... rows) {
        testing(result);
        assertEquals(result.context + ": length", rows.length, result.value.count());
        for (int i = 0; i < rows.length; i++) {
            assertVector(result.context + ":" + i, (IPersistentVector) result.value.nth(i), rows[i]);
        }
        counter.passed();
    }

    public static void main(final String... args) {
        new LinearBinaryTest().run();
    }
}
