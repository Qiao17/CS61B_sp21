package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }

        @Override
        public boolean equals(Object obj){
            Node o = (Node) obj;
            return (this.key == o.key && this.value == o.value);
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    //size is the number of elements in the map
    private int size;
    private double loadFactor;

    /** Constructors */
    public MyHashMap() {
        int initialSize = 16;
        buckets = createTable(initialSize);
        size = 0;
        loadFactor = 0.75;
    }

    public MyHashMap(int initialSize) {
        buckets = createTable(initialSize);
        size = 0;
        loadFactor = 0.75;
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        buckets = createTable(initialSize);
        size = 0;
        loadFactor = maxLoad;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return new Collection[tableSize];
    }

    @Override
    public void clear() {
        size = 0;
        for (Collection c: buckets) {
            if (c == null) {continue;}
            c.clear();
        }
    }

    @Override
    public boolean containsKey(K key) {
        int hashCode = key.hashCode();
        int index = Math.floorMod(hashCode, buckets.length);
        if (buckets[index] == null) {return false;}
        for (Node n: buckets[index]) {
            if (n.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V get(K key) {
        int hashCode = key.hashCode();
        int index = Math.floorMod(hashCode, buckets.length);
        if (buckets[index] == null) {return null;}
        for (Node n: buckets[index]) {
            if (n.key.equals(key)) {
                return n.value;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        if ((size + 1) / (double)buckets.length > loadFactor) {
            resize();
        }
        // get bucket index
        int hashCode = key.hashCode();
        int index = Math.floorMod(hashCode, buckets.length);
        if (buckets[index] == null) {
            buckets[index] = createBucket();
        } else {
            for (Node node: buckets[index]) {
                // update
                if (node.key.equals(key)) {
                    node.value = value;
                    return;
                }
            }
        }
        Node n = createNode(key, value);
        buckets[index].add(n);
        size += 1;
    }

    public void resize() {
        Collection<Node>[] old = buckets;
        int length = buckets.length;
        buckets = createTable(length * 2);
        int hashCode;
        int index;
        for (int i = 0; i < length; i++) {
            if (old[i] == null) {continue;}
            for (Node c: old[i]) {
                hashCode = c.key.hashCode();
                index = Math.floorMod(hashCode, length * 2);
                if (buckets[index] == null) {
                    buckets[index] = createBucket();
                }
                buckets[index].add(c);
            }
        }
    }

    @Override
    public Set<K> keySet() {
        Set<K> keys= new HashSet<>();
        for (int i = 0; i < buckets.length; i++) {
            if (buckets[i] != null) {
                for (Node n : buckets[i]) {
                    keys.add(n.key);
                }
            }
        }
        return keys;
    }

    @Override
    public V remove(K key) {
        if (!containsKey(key)) {return null;}
        int hashCode = key.hashCode();
        int index = Math.floorMod(hashCode, buckets.length);
        V value = get(key);
        Node nRemove = createNode(key, value);boolean b;
        buckets[index].remove(nRemove);
        size -= 1;
        return value;
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return new MyHashMapIterator();
    }

    private class MyHashMapIterator implements Iterator<K> {
        private Set<K> keys;
        private Iterator<K> iterator;
        public MyHashMapIterator() {
            keys = keySet();
            iterator = keys.iterator();
        }
        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public K next() {
            return iterator.next();
        }
    }
}
