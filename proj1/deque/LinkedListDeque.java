package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T>, Deque<T>{
    private class Node {
        private T item;
        private Node next;
        private Node pre;
        public Node(T i, Node p, Node n) {
            item = i;
            next = n;
            pre =p;
        }
    }

    Node sentinel;
    int size;

    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.pre = sentinel;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(T item) {
        Node newNode = new Node(item, sentinel, sentinel.next);
        sentinel.next.pre = newNode;
        sentinel.next = newNode;
        size += 1;
    }
    @Override
    public void addLast(T item) {
        Node newNode = new Node(item, sentinel.pre, sentinel);
        sentinel.pre.next = newNode;
        sentinel.pre = newNode;
        size += 1;
    }

    @Override
    public void printDeque() {
        Node p = sentinel.next;
        while (p != sentinel) {
            System.out.print(p.item);
            System.out.print(' ');
            p = p.next;
        }
        System.out.println(' ');
    }

    // Removes and returns the item at the front of the deque. If no such item exists, returns null.
    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T item = sentinel.next.item;
        sentinel.next.next.pre = sentinel;
        sentinel.next = sentinel.next.next;
        size -= 1;
        return item;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        Node newLast = sentinel.pre.pre;
        T item = newLast.next.item;
        newLast.next = sentinel;
        sentinel.pre = newLast;
        size -= 1;
        return item;
    }

    // Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
    // If no such item exists, returns null. Must not alter the deque!
    @Override
    public T get(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        Node p = sentinel;
        for(int i = 0; i <= index; i++) {
            p = p.next;
        }
        return p.item;
    }

    // Same as get, but uses recursion.
    public T getRecursive(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        return getRecursiveHelp(index).item;
    }

    // help method
    private Node getRecursiveHelp(int index) {
        if (index == 0) {
            return sentinel.next;
        }
        return getRecursiveHelp(index - 1).next;
    }

    private class LLDIterator implements Iterator<T> {
        private Node wizPoz;
        public LLDIterator() {
            wizPoz = sentinel.next;
        }

        @Override
        public boolean hasNext() {
            return wizPoz != sentinel;
        }

        @Override
        public T next() {
            T item = wizPoz.item;
            wizPoz = wizPoz.next;
            return item;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new LLDIterator();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Deque ) {
            Deque oLLD = (Deque)o;
            if (size != oLLD.size()) {
                return false;
            }
            int i = 0;
            for (T x: this) {
                if (!x.equals(oLLD.get(i))) {
                    return false;
                }
                i++;
            }
            return true;
        }
        return false;
    }
}
