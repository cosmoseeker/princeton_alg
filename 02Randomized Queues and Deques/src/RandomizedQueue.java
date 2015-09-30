import java.util.Iterator;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] rQueue;
    private int    N;

    // construct an empty randomized queue
    public RandomizedQueue() {
        this.rQueue = (Item[]) new Object[1];
        this.N = 0;
    }

    // is the queue empty?
    public boolean isEmpty() {
        return this.N == 0;
    }

    // return the number of items on the queue
    public int size() {
        return this.N;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new java.lang.NullPointerException();
        }
        if (this.N == this.rQueue.length) {
            resize(2 * this.rQueue.length);
        }
        this.rQueue[this.N++] = item;
    }

    private void resize(int capcity) {
        Item[] resizeRQ = (Item[]) new Object[capcity];
        for (int i = 0; i < this.N; i++) {
            resizeRQ[i] = this.rQueue[i];
        }
        this.rQueue = resizeRQ;
    }

    // remove and return a random item
    public Item dequeue() {
        if (this.isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        int index = StdRandom.uniform(this.N);
        Item item = this.rQueue[index];
        // for random queue, just move last item to index
        // this enhances score from 97.61 to 100
        this.rQueue[index] = this.rQueue[this.N - 1];
        this.rQueue[this.N - 1] = null;// avoid loitering
        /*
         * for (int i = index + 1; i < this.N; i++) { this.rQueue[i - 1] =
         * this.rQueue[i]; }
         */
        this.N--;
        if (this.N > 0 && N == this.rQueue.length / 4) {
            resize(this.rQueue.length / 2);
        }
        return item;
    }

    // return (but do not remove) a random item
    public Item sample() {
        if (this.isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        int index = StdRandom.uniform(this.N);
        return this.rQueue[index];
    }

    @Override
    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RQIterator();
    }

    private class RQIterator implements Iterator<Item> {
        private Item[] rqIterator;
        private int    index = 0;

        public RQIterator() {
            this.rqIterator = (Item[]) new Object[N];
            for (int i = 0; i < N; i++) {
                this.rqIterator[i] = rQueue[i];
            }
            StdRandom.shuffle(this.rqIterator);
        }

        @Override
        public boolean hasNext() {
            return this.index < N;
        }

        @Override
        public Item next() {
            if (!hasNext())
                throw new java.util.NoSuchElementException();
            Item item = this.rqIterator[index++];
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
