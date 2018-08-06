package queue;

// n - count of elements in sequence
// a[i] - sequence

public class ArrayQueueModule {

    // Inv: (n >= 0) && (a[i] != null forall 0 <= i < n)
    private static int begin = 0;
    private static int end = 0;
    private static int size = 0;
    private static Object[] elements = new Object[5];

    // Pre: (elements.length != 0) && (0 <= x < elements.length)
    // Post: (result == (x + 1) % elements.length) && sequence doesn't change
    private static int add(int x) {
        return (x + 1) % elements.length;
    }

    // Pre: (elements.length != 0) && (0 <= x < elements.length)
    // Post: ((result == x - 1 && x > 0) ||
    //       (result == elements.length - 1 && x == 0)) && sequence doesn't change
    private static int dec(int x) {
        if (x == 0) {
            return elements.length - 1;
        } else {
            return x - 1;
        }
    }

    // Pre: capacity >= 0
    // Post: (capacity < elements.length <= capacity * 4) && sequence doesn't change
    private static void ensureCapacity(int capacity) {
        if (capacity >= elements.length || capacity * 4 < elements.length) {
            Object[] newElements = new Object[2 * capacity + 1];

            if(end < begin){
                System.arraycopy(elements, begin, newElements, 0, elements.length - begin);
                System.arraycopy(elements, 0, newElements, elements.length - begin, end);
                end = elements.length - begin + end;
            } else {
                System.arraycopy(elements, begin, newElements, 0, end - begin);
                end = end - begin;
            }

            begin = 0;
            elements = newElements;
        }
    }

    // Pre: x != null
    // Post: (n == n' + 1) && (a[i] == a'[i] forall 0 <= i < n') && (a[n'] == x)
    public static void enqueue(Object x) {
        assert x != null : "Element is null";
        ensureCapacity(++size);
        elements[end] = x;
        end = add(end);
    }

    // Pre: x != null
    // Post: (n == n' + 1) && (a[i] == a[i - 1] forall 1 <= i < n) && (a[0] == x)
    public static void push(Object x) {
        assert x != null : "Element is null";
        ensureCapacity(++size);
        begin = dec(begin);
        elements[begin] = x;
    }

    // Pre: n > 0
    // Post: (n == n' - 1) && (a[i] == a'[i + 1] forall 0 <= i < n) && (result == a'[0])
    public static Object dequeue() {
        assert size > 0 : "Queue is empty";
        Object ans = elements[begin];
        elements[begin] = null;
        begin = add(begin);
        ensureCapacity(--size);
        return ans;
    }

    // Pre: n > 0
    // Post: (n == n' - 1) && (a[i] == a'[i] forall 0 <= i < n') && (result == a'[n])
    public static Object remove() {
        assert size > 0 : "Queue is empty";
        end = dec(end);
        Object ans = elements[end];
        elements[end] = null;
        ensureCapacity(--size);
        return ans;
    }

    // Pre: n > 0
    // Post: (result == a[0]) && sequence doesn't change
    public static Object element() {
        assert size > 0 : "Queue is empty";
        return elements[begin];
    }

    // Pre: n > 0
    // Post: (result == a[n - 1]) && sequence doesn't change
    public static Object peek() {
        assert size > 0 : "Queue is empty";
        return elements[dec(end)];
    }

    // Pre: true
    // Post: (result == n) && sequence doesn't change
    public static int size() {
        return size;
    }

    // Pre: true
    // Post: (result == (n == 0)) && sequence doesn't change
    public static boolean isEmpty() {
        return size == 0;
    }

    // Pre: true
    // Post: n == 0
    public static void clear() {
        elements = new Object[5];
        begin = end = size = 0;
    }

}
