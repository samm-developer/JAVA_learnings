// Lesson 13k: Legacy collections — Hashtable, Vector, Stack (and modern replacements)
// Compile: javac LessonConsole.java Lesson13k_LegacyCollections.java
// Run:     java Lesson13k_LegacyCollections
//
// After: Lesson 13j

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class Lesson13k_LegacyCollections {

    public static void main(String[] args) {
        overview();
        problemHashtable();
        solutionConcurrentHashMap();
        problemVector();
        solutionArrayList();
        problemStack();
        solutionArrayDeque();
        summary();
    }

    static void overview() {
        LessonConsole.heading("=== 0) Legacy vs modern ===");
        System.out.println("""
                Legacy (Java 1.0–1.1)     Modern replacement
                Hashtable                 ConcurrentHashMap / HashMap
                Vector                    ArrayList / CopyOnWriteArrayList
                Stack extends Vector      ArrayDeque
                """);
    }

    static void problemHashtable() {
        LessonConsole.heading("=== PROBLEM: Hashtable — synchronized on EVERY call ===");
        System.out.println("""
                Hashtable extends Dictionary (pre-Collections)
                  • every get/put locks whole table → slow under threads
                  • no null keys/values
                  • legacy API (Enumeration)
                """);
        Hashtable<String, Integer> table = new Hashtable<>();
        table.put("Asha", 90);
        System.out.println("  Hashtable demo: " + table + "  (works but avoid for new code)");
        System.out.println();
    }

    static void solutionConcurrentHashMap() {
        LessonConsole.heading("=== SOLUTION: ConcurrentHashMap (13e) or HashMap (single thread) ===");
        ConcurrentHashMap<String, Integer> chm = new ConcurrentHashMap<>();
        chm.put("Asha", 90);
        System.out.println("  multi-thread map → ConcurrentHashMap " + chm);
        System.out.println("  single-thread    → HashMap (13c)");
        System.out.println();
    }

    static void problemVector() {
        LessonConsole.heading("=== PROBLEM: Vector — synchronized ArrayList ===");
        System.out.println("""
                Vector = growable array + synchronized methods
                  • one thread at a time for most ops
                  • ArrayList + Collections.synchronizedList is clearer if you need sync
                """);
        Vector<String> v = new Vector<>();
        v.add("legacy");
        System.out.println("  Vector: " + v);
        System.out.println();
    }

    static void solutionArrayList() {
        LessonConsole.heading("=== SOLUTION: ArrayList (13i) ===");
        ArrayList<String> list = new ArrayList<>();
        list.add("modern");
        System.out.println("  ArrayList: " + list + "  ✅ default choice for List");
        System.out.println();
    }

    static void problemStack() {
        LessonConsole.heading("=== PROBLEM: Stack extends Vector (old design) ===");
        Stack<String> stack = new Stack<>();
        stack.push("A");
        stack.push("B");
        System.out.println("  Stack pop=" + stack.pop() + "  works but inherits Vector baggage");
        System.out.println();
    }

    static void solutionArrayDeque() {
        LessonConsole.heading("=== SOLUTION: ArrayDeque as stack/queue (13l) ===");
        Deque<String> deque = new ArrayDeque<>();
        deque.push("A");
        deque.push("B");
        System.out.println("  ArrayDeque pop=" + deque.pop() + "  ✅ preferred stack");
        System.out.println();
    }

    static void summary() {
        LessonConsole.heading("=== Summary: Legacy collections ===");
        System.out.println("""
                Avoid for new code: Hashtable, Vector, Stack
                Use instead:        HashMap/CHM, ArrayList, ArrayDeque
                Next:              Lesson 13l Deque & ArrayDeque deep dive
                """);
    }
}
