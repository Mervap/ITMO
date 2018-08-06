package queue;

// n - count of elements in sequence
// a[i] - sequence

public class ArrayQueueADT {

    // Inv: (queue.n >= 0) && (queue.a[i] != null forall 0 <= i < queue.n)
    private int begin = 0;
    private int end = 0;
    private int size = 0;
    private Object[] elements = new Object[5];

    // Pre: (elements.length != 0) && (0 <= x < elements.length) && (query != null)
    // Post: (result == (x + 1) % elements.length) && sequence doesn't change
    private static int add(ArrayQueueADT query, int x) {
        assert query != null : "Queue is null";
        return (x + 1) % query.elements.length;
    }

    // Pre: (queue.elements.length != 0) && (0 <= x < queue.elements.length) && (query != null)
    // Post: ((result == x - 1 && x > 0) ||
    //       (result == queue.elements.length - 1 && x == 0)) && sequence doesn't change
    private static int dec(ArrayQueueADT query, int x) {
        assert query != null : "Queue is null";
        if (x == 0) {
            return query.elements.length - 1;
        } else {
            return x - 1;
        }
    }

    // Pre: (capacity >= 0) && (query != null)
    // Post: (capacity < queue.elements.length <= capacity * 4) && sequence doesn't change
    private static void ensureCapacity(ArrayQueueADT query, int capacity) {
        assert query != null : "Queue is null";
        if (capacity >= query.elements.length || capacity * 4 < query.elements.length) {
            Object[] newElements = new Object[2 * capacity + 1];

            if(query.end < query.begin){
                System.arraycopy(query.elements, query.begin, newElements, 0, query.elements.length - query.begin);
                System.arraycopy(query.elements, 0, newElements, query.elements.length - query.begin, query.end);
                query.end = query.elements.length - query.begin + query.end;
            } else {
                System.arraycopy(query.elements, query.begin, newElements, 0, query.end - query.begin);
                query.end = query.end - query.begin;
            }

            query.begin = 0;
            query.elements = newElements;
        }
    }

    // Pre: (x != null) && (query != null)
    // Post: (queue.n == queue.n' + 1) && (queue.a[i] == queue.a'[i] forall 0 <= i < queue.n') && (queue.a[n'] == x)
    public static void enqueue(ArrayQueueADT query, Object x) {
        assert query != null : "Queue is null";
        assert x != null : "Element is null";
        ensureCapacity(query, ++query.size);
        query.elements[query.end] = x;
        query.end = add(query, query.end);
    }

    // Pre: (x != null) && (query != null)
    // Post: (queue.n == queue.n' + 1) && (queue.a[i] == queue.a[i - 1] forall 1 <= i < queue.n) && (queue.a[0] == x)
    public static void push(ArrayQueueADT query, Object x) {
        assert query != null : "Queue is null";
        assert x != null : "Element is null";
        ensureCapacity(query, ++query.size);
        query.begin = dec(query, query.begin);
        query.elements[query.begin] = x;
    }

    // Pre: (queue.n > 0) && (query != null)
    // Post: (queue.n == queue.n' - 1) && (queue.a[i] == queue.a'[i + 1] forall 0 <= i < queue.n) && (result == queue.a'[0])
    public static Object dequeue(ArrayQueueADT query) {
        assert query != null : "Queue is null";
        assert query.size > 0 : "Queue is empty";
        Object ans = query.elements[query.begin];
        query.elements[query.begin] = null;
        query.begin = add(query, query.begin);
        ensureCapacity(query, --query.size);
        return ans;
    }

    // Pre: (queue.n > 0) && (query != null)
    // Post: (queue.n == queue.n' - 1) && (queue.a[i] == queue.a'[i] forall 0 <= i < queue.n') && (result == queue.a'[n])
    public static Object remove(ArrayQueueADT query) {
        assert query != null : "Queue is null";
        assert query.size > 0 : "Queue is empty";
        query.end = dec(query, query.end);
        Object ans = query.elements[query.end];
        query.elements[query.end] = null;
        ensureCapacity(query, --query.size);
        return ans;
    }

    // Pre: (queue.n > 0) && (query != null)
    // Post: (result == queue.a[0]) && sequence doesn't change
    public static Object element(ArrayQueueADT query) {
        assert query != null : "Queue is null";
        assert query.size > 0 : "Queue is empty";
        return query.elements[query.begin];
    }

    // Pre: (queue.n > 0) && (query != null)
    // Post: (result == queue.a[n - 1]) && sequence doesn't change
    public static Object peek(ArrayQueueADT query) {
        assert query != null : "Queue is null";
        assert query.size > 0 : "Queue is empty";
        return query.elements[dec(query, query.end)];
    }

    // Pre: query != null
    // Post: (result == queue.n) && sequence doesn't change
    public static int size(ArrayQueueADT query) {
        assert query != null : "Queue is null";
        return query.size;
    }

    // Pre: query != null
    // Post: (result == (queue.n == 0)) && sequence doesn't change
    public static boolean isEmpty(ArrayQueueADT query) {
        assert query != null : "Queue is null";
        return query.size == 0;
    }

    // Pre: query != null
    // Post: queue.n == 0
    public static void clear(ArrayQueueADT query) {
        assert query != null : "Queue is null";
        query.elements = new Object[5];
        query.begin = query.end = query.size = 0;
    }

}
