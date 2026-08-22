// Lesson 27e: Thread-safe versions — problem then solution
// Compile: javac Lesson27e_ThreadSafeVersions.java
// Run:     java Lesson27e_ThreadSafeVersions
//
// Place: after 27c/27d (volatile + Atomic).
// "Thread-safe" = many threads can use it without corrupting data / wrong results.
//
// ========== CHEAT SHEET (unsafe → thread-safe version) ==========
//
//   ArrayList           → Collections.synchronizedList(...)
//                         or CopyOnWriteArrayList
//   HashMap             → ConcurrentHashMap
//   StringBuilder       → StringBuffer
//   int count++         → AtomicInteger              (Lesson 27c)
//   plain boolean flag  → volatile boolean           (Lesson 27d — visibility)
//
// ================================================================

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Lesson27e_ThreadSafeVersions {

    static final int TIMES = 50_000;

    public static void main(String[] args) throws InterruptedException {
        overview();
        listNotSafeThenSafe();
        mapNotSafeThenSafe();
        stringBuilderVsBuffer();
        counterReminder();
        cheatSheet();
    }

    static void overview() {
        System.out.println("=== 0) What \"thread-safe version\" means ===");
        System.out.println("""
                NOT thread-safe: ArrayList, HashMap, StringBuilder, plain int
                Thread-safe:     synchronized wrappers, Concurrent*, Atomic*, StringBuffer

                Rule: if 2+ threads WRITE the same object → need a thread-safe version
                      (or synchronize yourself)
                """);
    }

    // -------------------------------------------------------------------------
    // LIST: ArrayList (unsafe) → Collections.synchronizedList / CopyOnWriteArrayList
    // -------------------------------------------------------------------------
    static void listNotSafeThenSafe() throws InterruptedException {
        System.out.println("=== 1) List ===");

        // PROBLEM
        List<Integer> unsafe = new ArrayList<>();
        runTwoWriters(() -> {
            for (int i = 0; i < TIMES; i++) {
                unsafe.add(i); // ❌ race — size/array can corrupt
            }
        });
        System.out.println("  PROBLEM  ArrayList size: " + unsafe.size()
                + "  (expected " + (TIMES * 2) + ") ❌ often wrong");

        // SOLUTION A — synchronized wrapper (locks whole list on each call)
        List<Integer> syncList = Collections.synchronizedList(new ArrayList<>());
        runTwoWriters(() -> {
            for (int i = 0; i < TIMES; i++) {
                syncList.add(i);
            }
        });
        System.out.println("  SOLUTION Collections.synchronizedList size: " + syncList.size()
                + "  ✅");

        // SOLUTION B — concurrent list (good for many readers, few writers)
        List<Integer> cow = new CopyOnWriteArrayList<>();
        runTwoWriters(() -> {
            for (int i = 0; i < 5_000; i++) { // smaller — COW is expensive on write
                cow.add(i);
            }
        });
        System.out.println("  SOLUTION CopyOnWriteArrayList size: " + cow.size() + "  ✅");
        System.out.println();
    }

    // -------------------------------------------------------------------------
    // MAP: HashMap (unsafe) → ConcurrentHashMap
    // -------------------------------------------------------------------------
    static void mapNotSafeThenSafe() throws InterruptedException {
        System.out.println("=== 2) Map ===");

        // PROBLEM — HashMap not thread-safe (can hang or lose entries in theory)
        Map<Integer, Integer> unsafe = new HashMap<>();
        runTwoWriters(() -> {
            for (int i = 0; i < TIMES; i++) {
                unsafe.put(i, i);
            }
        });
        System.out.println("  PROBLEM  HashMap size: " + unsafe.size()
                + "  (expected up to " + TIMES + " unique keys from 2 threads) ❌ often wrong");

        // SOLUTION
        Map<Integer, Integer> safe = new ConcurrentHashMap<>();
        runTwoWriters(() -> {
            for (int i = 0; i < TIMES; i++) {
                safe.put(i, i);
            }
        });
        System.out.println("  SOLUTION ConcurrentHashMap size: " + safe.size() + "  ✅");
        System.out.println();
    }

    // -------------------------------------------------------------------------
    // STRING: StringBuilder (unsafe) → StringBuffer (thread-safe, older)
    // -------------------------------------------------------------------------
    static void stringBuilderVsBuffer() throws InterruptedException {
        System.out.println("=== 3) StringBuilder vs StringBuffer ===");

        StringBuilder sb = new StringBuilder();
        runTwoWriters(() -> {
            for (int i = 0; i < 10_000; i++) {
                sb.append('a'); // ❌ not thread-safe
            }
        });
        System.out.println("  PROBLEM  StringBuilder length: " + sb.length()
                + "  (expected 20000) ❌ often wrong");

        StringBuffer sbuf = new StringBuffer();
        runTwoWriters(() -> {
            for (int i = 0; i < 10_000; i++) {
                sbuf.append('a'); // ✅ synchronized methods
            }
        });
        System.out.println("  SOLUTION StringBuffer length: " + sbuf.length() + "  ✅");
        System.out.println("  (Modern code: prefer StringBuilder + sync yourself, or build per-thread)");
        System.out.println();
    }

    // -------------------------------------------------------------------------
    // COUNTER reminder (from 27c)
    // -------------------------------------------------------------------------
    static void counterReminder() throws InterruptedException {
        System.out.println("=== 4) Counter (reminder from 27c) ===");

        AtomicInteger count = new AtomicInteger(0);
        runTwoWriters(() -> {
            for (int i = 0; i < TIMES; i++) {
                count.incrementAndGet();
            }
        });
        System.out.println("  SOLUTION AtomicInteger: " + count.get()
                + "  (expected " + (TIMES * 2) + ") ✅");
        System.out.println();
    }

    static void cheatSheet() {
        System.out.println("=== Cheat sheet: unsafe → thread-safe version ===");
        System.out.println("""
                ArrayList          → Collections.synchronizedList(...)
                                   → or CopyOnWriteArrayList (read-heavy)
                HashMap            → ConcurrentHashMap
                HashSet            → ConcurrentHashMap.newKeySet()
                StringBuilder      → StringBuffer (or sync / one thread only)
                int count++        → AtomicInteger
                boolean flag       → volatile boolean (visibility only — Lesson 27d)

                Also thread-safe by design:
                  Vector, Hashtable (old — prefer ConcurrentHashMap)
                """);
    }

    /** Run the same Runnable on 2 threads and wait for both. */
    static void runTwoWriters(Runnable work) throws InterruptedException {
        Thread t1 = new Thread(work);
        Thread t2 = new Thread(work);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}
