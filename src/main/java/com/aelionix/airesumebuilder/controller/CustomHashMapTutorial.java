package com.aelionix.airesumebuilder.controller;

import java.util.HashMap;

public class CustomHashMapTutorial<K, V> {

    private int size;
    private int capacity;
    private Node<K, V>[] bucket;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    public CustomHashMapTutorial(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.bucket = new Node[capacity];
    }
    public CustomHashMapTutorial() {
        this.capacity = DEFAULT_INITIAL_CAPACITY;
        this.size = 0;
        this.bucket = new Node[capacity];
    }

    private class Node<K, V> {

        private K key;
        private V value;
        private Node<K, V> next;
        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "value=" + value +
                    ", key=" + key +
                    '}';
        }
    }
    private int getIndex(K key) {
        if (key == null) return 0;
        int h = key.hashCode();
        h = h * 31; // Multiply by a small prime for basic mixing
        return Math.abs(h) % capacity;
    }

    private Node<K, V> insert(K key, V value) {
        int index = getIndex(key);
        if (bucket[index] == null) {
            bucket[index] = new Node(key, value);
            size++;
            return bucket[index];
        }
        Node<K, V> node = bucket[index];
        while (node!= null) {

            if (node.key.equals(key)) {
                node.value = value;
            }
            if(node.next==null) return node;
            node = node.next;

        }
        node.next = new Node<>(key, value);
        return node;
    }

    private Object getValue(K key) {
        int h = getIndex(key);
        if(bucket[h] == null) return null;

        Node<K, V> node = bucket[h];
        while (node!= null) {
            if (node.key.equals(key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }
    public void printAll() {
        System.out.println("HashMap Contents:");
        for (int i = 0; i < capacity; i++) {
            CustomHashMapTutorial.Node current = bucket[i];
            while (current != null) {
                System.out.println("Key: " + current.key + ", Value: " + current.value);
                current = current.next;
            }
        }
        System.out.println("Total size: " + size);
    }

    public static void main(String[] args) {

        CustomHashMapTutorial<Integer, Integer> customHashMapTutorial = new CustomHashMapTutorial<Integer, Integer>(20);
        customHashMapTutorial.insert(1, 1);
        customHashMapTutorial.insert(1, 2);
        customHashMapTutorial.insert(2, 2);
        customHashMapTutorial.insert(3, 2);
        customHashMapTutorial.insert(4, 2);
        customHashMapTutorial.insert(5, 2);
        System.out.println(customHashMapTutorial.getValue(1));
        customHashMapTutorial.printAll();
    }


}
