import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * A class to represent a HashTable
 * @author Shravani Suram
 */
public class HashTable<T> {
    /**
     * nested class representing hash nodes storing key and value
     */
    public class HashNode<T> implements Comparable<HashNode<T>> {
        /**
         * stores the key of the hash node
         */
        private String key;
        /**
         * stores the value of the hash node
         */
        private T value;
        /**
         * stores the next node in the list
         */
        private HashNode<T> next;

        /**
         * Creates a HashNode object
         *
         * @param key   the key of the HashNode
         * @param value the value of the HashNode
         */
        public HashNode(String key, T value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }

        /**
         * Sets the next HashNode in list
         */
        public void setNext(HashNode<T> next) {
            this.next = next;
        }

        /**
         * Returns the next HashNode in list
         */
        public HashNode<T> getNext() {
            return this.next;
        }

        /**
         * Returns the key of the specified node
         *
         * @return the key of the node
         */
        public String getKey() {
            return this.key;
        }

        /**
         * Returns the value of the specified node
         *
         * @return the value of the node
         */
        public T getValue() {
            return this.value;
        }

        /**
         * Compares the nodes by their keys
         *
         * @return returns the subtraction of the keys
         */
        @Override
        public int compareTo(HashNode node) {
            return (Integer) this.value - (Integer) node.value;
        }
    }

    /**
     * Stores the number of keys in the table
     */
    private int size = 0;

    /**
     * Stores the total possible capacity of the table
     */
    private int capacity = 0;
    /**
     * Stores the HashTable as a list
     */
    private ArrayList<HashNode<T>> table;

    /**
     * Creates the HashTable object and initializes capacity to 10
     */
    public HashTable() {
        table = new ArrayList<HashNode<T>>(10);
        capacity = 10;
        HashNode<T> node = new HashNode<T>(null, null);
        /** Adds null nodes to each bucket */
        for (int i = 0; i < capacity; i++) {
            table.add(i, node);
        }
    }

    /**
     * Creates the HashTable object with given capacity
     *
     * @param capacity the maximum number of elements table can hold
     * @throws throws IllegalArgumentException
     *                if capacity is negative
     */
    public HashTable(int capacity) {
        if (capacity > 0) {
            table = new ArrayList<HashNode<T>>(capacity);
            this.capacity = capacity;
            HashNode<T> node = new HashNode<T>(null, null);
            /** Adds empty nodes to all of the buckets */
            for (int i = 0; i < capacity; i++) {
                table.add(i, node);
            }
        } else
            throw new IllegalArgumentException();
    }

    /**
     * Returns the size of the table
     *
     * @return the size of the table
     */
    public int getSize() {
        return size;
    }

    /**
     * Copies HashTable into an ArrayList
     */
    public ArrayList<HashNode<T>> getTable() {
        ArrayList<HashNode<T>> array = new ArrayList<HashNode<T>>(this.size);
        HashNode<T> node, node1;
        /** Traverses through each bucket of hashtable */
        for (int i = 0; i < capacity; i++) {
            node = table.get(i);
            /** Traverses through linked list within each bucket */
            while (node != null) {
                if (node.key != null) {
                    node1 = new HashNode<T>(node.key, node.value);
                    array.add(node1);
                }
                node = node.getNext();
            }
        }
        return array;
    }

    /**
     * Returns the value of the specified key, if present in the table
     *
     * @param key the key that is to be found
     * @return the value of the key found
     * @throws throws NoSuchElementException
     *                when key is not present in table
     */
    public T get(String key) {
        int index = 0;
        HashNode<T> element;

        index = Math.abs(key.hashCode()) % capacity;
        element = this.table.get(index);
        /** if key does not exist in table */
        if (element.key == null) {
            throw new NoSuchElementException();
        }
        /** Traverse through table until find matching key */
        while (element != null) {
            if (element.key.equals(key) == true) {
                return element.value;
            }
            element = element.getNext();
        }
        throw new NoSuchElementException();
    }

    /**
     * Adds the inputted key and value as a node to the hashtable
     *
     * @param key   the key that will be added
     * @param value the value that will be added
     */
    public void put(String key, T value) {
        int index;
        HashNode nodeptr = null;
        HashNode node;
        HashNode prev;
        boolean found;

        prev = null;
        found = false;
        node = new HashNode(key, value);
        index = Math.abs(key.hashCode()) % capacity;
        try {
            nodeptr = table.get(index);
            /** Traverse through table and check if duplicate keys or else add to correct index */
            while (nodeptr != null) {
                /** if Integer add to previous value */
                if (key.equals(nodeptr.key) == true) {
                    if (value instanceof Integer && nodeptr.value instanceof Integer) {
                        nodeptr.value = (Integer) value + (Integer) nodeptr.value;
                        found = true;
                        break;
                    }
                    /** If not integer, replace value */
                    else {
                        nodeptr.value = value;
                        found = true;
                        break;
                    }
                }
                /** Traverse to next bucket in table */
                else {
                    prev = nodeptr;
                    nodeptr = nodeptr.getNext();
                }
            }
            /** If not duplicate, then add within bucket or add to slot itself */
            if (found == false) {
                if (prev.key != null) {
                    prev.setNext(node);
                } else {
                    table.set(index, node);
                }
                size++;
            }
        } catch (IndexOutOfBoundsException e) {
        }
        /** Adjust capacity if load factor is 1 */
        if (size / capacity == 1) {
            table.ensureCapacity(capacity * 2);
            HashNode<T> empty = new HashNode<T>(null, null);
            for (int i = size; i < capacity; i++) {
                table.add(i, empty);
            }
        }
    }

    /**
     * Removes the specified node from the hashtable
     *
     * @param key the key to be removed
     * @return the value of the key that was removed
     * @throws throws NoSuchElementException
     *                if key is not found in table
     */
    public T remove(String key) {
        /** stores the index that the key is at */
        int index;
        HashNode node;
        /** stores the prev node if it is in a bucket */
        HashNode prev = null;
        HashNode save;

        index = Math.abs(key.hashCode()) % capacity;
        node = table.get(index);
        /** if key is not present in table */
        if (node.key == null) {
            throw new NoSuchElementException();
        }
        /** If entry is not found */
        while (node != null) {
            if (key.equals(node.key) == true) {
                save = node.getNext();
                /** if not the first node of list or only node at index */
                if (prev != null) {
                    prev.setNext(save);
                    size--;
                    return (T) node.value;
                }
                /** if first node or only node */
                else {
                    if (save == null) {
                        save = new HashNode(null, null);
                    }
                    table.set(index, save);
                    size--;
                    return (T) node.value;
                }
            }
            /** Traverse to next node within bucket */
            else {
                prev = node;
                node = node.getNext();
            }
        }
        throw new NoSuchElementException();
    }

    /**
     * Returns the number of elements in the table
     *
     * @return the number of elements in table
     */
    public int size() {
        return this.size;
    }
}