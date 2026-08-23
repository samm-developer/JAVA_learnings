// Lesson 13g: TreeMap, LinkedHashSet, ArrayDeque & map/queue pick guide
// Compile: javac LessonConsole.java Lesson13g_TreeMapAndMore.java
// Run:     java Lesson13g_TreeMapAndMore
//
// After: Lesson 13f (PriorityQueue)
//
// ========== CHEAT SHEET ==========
// TreeMap:        Red-Black tree — keys sorted, O(log n) get/put, navigable (floor/ceiling)
// LinkedHashSet:  HashSet speed + insertion order (HashMap keys only, dummy value)
// ArrayDeque:     circular array — fast queue/stack (prefer over LinkedList)
// EnumMap:        array indexed by enum — very fast, enum keys only

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

public class Lesson13g_TreeMapAndMore {

    public static void main(String[] args) {
        treeMapInternal();
        treeMapDemo();
        linkedHashSetDemo();
        arrayDequeDemo();
        fullPickGuide();
    }

    static void treeMapInternal() {
        LessonConsole.heading("=== 0) TreeMap internal (Red-Black tree) ===");
        System.out.println("""
                TreeMap stores Entry nodes in a balanced BST (Red-Black tree):

                      [M]
                     /   \\
                   [D]   [R]
                   / \\     \\
                 [A] [F]   [Z]

                get/put/remove → O(log n) — walk tree comparing keys
                Keys must be Comparable OR pass Comparator at construction
                Benefit: sorted keys + floorKey/ceilingKey/higherKey
                """);
    }

    static void treeMapDemo() {
        LessonConsole.heading("=== 1) TreeMap — sorted keys ===");
        NavigableMap<Integer, String> scores = new TreeMap<>();
        scores.put(85, "Riya");
        scores.put(92, "Dev");
        scores.put(78, "Asha");
        System.out.println("  insert order 85,92,78 → keys sorted: " + scores.keySet());
        System.out.println("  floorKey(90)=" + scores.floorKey(90) + "  ceilingKey(90)=" + scores.ceilingKey(90));
        System.out.println("  firstEntry=" + scores.firstEntry() + "  lastEntry=" + scores.lastEntry());
        System.out.println();
    }

    static void linkedHashSetDemo() {
        LessonConsole.heading("=== 2) LinkedHashSet — unique + insertion order ===");
        System.out.println("""
                Internal: HashMap<E, dummy> + doubly-linked list through keys
                (same idea as LinkedHashMap but only keys, no values you care about)
                """);

        Set<String> set = new LinkedHashSet<>();
        set.add("java");
        set.add("oop");
        set.add("java"); // duplicate ignored
        set.add("spring");
        System.out.println("  add order java, oop, java, spring → " + set + "  ✅ order kept");
        System.out.println();
    }

    static void arrayDequeDemo() {
        LessonConsole.heading("=== 3) ArrayDeque — queue & stack on circular array ===");
        System.out.println("""
                Internal: Object[] elements + head/tail indices (ring buffer)
                offerLast/pollFirst → FIFO queue
                push/pop            → stack (LIFO)
                """);

        Deque<String> q = new ArrayDeque<>();
        q.offerLast("A");
        q.offerLast("B");
        q.offerLast("C");
        System.out.print("  FIFO pollFirst: ");
        while (!q.isEmpty()) {
            System.out.print(q.pollFirst() + " ");
        }
        System.out.println();

        Deque<String> stack = new ArrayDeque<>();
        stack.push("X");
        stack.push("Y");
        System.out.println("  stack push X,Y → pop=" + stack.pop() + " (top first)");
        System.out.println();
    }

    static void fullPickGuide() {
        LessonConsole.heading("=== 4) Full pick guide (maps + queues + sets) ===");
        System.out.println("""
                MAP
                  HashMap              fast, no order              → 13c
                  LinkedHashMap        insert or access order      → 13d
                  ConcurrentHashMap    multi-thread                → 13e
                  TreeMap              sorted keys, O(log n)       → this file

                QUEUE / DEQUE
                  PriorityQueue        smallest/largest next       → 13f
                  ArrayDeque           FIFO/LIFO, fast             → this file
                  PriorityBlockingQueue thread-safe priority queue

                SET
                  HashSet              unique, unordered
                  LinkedHashSet        unique, insert order
                  TreeSet              unique, sorted

                Legacy (avoid for new code): Vector, Hashtable (global sync)
                """);
        System.out.println("Series complete: 13 → 13b → 13c → 13d → 13e → 13f → 13g");
    }
}
