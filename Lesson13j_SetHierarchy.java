// Lesson 13j: Set hierarchy — HashSet, LinkedHashSet, SortedSet, NavigableSet, TreeSet
// Compile: javac LessonConsole.java Lesson13j_SetHierarchy.java
// Run:     java Lesson13j_SetHierarchy
//
// After: Lesson 13i

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Lesson13j_SetHierarchy {

    public static void main(String[] args) {
        setContract();
        hashSetInternal();
        linkedHashSetDemo();
        sortedAndNavigableSet();
        treeSetInternal();
        summary();
    }

    static void setContract() {
        LessonConsole.heading("=== 0) Set interface — no duplicates ===");
        Set<String> set = new HashSet<>();
        set.add("java");
        set.add("oop");
        set.add("java"); // ignored
        System.out.println("  add java twice → " + set);
        System.out.println("  Set extends Collection — no get(i), no duplicate equals elements");
        System.out.println();
    }

    static void hashSetInternal() {
        LessonConsole.heading("=== 1) HashSet internal — HashMap in disguise ===");
        System.out.println("""
                HashSet internally uses HashMap<E, Object>:
                  element → stored as KEY
                  value   → dummy PRESENT sentinel object

                add(e)  → map.put(e, PRESENT)   O(1) avg
                contains→ map.containsKey(e)

                Same hashCode + equals rules as HashMap keys (Lesson 13c)
                """);
        HashSet<String> tags = new HashSet<>();
        tags.add("spring");
        tags.add("boot");
        System.out.println("  HashSet: " + tags);
        System.out.println();
    }

    static void linkedHashSetDemo() {
        LessonConsole.heading("=== 2) LinkedHashSet — HashSet + insert order ===");
        LinkedHashSet<String> ordered = new LinkedHashSet<>();
        ordered.add("Zebra");
        ordered.add("Apple");
        ordered.add("Mango");
        System.out.println("  put order Z,A,M → " + ordered + "  ✅ insertion order");
        System.out.println("  Internal: LinkedHashMap keys only (see 13d)");
        System.out.println();
    }

    static void sortedAndNavigableSet() {
        LessonConsole.heading("=== 3) SortedSet & NavigableSet ===");
        SortedSet<Integer> sorted = new TreeSet<>();
        sorted.add(30);
        sorted.add(10);
        sorted.add(20);
        System.out.println("  SortedSet (TreeSet): " + sorted + "  — natural order");
        System.out.println("  first=" + sorted.first() + "  last=" + sorted.last());
        System.out.println("  headSet(25)=" + sorted.headSet(25) + "  subSet(10,30)=" + sorted.subSet(10, 30));

        NavigableSet<Integer> nav = (NavigableSet<Integer>) sorted;
        System.out.println("  NavigableSet ceiling(15)=" + nav.ceiling(15)
                + "  floor(15)=" + nav.floor(15));
        System.out.println("  descendingSet: " + nav.descendingSet());
        System.out.println();
    }

    static void treeSetInternal() {
        LessonConsole.heading("=== 4) TreeSet internal — Red-Black tree ===");
        System.out.println("""
                TreeSet backed by TreeMap (keys = elements, dummy values)

                Balanced BST → add/contains/remove O(log n)
                Elements must be Comparable OR pass Comparator

                NavigableSet adds: ceiling, floor, pollFirst, descendingSet
                """);
        TreeSet<String> words = new TreeSet<>();
        words.add("dog");
        words.add("ant");
        words.add("cat");
        System.out.println("  TreeSet sorted: " + words);
        System.out.println();
    }

    static void summary() {
        LessonConsole.heading("=== Summary: Set hierarchy ===");
        System.out.println("""
                Set             → contract (unique)
                HashSet         → HashMap keys, O(1) avg, no order
                LinkedHashSet   → insert order
                SortedSet       → sorted iteration (subSet/headSet/tailSet)
                NavigableSet    → SortedSet + ceiling/floor/descending
                TreeSet         → Red-Black tree, O(log n)
                Next:           Lesson 13k Legacy (Hashtable, Vector, Stack)
                """);
    }
}
