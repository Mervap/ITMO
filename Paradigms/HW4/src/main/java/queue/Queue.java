package queue;

// n - count of elements in sequence
// a[i] - sequence

import java.util.function.Function;
import java.util.function.Predicate;

public interface Queue {
    // Inv: (n >= 0) && (a[i] != null forall 0 <= i < n)

    // Pre: x != null
    // Post: (n == n' + 1) && (a[i] == a'[i] forall 0 <= i < n') && (a[n'] == x)
    void enqueue(Object x);

    // Pre: n > 0
    // Post: (n == n' - 1) && (a[i] == a'[i + 1] forall 0 <= i < n) && (result == a'[0])
    Object dequeue();

    // Pre: n > 0
    // Post: (result == a'[0]) && sequence doesn't change
    Object element();

    // Pre: true
    // Post: (result == n) && sequence doesn't change
    int size();

    // Pre: true
    // Post: (result == (n == 0)) && sequence doesn't change
    boolean isEmpty();

    // Pre: true
    // Post: n == 0
    void clear();

    // Pre: pred != null
    // Post: sequence doesn't change && result = {elem[0], ... , elem[k-1]} : pred(elem[i]) == true forall 0 <= i < k &&
    // && Exist {i[0] < ... < i[k-1]} in {0,...,n-1} : elem[j] == a[i[j]] forall 0 <= j < k && k -> max
    Queue filter(Predicate<Object> pred);

    // Pre: func != null && func(a[i]) != null forall 0 <= i < n
    // Post: sequence doesn't change && R = {func(a[0]),...,func(a[n-1])}
    Queue map(Function<Object, Object> func);

}
