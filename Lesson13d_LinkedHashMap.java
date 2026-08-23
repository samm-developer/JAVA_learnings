// Lesson 13d: LinkedHashMap — order + internal working
// Compile: javac LessonConsole.java Lesson13d_LinkedHashMap.java
// Run:     java Lesson13d_LinkedHashMap
//
// After: Lesson 13c (HashMap)
//
// ========== CHEAT SHEET ==========
// Same bucket structure as HashMap PLUS a doubly-linked list through ALL entries
// insertion-order  → iteration order = order you put keys (default)
// access-order     → get/put moves entry to tail (LRU cache pattern)
// Memory:          extra prev/next pointers per entry vs HashMap
// Use when:        predictable iteration order or simple LRU cache

import java.util.LinkedHashMap;
import java.util.Map;

public class Lesson13d_LinkedHashMap {

    public static void main(String[] args) {
        internalPicture();
        problemHashMapNoOrder();
        solutionInsertionOrder();
        accessOrderLruCache();
        summary();
    }

    static void internalPicture() {
        LessonConsole.heading("=== 0) Internal shape ===");
        System.out.println("""
                LinkedHashMap = HashMap buckets  +  doubly-linked list

                  table[] buckets (same as HashMap — fast lookup)

                  head ⇄ Entry A ⇄ Entry B ⇄ Entry C ⇄ tail
                         (insertion / access order chain)

                get(key): hash → bucket (fast) + optionally relink if access-order
                iterate:  walk the linked list → stable order
                """);
    }

    static void problemHashMapNoOrder() {
        LessonConsole.heading("=== PROBLEM: HashMap iteration order unpredictable ===");
        Map<String, Integer> hash = new java.util.HashMap<>();
        hash.put("Zebra", 1);
        hash.put("Apple", 2);
        hash.put("Mango", 3);
        System.out.println("  put order: Zebra, Apple, Mango");
        System.out.println("  HashMap iteration: " + hash.keySet() + "  ❌ not insert order");
        System.out.println();
    }

    static void solutionInsertionOrder() {
        LessonConsole.heading("=== SOLUTION: LinkedHashMap (insertion-order) ===");
        LinkedHashMap<String, Integer> linked = new LinkedHashMap<>();
        linked.put("Zebra", 1);
        linked.put("Apple", 2);
        linked.put("Mango", 3);
        System.out.println("  put order: Zebra, Apple, Mango");
        System.out.println("  LinkedHashMap iteration: " + linked.keySet() + "  ✅ insert order");
        linked.put("Apple", 99); // update value — does NOT move position
        System.out.println("  after put Apple again:   " + linked.keySet() + " (Apple stays in place)");
        System.out.println();
    }

    static void accessOrderLruCache() {
        LessonConsole.heading("=== BONUS: access-order = LRU cache ===");
        // max 3 entries; eldest removed when size > 3
        LinkedHashMap<String, String> lru = new LinkedHashMap<>(4, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                return size() > 3;
            }
        };

        lru.put("A", "pageA");
        lru.put("B", "pageB");
        lru.put("C", "pageC");
        System.out.println("  after A,B,C: " + lru.keySet());

        lru.get("A"); // access A → moves to tail (recently used)
        System.out.println("  after get(A): " + lru.keySet() + " (A moved to end)");

        lru.put("D", "pageD"); // cache full → eldest (B) evicted
        System.out.println("  after put(D): " + lru.keySet() + "  ✅ B evicted (least recently used)");
        System.out.println();
    }

    static void summary() {
        LessonConsole.heading("=== Summary: LinkedHashMap ===");
        System.out.println("""
                vs HashMap:     same O(1) avg get/put + predictable iteration order
                insertion:    new LinkedHashMap<>()
                access-order: new LinkedHashMap<>(16, 0.75f, true)  // 3rd arg true
                LRU cache:    override removeEldestEntry()
                Next:         Lesson 13e ConcurrentHashMap
                """);
    }
}
