// Lesson 13c: HashMap — deep dive + internal working
// Compile: javac LessonConsole.java Lesson13c_HashMapDeepDive.java
// Run:     java Lesson13c_HashMapDeepDive
//
// After: Lesson 13 (Collections), 13b (memory overview)
// Study order:
//   13c HashMap          ← YOU ARE HERE
//   13d LinkedHashMap
//   13e ConcurrentHashMap
//   13f PriorityQueue
//   13g TreeMap & more
//
// ========== CHEAT SHEET ==========
// Structure: table[] buckets → each bucket = chain/tree of Entry(key,value)
// put(k,v):  hash(k) → index → equals check in bucket → add/overwrite
// get(k):    same path to find value
// Average:   O(1) put/get   |   Worst (all collide): O(n)
// Keys MUST implement hashCode() + equals() correctly (or use String/Integer)

import java.util.HashMap;
import java.util.Map;

public class Lesson13c_HashMapDeepDive {

    public static void main(String[] args) {
        internalPicture();
        putGetFlow();
        problemBadKey();
        solutionGoodKey();
        overwriteAndNull();
        loadFactorResize();
        summary();
    }

    static void internalPicture() {
        LessonConsole.heading("=== 0) Internal shape (simplified Java HashMap) ===");
        System.out.println("""
                HashMap on HEAP
                  └── table[]  (array of buckets)

                  hash(key) → bucket index (0 .. table.length-1)

                  table[2] ──► Node(K1,V1) ──► Node(K2,V2) ──► null
                               same bucket = hash collision
                               (long chains may become balanced tree)

                  Each Node stores: hash, key, value, next
                """);
    }

    static void putGetFlow() {
        LessonConsole.heading("=== 1) put / get flow ===");
        Map<String, Integer> marks = new HashMap<>();
        marks.put("Asha", 90);
        marks.put("Riya", 85);

        String key = "Asha";
        int h = key.hashCode();
        System.out.println("  key=\"Asha\" hashCode=" + h);
        System.out.println("  (JDK mixes bits, then index = (n-1) & hash into table[])");
        System.out.println("  get(\"Asha\") → hash → bucket → equals(\"Asha\") → " + marks.get("Asha"));
        System.out.println();
    }

    static void problemBadKey() {
        LessonConsole.heading("=== PROBLEM: key class without hashCode/equals ===");
        Map<BadStudent, Integer> map = new HashMap<>();
        BadStudent a = new BadStudent("Asha");
        BadStudent b = new BadStudent("Asha"); // same name, different object

        map.put(a, 90);
        System.out.println("  put(a, 90)");
        System.out.println("  get(b) = " + map.get(b) + "  ❌ null — HashMap can't find 'same' student");
        System.out.println("  map.size=" + map.size() + " (looks like duplicates could exist)");
        System.out.println();
    }

    static void solutionGoodKey() {
        LessonConsole.heading("=== SOLUTION: consistent hashCode + equals ===");
        Map<GoodStudent, Integer> map = new HashMap<>();
        GoodStudent a = new GoodStudent("Asha");
        GoodStudent b = new GoodStudent("Asha");

        map.put(a, 90);
        System.out.println("  put(a, 90)");
        System.out.println("  get(b) = " + map.get(b) + "  ✅ same logical key finds value");
        System.out.println("  put(b, 95) overwrites → " + map.get(b) + " (one entry)");
        System.out.println();
    }

    static void overwriteAndNull() {
        LessonConsole.heading("=== 2) Overwrite + null key (one null key allowed) ===");
        Map<String, String> cfg = new HashMap<>();
        cfg.put("theme", "light");
        cfg.put("theme", "dark"); // same key → replace value
        cfg.put(null, "no-key-default");
        System.out.println("  after puts: " + cfg);
        System.out.println("  get(null)=" + cfg.get(null));
        System.out.println();
    }

    static void loadFactorResize() {
        LessonConsole.heading("=== 3) Load factor & resize (why capacity matters) ===");
        System.out.println("""
                Default load factor ≈ 0.75
                  size > capacity * 0.75  →  new bigger table[] → rehash all entries

                More entries without resize → longer chains → slower get/put

                Tip: new HashMap<>(expectedSize) avoids repeated resize while growing
                """);
        HashMap<Integer, String> map = new HashMap<>(16); // 16 buckets upfront
        for (int i = 0; i < 5; i++) {
            map.put(i, "v" + i);
        }
        System.out.println("  demo map size=" + map.size() + " " + map);
        System.out.println();
    }

    static void summary() {
        LessonConsole.heading("=== Summary: HashMap ===");
        System.out.println("""
                Use when:     fast key → value lookup, order does NOT matter
                Internal:     array of buckets + collision chains (trees if long)
                Key rule:     hashCode + equals must agree
                Not thread-safe → see Lesson 13e ConcurrentHashMap
                Next:         Lesson 13d LinkedHashMap (insertion / access order)
                """);
    }

    // --- bad key: Object.equals/hashCode (identity) ---
    static class BadStudent {
        final String name;
        BadStudent(String name) { this.name = name; }
    }

    // --- good key ---
    static class GoodStudent {
        final String name;
        GoodStudent(String name) { this.name = name; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof GoodStudent other)) return false;
            return name.equals(other.name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }
}
