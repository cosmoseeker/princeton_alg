import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private class Node {
        private Item item;
        private Node prev;
        private Node next;
    }
    private Node first;
    private Node tail;
    private int  N;

    // construct an empty deque
    public Deque() {
        this.first = null;
        this.tail = null;
        this.N = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return this.N == 0;

    }

    // return the number of items on the deque
    public int size() {
        return this.N;

    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new java.lang.NullPointerException();
        }
        Node node = new Node();
        node.item = item;
        node.prev = null;
        if (this.isEmpty()) {
            node.next = null;
            this.tail = node;
        } else {
            node.next = this.first;
            this.first.prev = node;
        }
        this.first = node;
        this.N++;
    }

    // add the item to the end
    public void addLast(Item item) {
        if (item == null) {
            throw new java.lang.NullPointerException();
        }
        Node node = new Node();
        node.item = item;
        node.next = null;
        if (this.isEmpty()) {
            node.prev = null;
            this.first = node;
        } else {
            node.prev = this.tail;
            this.tail.next = node;
        }
        this.tail = node;
        this.N++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (this.isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        Item item = this.first.item;
        this.first = this.first.next;
        this.N--;
        // remove old first
        if (this.isEmpty()) {
            this.tail = null;
        } else {
            this.first.prev = null;
        }
        return item;

    }

    // remove and return the item from the end
    public Item removeLast() {
        if (this.isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        Item item = this.tail.item;
        this.tail = this.tail.prev;
        this.N--;
        // remove old first
        if (this.isEmpty()) {
            this.first = null;
        } else {
            this.tail.next = null;
        }
        return item;
    }

    @Override
    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new QueueIterator();
    }

    private class QueueIterator implements Iterator<Item> {
        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            Item item = current.item;
            current = current.next;
            return item;
        }

        @Override
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }

    }

    // unit testing
    public static void main(String[] args) {

    }

}
