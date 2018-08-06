package queue;

public class LinkedQueue extends AbstractQueue {
    private Node head;
    private Node tail;

    protected void enqueueUpdate(Object x) {
        if (size == 0) {
            head = tail = new Node(x);
        } else {
            tail.next = new Node(x);
            tail = tail.next;
        }
    }

    protected void dequeueUpdate() {
        head = head.next;
        if (size == 1) {
            tail = null;
        }
    }

    protected Object elementUpdate() {
        return head.value;
    }

    protected void clearUpdate() {
        head = tail = null;
    }

    protected LinkedQueue createCopy() {
        LinkedQueue result = new LinkedQueue();
        Node tmp = head;
        while (tmp != null) {
            result.enqueue(tmp.value);
            tmp = tmp.next;
        }
        return result;
    }

    private class Node {
        private Object value;
        private Node next;

        public Node(Object value) {
            this(value, null);
        }

        public Node(Object value, Node next) {
            assert value != null;
            this.value = value;
            this.next = next;
        }
    }
}
