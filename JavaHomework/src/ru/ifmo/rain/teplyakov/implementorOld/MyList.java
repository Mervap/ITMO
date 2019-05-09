package ru.ifmo.rain.teplyakov.implementor;

import java.util.List;

public interface MyList<V extends Comparable> extends List<V> {

    V getAny();

    <T> T getFromT(V t);

    <T1, T2> T1 getFromT(V e, T2 t2);
}