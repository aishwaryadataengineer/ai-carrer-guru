package com.aelionix.airesumebuilder.controller;

public class CustomHashMap {
    // Non-generic Entry class
    private static class Entry {
        Object key;
        Object value;
        Entry next;

        Entry(Object key, Object value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }

        @Override
        public String toString() {
            return "Entry{" +
                    "key=" + key +
                    ", value=" + value +
                    ", next=" + next +
                    '}';
        }
    }

    private Entry[] buckets;
    private int size;
    private int capacity;
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR_THRESHOLD = 0.75f;

    // Constructor
    public CustomHashMap() {
        this.capacity = INITIAL_CAPACITY;
        this.buckets = new Entry[INITIAL_CAPACITY];
        this.size = 0;
    }

    // Simple hash function
    private int getIndex(Object key) {
        if (key == null) return 0;
        int h = key.hashCode();
        return Math.abs(h) % capacity; // Simplest approach
    }

    // Resize the array
    private void resize() {
        Entry[] oldBuckets = buckets;
        capacity *= 2;
        buckets = new Entry[capacity];
        size = 0;

        for (Entry head : oldBuckets) {
            Entry current = head;
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }

    // Put key-value pair
    @SuppressWarnings("unchecked")
    public void put(Object key, Object value) {
        if ((float) size / capacity >= LOAD_FACTOR_THRESHOLD) {
            resize();
        }

        int index = getIndex(key);

        if (buckets[index] == null) {
            buckets[index] = new Entry(key, value);
            size++;
            return;
        }

        Entry current = buckets[index];
        while (current != null) {
            if ((current.key == null && key == null) ||
                    (current.key != null && current.key.equals(key))) {
                current.value = value;
                return;
            }
            if (current.next == null) break;
            current = current.next;
        }
        current.next = new Entry(key, value);
        size++;
    }

    // Get value by key
    @SuppressWarnings("unchecked")
    public Object get(Object key) {
        int index = getIndex(key);
        Entry current = buckets[index];

        while (current != null) {
            if ((current.key == null && key == null) ||
                    (current.key != null && current.key.equals(key))) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    // Remove key-value pair
    public boolean remove(Object key) {
        int index = getIndex(key);
        Entry current = buckets[index];
        Entry prev = null;

        while (current != null) {
            if ((current.key == null && key == null) ||
                    (current.key != null && current.key.equals(key))) {
                if (prev == null) {
                    buckets[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // New method to print all elements
    public void printAll() {
        System.out.println("HashMap Contents:");
        for (int i = 0; i < capacity; i++) {
            Entry current = buckets[i];
            while (current != null) {
                System.out.println("Key: " + current.key + ", Value: " + current.value);
                current = current.next;
            }
        }
        System.out.println("Total size: " + size);
    }

    // Test the implementation
    public static void main(String[] args) {
        CustomHashMap map = new CustomHashMap();

        // Add some entries
        map.put("One", 1);
        map.put("Two", 2);
        map.put("Three", 3);

        // Print all elements
        map.printAll();

        // Update and remove
        map.put("One", 10);
        map.remove("Two");

        // Print again
        System.out.println("\nAfter update and remove:");
        map.printAll();

        // Add more to trigger resize
        for (int i = 0; i < 20; i++) {
            map.put("Key" + i, i);
        }

        // Print after resize
        System.out.println("\nAfter adding 20 keys:");
        map.printAll();
    }
}