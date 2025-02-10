# Comprehensive Guide to Java `Map` Interface (Java 21+)

## **1. Introduction to `Map` in Java**
A `Map` is a **key-value** based data structure in Java, where each key maps to exactly one value. Unlike Lists or Sets, a `Map` does **not allow duplicate keys**, but values can be duplicated.

Java provides several implementations of the `Map` interface in the `java.util` package, each with different characteristics.

---

## **2. `Map` Implementations in Java**
### **2.1. `HashMap`**
- **Unordered** collection.
- Allows **one `null` key** and multiple `null` values.
- **Not thread-safe**.

```java
Map<String, Integer> hashMap = new HashMap<>();
hashMap.put("Apple", 3);
hashMap.put("Banana", 2);
System.out.println(hashMap); // Output order is unpredictable
```

### **2.2. `LinkedHashMap`**
- Maintains **insertion order**.
- Slightly **slower** than `HashMap`.

```java
Map<String, Integer> linkedHashMap = new LinkedHashMap<>();
linkedHashMap.put("Apple", 3);
linkedHashMap.put("Banana", 2);
System.out.println(linkedHashMap); // Output: {Apple=3, Banana=2}
```

### **2.3. `TreeMap`**
- Implements a **Red-Black Tree**.
- Maintains **natural sorting order** of keys.
- **Does not allow `null` keys**.

```java
Map<String, Integer> treeMap = new TreeMap<>();
treeMap.put("Apple", 3);
treeMap.put("Banana", 2);
System.out.println(treeMap); // Output: {Apple=3, Banana=2}
```

### **2.4. `ConcurrentHashMap`**
- **Thread-safe** version of `HashMap`.
- Uses **segment locking** for better concurrency.
- **Does not allow `null` keys or values**.

```java
Map<String, Integer> concurrentMap = new ConcurrentHashMap<>();
concurrentMap.put("Apple", 3);
concurrentMap.put("Banana", 2);
System.out.println(concurrentMap);
```

---

## **3. Important Methods in `Map`**

| Method | Description |
|--------|-------------|
| `put(K key, V value)` | Inserts a key-value pair |
| `get(K key)` | Retrieves value for a key |
| `remove(K key)` | Deletes key-value pair |
| `containsKey(K key)` | Checks if key exists |
| `containsValue(V value)` | Checks if value exists |
| `size()` | Returns number of entries |
| `isEmpty()` | Checks if map is empty |

```java
Map<String, Integer> map = new HashMap<>();
map.put("Apple", 10);
map.put("Orange", 5);
System.out.println(map.get("Apple")); // Output: 10
map.remove("Orange");
System.out.println(map.containsKey("Apple")); // true
```

---

## **4. Using `Map` with Streams and Lambdas**
### **4.1. Filtering a `Map`**
```java
Map<String, Integer> map = Map.of("Apple", 50, "Banana", 30, "Mango", 40);

Map<String, Integer> filteredMap = map.entrySet()
    .stream()
    .filter(entry -> entry.getValue() > 35)
    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

System.out.println(filteredMap); // Output: {Apple=50, Mango=40}
```

### **4.2. Sorting a `Map` by Values**
```java
Map<String, Integer> sortedByValue = map.entrySet()
    .stream()
    .sorted(Map.Entry.comparingByValue())
    .collect(Collectors.toMap(
        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

System.out.println(sortedByValue); // Output: {Banana=30, Mango=40, Apple=50}
```

### **4.3. Transforming `Map` Values**
```java
Map<String, Integer> updatedMap = map.entrySet()
    .stream()
    .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue() + 10));

System.out.println(updatedMap); // Output: {Apple=60, Banana=40, Mango=50}
```

---

## **5. Java 21 Enhancements for `Map`**
### **5.1. `computeIfAbsent()` and `computeIfPresent()`**
```java
map.computeIfAbsent("Pineapple", k -> 100);
map.computeIfPresent("Apple", (k, v) -> v + 20);
System.out.println(map); // Output: {Apple=70, Banana=30, Mango=40, Pineapple=100}
```

### **5.2. Using `Map.copyOf()`**
```java
Map<String, Integer> immutableMap = Map.copyOf(map);
// immutableMap.put("NewFruit", 100); // Throws UnsupportedOperationException
```

---

## **6. Interview Questions on `Map`**
1. **What are the differences between `HashMap`, `LinkedHashMap`, and `TreeMap`?**
2. **Why does `HashMap` allow `null` keys but `TreeMap` does not?**
3. **How does `ConcurrentHashMap` achieve thread-safety?**
4. **How do you sort a `Map` by values using Java Streams?**
5. **Explain the difference between `computeIfAbsent` and `computeIfPresent`.**
6. **How do you filter a `Map` based on conditions using Streams?**
7. **Why are `Map.of()` and `Map.copyOf()` immutable in Java 9+?**
8. **Can you modify a `Map` while iterating over it? How?**
9. **What is the internal working of `HashMap` in Java?**
10. **Explain how Java 21 improves `Map` usability.**

---

## **7. Conclusion**
- `Map` is a **key-value** data structure with different implementations (`HashMap`, `TreeMap`, `LinkedHashMap`, etc.).
- Java **Streams and Lambdas** enhance `Map` processing by allowing **filtering, sorting, and transformations**.
- **Java 21** introduces new helper methods like `computeIfAbsent()`, `Map.copyOf()`, and performance improvements.

---

Use this guide as a **study reference for interviews** and Java development! 🚀

