package expression.operations;

import expression.exceptions.*;

public interface Operation<T> {
    T parseNumber(String number) throws IncorrectConstException;

    T add(T x, T y) throws OverflowException;

    T sub(T x, T y) throws OverflowException;

    T mul(T x, T y) throws OverflowException;

    T div(T x, T y) throws EvaluatingException;

    T not(T x) throws OverflowException;

    T count(T x) throws EvaluatingException;

    T min(T x, T y);

    T max(T x, T y);

}