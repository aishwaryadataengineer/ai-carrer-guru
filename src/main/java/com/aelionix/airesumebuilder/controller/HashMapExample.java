package com.aelionix.airesumebuilder.controller;

import java.util.Arrays;

public class HashMapExample<K,V> {

    private int size;
    private int capacity;
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR_THRESHOLD = 0.75f;
    private Node[] buckets;


    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> nextNode;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public HashMapExample() {
        capacity = INITIAL_CAPACITY;
        buckets = new Node[capacity];
        size = 0;
    }

    @Override
    public String toString() {
        return "HashMapExample{" +
                "buckets=" + Arrays.toString(buckets) +
                '}';
    }
    private int getIndex(K key) {
        if (key == null) return 0;
        int h = key.hashCode();
        h = h * 31; // Multiply by a small prime for basic mixing
        return Math.abs(h) % capacity;
    }
    private void insert(K key, V value) {

        if(size/capacity >= LOAD_FACTOR_THRESHOLD) {
            resize();
        }

        int h = getIndex(key);
        if(buckets[h]==null) {
            buckets[h] = new Node<>(key, value);
            size++;
            return;
        }

    }

    // Resize the array
    private void resize() {
        Node[] oldBuckets = buckets;
        capacity *= 2;
        buckets = new Node[capacity]; // No cast needed
        size = 0;

        for (Node<K,V> head : oldBuckets) {
            Node<K,V> current = head;
            while (current != null) {
                insert(current.key, current.value);
                current = current.nextNode;
            }
        }
    }


    public static void main(String[] args) {

    }
}
