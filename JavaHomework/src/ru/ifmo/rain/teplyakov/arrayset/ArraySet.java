package ru.ifmo.rain.teplyakov.arrayset;

import java.util.*;

public class ArraySet<T> extends AbstractSet<T> implements NavigableSet<T> {

    private final List<T> data;
    private final Comparator<? super T> comparator;

    private ArraySet(List<T> data, Comparator<? super T> comparator) {
        this.data = data;
        this.comparator = comparator;
    }

    public ArraySet() {
        this(new ReversedArrayList<>(), null);
    }

    public ArraySet(Collection<? extends T> other) {
        this(new ReversedArrayList<>(new TreeSet<>(other)), null);
    }

    public ArraySet(Collection<? extends T> other, Comparator<? super T> comparator) {
        TreeSet<T> tmp = new TreeSet<>(comparator);
        tmp.addAll(other);
        this.data = new ReversedArrayList<>(tmp);
        this.comparator = comparator;
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        return Collections.binarySearch(data, (T) Objects.requireNonNull(o), comparator) >= 0;
    }

    @Override
    public Iterator<T> iterator() {
        return Collections.unmodifiableList(data).iterator();
    }

    @Override
    public int size() {
        return data.size();
    }

    private boolean checkInd(int ind) {
        return 0 <= ind && ind <= data.size() - 1;
    }

    private int getInd(T t, int deltaFound, int deltaNotFound) {
        int result = Collections.binarySearch(data, Objects.requireNonNull(t), comparator);
        if (result < 0) {
            result = -result - 1 + deltaNotFound;
        } else {
            result = result + deltaFound;
        }

        return checkInd(result) ? result : -1;
    }

    private T getElement(int ind) {
        if (ind == -1) {
            return null;
        }
        return data.get(ind);
    }

    private int lowerInd(T t) {
        return getInd(t, -1, -1);
    }

    private int floorInd(T t) {
        return getInd(t, 0, -1);
    }

    private int ceilingInd(T t) {
        return getInd(t, 0, 0);
    }

    private int higherInd(T t) {
        return getInd(t, 1, 0);
    }


    @Override
    public T lower(T t) {
        return getElement(lowerInd(t));
    }

    @Override
    public T floor(T t) {
        return getElement(floorInd(t));
    }

    @Override
    public T ceiling(T t) {
        return getElement(ceilingInd(t));
    }

    @Override
    public T higher(T t) {
        return getElement(higherInd(t));
    }

    @Override
    public T pollFirst() {
        throw new UnsupportedOperationException();
    }

    @Override
    public T pollLast() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<T> descendingSet() {
        return new ArraySet<>(((ReversedArrayList<T>) data).getReversed(), Collections.reverseOrder(comparator));
    }

    @Override
    public Iterator<T> descendingIterator() {
        return descendingSet().iterator();
    }

    private NavigableSet<T> emptySet() {
        return new ArraySet<>(new ReversedArrayList<>(), comparator);
    }

    @Override
    public NavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive) {

        int l = fromInclusive ? ceilingInd(fromElement) : higherInd(fromElement);
        int r = toInclusive ? floorInd(toElement) : lowerInd(toElement);

        if (l == -1 || r == -1 || l > r) {
            return emptySet();
        }

        return new ArraySet<>(data.subList(l, r + 1), comparator);
    }

    @Override
    public NavigableSet<T> headSet(T toElement, boolean inclusive) {
        if (data.isEmpty()) {
            return emptySet();
        }

        return subSet(first(), true, toElement, inclusive);
    }

    @Override
    public NavigableSet<T> tailSet(T fromElement, boolean inclusive) {
        if (data.isEmpty()) {
            return emptySet();
        }

        return subSet(fromElement, inclusive, last(), true);
    }

    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) throws IllegalArgumentException {
        if (comparator.compare(fromElement, toElement) > 0) {
            throw new IllegalArgumentException();
        }

        return subSet(fromElement, true, toElement, false);
    }

    @Override
    public SortedSet<T> headSet(T toElement) {
        return headSet(toElement, false);
    }

    @Override
    public SortedSet<T> tailSet(T fromElement) {
        return tailSet(fromElement, true);
    }

    @Override
    public Comparator<? super T> comparator() {
        return comparator;
    }

    private void checkNonEmpty() {
        if (data.isEmpty()) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public T first() {
        checkNonEmpty();
        return data.get(0);
    }

    @Override
    public T last() {
        checkNonEmpty();
        return data.get(size() - 1);
    }
}
