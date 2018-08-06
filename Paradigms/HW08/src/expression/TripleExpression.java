package expression;

import expression.exceptions.EvaluatingException;

public interface TripleExpression<T> {
    T evaluate(T x, T y, T z) throws EvaluatingException;
}