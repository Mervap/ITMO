package queue;

import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractQueue implements Queue {
    protected int size = 0;

    public void enqueue(Object x) {
        assert x != null : "Element is null";
        enqueueUpdate(x);
        ++size;
    }

    protected abstract void enqueueUpdate(Object x);

    public Object dequeue() {
        assert size > 0 : "Queue is empty";
        Object ans = element();
        dequeueUpdate();
        --size;
        return ans;
    }

    protected abstract void dequeueUpdate();

    public Object element() {
        assert size > 0 : "Queue is empty";
        return elementUpdate();
    }

    protected abstract Object elementUpdate();

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        clearUpdate();
        size = 0;
    }

    protected abstract void clearUpdate();

    protected abstract AbstractQueue createCopy();

    public AbstractQueue filter(Predicate <Object> predicate) {
        assert predicate != null;
        AbstractQueue result = createCopy();
        for (int i = 0; i < size; ++i) {
            Object tmp = result.dequeue();
            if (predicate.test(tmp)) {
                result.enqueue(tmp);
            }
        }
        return result;
    }

    public AbstractQueue map(Function <Object, Object> func) {
        assert func != null;
        AbstractQueue result = createCopy();
        for (int i = 0; i < size; i++) {
            result.enqueue(func.apply(result.dequeue()));
        }
        return result;
    }
}
