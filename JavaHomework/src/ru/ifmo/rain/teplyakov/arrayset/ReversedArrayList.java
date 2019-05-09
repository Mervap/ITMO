package ru.ifmo.rain.teplyakov.arrayset;

import java.util.*;

public class ReversedArrayList<T> extends AbstractList<T> implements RandomAccess {

    private final List<T> data;
    private final boolean reversed;


    private ReversedArrayList(List<T> other, boolean reversed) {
        this.data = other;
        this.reversed = reversed;
    }

    public ReversedArrayList() {
        this(Collections.emptyList(), false);
    }

    public ReversedArrayList(TreeSet<T> other) {
        this(new ArrayList<>(other), false);
    }

    public ReversedArrayList<T> getReversed() {
        return new ReversedArrayList<>(data, !reversed);
    }

    @Override
    public T get(int index) {
        return reversed ? data.get(size() - 1 - index) : data.get(index);
    }

    @Override
    public int size() {
        return data.size();
    }


}
