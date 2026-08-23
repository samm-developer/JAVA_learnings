// Lesson 13i: ArrayList — deep dive + internal working
// Compile: javac LessonConsole.java Lesson13i_ArrayListDeepDive.java
// Run:     java Lesson13i_ArrayListDeepDive
//
// After: Lesson 13h | Also see 13b (memory diagram)

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Lesson13i_ArrayListDeepDive {

    public static void main(String[] args) {
        internalPicture();
        basicOps();
        problemMiddleInsert();
        growAndCapacity();
        vsLinkedList();
        summary();
    }

    static void internalPicture() {
        LessonConsole.heading("=== 0) Internal shape ===");
        System.out.println("""
                ArrayList on HEAP
                  ├── size          (elements in use)
                  └── elementData[] (Object array — capacity >= size)

                index 0..size-1 hold references to your objects

                get(i)     → O(1)  direct array[index]
                add(end)   → O(1) amortized (sometimes grow + copy)
                add(i,mid) → O(n)  shift right
                remove(i)  → O(n)  shift left
                """);
    }

    static void basicOps() {
        LessonConsole.heading("=== 1) Basic operations ===");
        ArrayList<String> list = new ArrayList<>();
        list.add("Apple");
        list.add("Banana");
        list.set(1, "Mango");
        list.remove("Apple");
        System.out.println("  list=" + list + " size=" + list.size());
        System.out.println();
    }

    static void problemMiddleInsert() {
        LessonConsole.heading("=== PROBLEM: insert/remove in middle ===");
        List<String> list = new ArrayList<>(List.of("A", "B", "C", "D", "E"));
        list.add(2, "X"); // shift C,D,E right
        System.out.println("  add(2,X): " + list + "  ❌ O(n) — copies references");
        list.remove(2);
        System.out.println("  remove(2): " + list);
        System.out.println();
    }

    static void growAndCapacity() {
        LessonConsole.heading("=== 2) Growth & capacity ===");
        System.out.println("""
                Default starts small → when full, new array ~1.5× size → copy refs

                new ArrayList<>(expectedSize)  → fewer resize copies
                trimToSize()                   → shrink array to size (save memory)
                """);
        ArrayList<Integer> nums = new ArrayList<>(100);
        for (int i = 0; i < 5; i++) {
            nums.add(i);
        }
        System.out.println("  capacity hint 100, size=" + nums.size() + " " + nums);
        System.out.println();
    }

    static void vsLinkedList() {
        LessonConsole.heading("=== 3) ArrayList vs LinkedList ===");
        System.out.println("""
                ArrayList   → array, cache-friendly, best default for List
                LinkedList  → Node chain, O(1) ends, O(n) get(i) — rarely needed

                Use ArrayList unless you have a specific reason for LinkedList.
                """);
        LinkedList<String> ll = new LinkedList<>();
        ll.add("only demo");
        System.out.println("  LinkedList demo: " + ll);
        System.out.println();
    }

    static void summary() {
        LessonConsole.heading("=== Summary: ArrayList ===");
        System.out.println("""
                Internal: Object[] + size + grow on demand
                Best for: indexed access, append-mostly workloads
                Iterator: fail-fast (see 13h)
                Next:      Lesson 13j Set hierarchy (HashSet, TreeSet, ...)
                """);
    }
}
