// Lesson 13h: Iterable, Iterator & ListIterator
// Compile: javac LessonConsole.java Lesson13h_IterableIterator.java
// Run:     java Lesson13h_IterableIterator
//
// After: Lesson 13g
// Study order: 13h Iterable/Iterator → 13i ArrayList → 13j Sets → ...
//
// ========== CHEAT SHEET ==========
// Iterable  → for-each works (iterator() method)
// Iterator  → hasNext/next/remove — forward only, all collections
// ListIterator → previous/add/set — lists only, bidirectional

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class Lesson13h_IterableIterator {

    public static void main(String[] args) {
        iterableBasics();
        iteratorBasics();
        problemConcurrentModification();
        solutionListIterator();
        internalWorking();
        summary();
    }

    static void iterableBasics() {
        LessonConsole.heading("=== 0) Iterable — enables for-each ===");
        List<String> list = new ArrayList<>(List.of("A", "B", "C"));
        System.out.println("""
                for (String s : list)  →  compiler calls list.iterator()

                Iterable<T>
                  └── iterator() → Iterator<T>
                """);
        for (String s : list) {
            System.out.println("  " + s);
        }
        System.out.println();
    }

    static void iteratorBasics() {
        LessonConsole.heading("=== 1) Iterator — forward traversal ===");
        List<String> list = new ArrayList<>(List.of("java", "oop", "spring"));
        Iterator<String> it = list.iterator();
        System.out.print("  forward: ");
        while (it.hasNext()) {
            System.out.print(it.next() + " ");
        }
        System.out.println();
        System.out.println("  API: hasNext() | next() | remove() (optional — removes via iterator)");
        System.out.println();
    }

    static void problemConcurrentModification() {
        LessonConsole.heading("=== PROBLEM: modify list during for-each ===");
        List<String> list = new ArrayList<>(List.of("A", "B", "C", "D"));
        try {
            for (String s : list) {
                if ("B".equals(s)) {
                    list.remove(s); // structural change while iterating
                }
            }
        } catch (Exception e) {
            System.out.println("  ❌ " + e.getClass().getSimpleName()
                    + " — iterator didn't expect list.remove() mid-loop");
        }
        System.out.println();
    }

    static void solutionListIterator() {
        LessonConsole.heading("=== SOLUTION: Iterator.remove() or ListIterator ===");
        List<String> list = new ArrayList<>(List.of("A", "B", "C", "D"));

        // Safe remove with Iterator
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            if ("B".equals(it.next())) {
                it.remove(); // ✅ iterator tracks change
            }
        }
        System.out.println("  after Iterator.remove(B): " + list);

        // ListIterator — backward + add/set at cursor
        ListIterator<String> lit = list.listIterator();
        while (lit.hasNext()) {
            String s = lit.next();
            if ("C".equals(s)) {
                lit.set("C-updated");
            }
        }
        lit.add("E"); // insert at end via cursor
        System.out.println("  after ListIterator set/add: " + list);

        System.out.print("  backward: ");
        while (lit.hasPrevious()) {
            System.out.print(lit.previous() + " ");
        }
        System.out.println("  ✅");
        System.out.println();
    }

    static void internalWorking() {
        LessonConsole.heading("=== 2) Internal: fail-fast iterator (ArrayList) ===");
        System.out.println("""
                ArrayList keeps modCount (changes on add/remove).

                Iterator stores expectedModCount at creation.
                  loop: if list.modCount != expectedModCount
                          → ConcurrentModificationException

                Use iterator.remove() / ListIterator mutators — they update modCount correctly.
                """);
    }

    static void summary() {
        LessonConsole.heading("=== Summary: Iterable / Iterator / ListIterator ===");
        System.out.println("""
                Iterable       → for-each
                Iterator       → any Collection, forward, remove()
                ListIterator   → List only, previous(), add(), set()
                Next:          Lesson 13i ArrayList deep dive
                """);
    }
}
