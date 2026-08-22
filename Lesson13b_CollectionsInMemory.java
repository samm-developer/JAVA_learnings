// Lesson 13b: Collections in memory (how they really sit on the heap)
// Compile: javac Lesson13b_CollectionsInMemory.java
// Run:     java Lesson13b_CollectionsInMemory
//
// Place: after Lesson 13 (Collections) + useful with Lesson 36c (JVM heap/stack).
//
// Big picture:
//   STACK → variable holding a REFERENCE (address)
//   HEAP  → the collection object + its internal structure + the elements

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class Lesson13b_CollectionsInMemory {
    public static void main(String[] args) {
        stackVsHeap();
        arrayListMemory();
        linkedListMemory();
        hashMapMemory();
        hashSetMemory();
        chooseGuide();
    }

    static void stackVsHeap() {
        System.out.println("=== 0) Stack vs Heap ===");
        System.out.println("""
                ArrayList<String> list = new ArrayList<>();
                list.add("Asha");

                  STACK (main frame)              HEAP
                  -------------------              ----
                  list ───────────────►  ArrayList object
                                              │
                                              ├── size = 1
                                              ├── elementData[] ──► [ ref0, null, null, ... ]
                                              │                         │
                                              └── (capacity often 10)   └──► String "Asha"
                """);
    }

    static void arrayListMemory() {
        System.out.println("=== 1) ArrayList — contiguous Object[] on the heap ===");
        System.out.println("""
                Internally (simplified):
                  ArrayList
                    ├── size          (how many elements YOU added)
                    └── elementData[] (backing array; capacity >= size)

                get(i) / set(i)  → O(1)  (jump to index in array)
                add at end       → O(1) amortized (sometimes grow = copy)
                add/remove middle→ O(n)  (shift elements)

                Growth (idea):
                  capacity full → allocate bigger array → copy old refs → add new item
                """);

        ArrayList<String> list = new ArrayList<>(); // starts empty; grows as you add
        System.out.println("empty → size=" + list.size());

        list.add("A");
        list.add("B");
        list.add("C");
        System.out.println("after 3 adds → size=" + list.size() + "  elements=" + list);

        // Ask for room up front (optional optimization — still one Object[] on heap)
        list.ensureCapacity(100);
        System.out.println("after ensureCapacity(100) → size still " + list.size()
                + " (size = elements used; capacity = array length behind the scenes)");

        list.trimToSize(); // shrink backing array to fit size (saves memory)
        System.out.println("after trimToSize() → size=" + list.size() + " (capacity matched to size)");
        System.out.println();
    }

    static void linkedListMemory() {
        System.out.println("=== 2) LinkedList — nodes linked by pointers ===");
        System.out.println("""
                Internally (simplified):
                  LinkedList
                    ├── first ──► Node("A") ──► Node("B") ──► Node("C") ──► null
                    └── last  ───────────────────────────────▲

                Each Node on the HEAP has: item + next (+ prev for doubly-linked)

                get(i)           → O(n)  (walk from start)
                add/remove ends  → O(1)
                add/remove known → O(1) at that node, but finding it is O(n)
                Extra memory     → each element needs a Node object (more GC pressure)
                """);

        LinkedList<String> ll = new LinkedList<>();
        ll.add("A");
        ll.add("B");
        ll.add("C");
        System.out.println("LinkedList demo: " + ll);
        System.out.println();
    }

    static void hashMapMemory() {
        System.out.println("=== 3) HashMap — array of buckets + chains ===");
        System.out.println("""
                Internally (simplified):
                  HashMap
                    └── table[]  (array of buckets on HEAP)                                                                               

                  hash(key) → index into table[]

                  table[i] ──► Entry(K,V) ──► Entry(K,V) ──► null
                               (same bucket = hash collision chain;
                                long chains may become balanced trees)

                put/get average → O(1)
                worst (bad hash)→ O(n)
                Keys need good hashCode() + equals()
                """);

        HashMap<String, Integer> map = new HashMap<>();
        map.put("Asha", 90);
        map.put("Riya", 85);
        map.put("Dev", 92);
        System.out.println("HashMap demo: " + map);
        System.out.println("get(\"Riya\") uses hash → bucket → equals check → " + map.get("Riya"));
        System.out.println();
    }

    static void hashSetMemory() {
        System.out.println("=== 4) HashSet — basically a HashMap under the hood ===");
        System.out.println("""
                HashSet stores values as HashMap KEYS (dummy value object).
                So memory shape ≈ HashMap, uniqueness via hashCode/equals.
                """);

        HashSet<String> set = new HashSet<>();
        set.add("java");
        set.add("oop");
        set.add("java"); // ignored — same key in internal map
        System.out.println("HashSet demo: " + set);
        System.out.println();
    }

    static void chooseGuide() {
        System.out.println("=== 5) What to pick (memory + speed) ===");
        System.out.println("""
                Need index access / mostly append?     → ArrayList  (tight array of refs)
                Lots of insert/remove at ends?        → LinkedList (rarely needed in practice)
                Key → value lookup?                   → HashMap
                Unique values only?                   → HashSet

                Memory tip:
                  ArrayList stores references in ONE array (cache-friendly).
                  LinkedList / map chains create MANY small Node/Entry objects.
                """);
    }
}
