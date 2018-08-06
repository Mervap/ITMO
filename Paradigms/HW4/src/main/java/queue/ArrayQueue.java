package queue;

public class ArrayQueue extends AbstractQueue {

    private int begin = 0;
    private int end = 0;
    private Object[] elements;

    public ArrayQueue() {
        elements = new Object[5];
    }

    public ArrayQueue(int sz) {
        elements = new Object[sz];
    }

    private int add(int x) {
        return (x + 1) % elements.length;
    }

    private void ensureCapacity(int capacity) {
        if (capacity >= elements.length || capacity * 4 < elements.length) {
            Object[] newElements = new Object[2 * capacity + 1];

            if(end >= begin){
                System.arraycopy(elements, begin, newElements, 0, end - begin);
                end = end - begin;
            } else {
                System.arraycopy(elements, begin, newElements, 0, elements.length - begin);
                System.arraycopy(elements, 0, newElements, elements.length - begin, end);
                end = elements.length - begin + end;
            }

            elements = newElements;
            begin = 0;
        }
    }

    protected void enqueueUpdate(Object x) {
        ensureCapacity(size + 1);
        elements[end] = x;
        end = add(end);
    }

    protected void dequeueUpdate() {
        elements[begin] = null;
        begin = add(begin);
        ensureCapacity(size);
    }

    protected Object elementUpdate() {
        return elements[begin];
    }

    protected void clearUpdate() {
        elements = new Object[5];
        begin = end = 0;
    }

    protected ArrayQueue createCopy() {
        ArrayQueue result = new ArrayQueue(elements.length);
        result.begin = begin;
        result.end = end;
        result.size = size;
        System.arraycopy(elements, 0, result.elements, 0, elements.length);
        return result;
    }

}
