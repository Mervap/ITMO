package cljtest;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import jstest.Engine;

import java.util.Optional;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ClojureEngine implements Engine {
    public static final IFn HASH_MAP = Clojure.var("clojure.core", "hash-map");
    public static final ClojureScript.F<Object> EVAL = ClojureScript.function("eval", Object.class);
    private final ClojureScript.F<String> TO_STRING = ClojureScript.function("toString", String.class);
    private final ClojureScript.F<Object> READ_STRING = ClojureScript.function("read-string", Object.class);

    private final Optional<IFn> evaluate;
    private final String evaluateString;
    private Result<Object> parsed;
    private String expression;

    public ClojureEngine(final String script, final Optional<String> evaluate) {
        ClojureScript.loadScript(script);

        this.evaluate = evaluate.map(n -> Clojure.var("clojure.core", n));
        evaluateString = evaluate.map(s -> s + " ").orElse("");
    }

    @Override
    public void parse(final String expression) {
        parsed = EVAL.call(READ_STRING.call(new Result<>("\"" + expression + "\"", expression)));
        this.expression = expression;
    }

    @Override
    public Result<Number> evaluate(final double[] vars) {
        final Object map = HASH_MAP.invoke("x", vars[0], "y", vars[1], "z", vars[2]);
        final String context = String.format("(%sexpr %s)\nwhere expr = %s", evaluateString, map, expression);
        return evaluate
                .map(f -> ClojureScript.call(f, new Object[]{parsed.value, map}, Number.class, context))
                .orElseGet(() -> ClojureScript.call((IFn) parsed.value, new Object[]{map}, Number.class, context));
    }

    public Result<String> parsedToString() {
        return TO_STRING.call(parsed);
    }
}
