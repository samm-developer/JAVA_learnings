// Lesson 13e: ConcurrentHashMap — thread-safe map + internal working
// Compile: javac LessonConsole.java Lesson13e_ConcurrentHashMap.java
// Run:     java Lesson13e_ConcurrentHashMap
//
// After: Lesson 13c (HashMap), Lesson 27e (thread-safe overview)
//
// ========== CHEAT SHEET ==========
// Problem:  HashMap + multiple writer threads → corrupt size/buckets (undefined behavior)
// Solution: ConcurrentHashMap — fine-grained locking / CAS per bucket (not one global lock)
// Java 8+:  synchronized on first node of bin + CAS for empty bin insert
// Extra API: putIfAbsent, compute, merge — atomic at key level
// Never:     null keys or null values

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Lesson13e_ConcurrentHashMap {

    static final int TIMES = 20_000;

    public static void main(String[] args) throws InterruptedException {
        internalPicture();
        problemHashMap();
        solutionConcurrentHashMap();
        atomicOps();
        vsHashtable();
        summary();
    }

    static void internalPicture() {
        LessonConsole.heading("=== 0) Internal idea (Java 8+ simplified) ===");
        System.out.println("""
                ConcurrentHashMap
                  └── table[] buckets (like HashMap)

                Thread 1 put → bin 3   (lock/CAS only bin 3)
                Thread 2 put → bin 7   (bin 7 in parallel)  ✅

                Old Hashtable: ONE lock on entire table → all threads wait
                HashMap:       NO lock → races corrupt map

                CHM: lock/CAS per bucket → safe + scalable reads/writes
                """);
    }

    static void problemHashMap() throws InterruptedException {
        LessonConsole.heading("=== PROBLEM: HashMap with 2 writer threads ===");
        Map<Long, Integer> unsafe = new HashMap<>();
        runTwoWriters(() -> {
            long base = Thread.currentThread().threadId() * TIMES;
            for (int i = 0; i < TIMES; i++) {
                unsafe.put(base + i, i); // unique keys per thread
            }
        });
        System.out.println("  HashMap size=" + unsafe.size()
                + "  expected=" + (TIMES * 2) + "  ❌ often wrong / lost entries");
        System.out.println();
    }

    static void solutionConcurrentHashMap() throws InterruptedException {
        LessonConsole.heading("=== SOLUTION: ConcurrentHashMap ===");
        Map<Long, Integer> safe = new ConcurrentHashMap<>();
        runTwoWriters(() -> {
            long base = Thread.currentThread().threadId() * TIMES;
            for (int i = 0; i < TIMES; i++) {
                safe.put(base + i, i);
            }
        });
        System.out.println("  ConcurrentHashMap size=" + safe.size()
                + "  expected=" + (TIMES * 2) + "  ✅");
        System.out.println();
    }

    static void atomicOps() {
        LessonConsole.heading("=== 2) Atomic key-level operations ===");
        ConcurrentHashMap<String, Integer> counts = new ConcurrentHashMap<>();

        // only first thread wins for key "visits"
        counts.putIfAbsent("visits", 0);
        counts.merge("visits", 1, Integer::sum);
        counts.merge("visits", 1, Integer::sum);
        System.out.println("  putIfAbsent + merge visits → " + counts.get("visits"));

        counts.compute("score", (k, v) -> v == null ? 10 : v + 5);
        counts.compute("score", (k, v) -> v == null ? 10 : v + 5);
        System.out.println("  compute score twice      → " + counts.get("score"));
        System.out.println();
    }

    static void vsHashtable() {
        LessonConsole.heading("=== 3) ConcurrentHashMap vs Hashtable ===");
        System.out.println("""
                Hashtable (legacy):     synchronized on EVERY operation — slow under contention
                ConcurrentHashMap:      bucket-level — preferred for multi-thread maps
                Collections.syncMap:    wraps HashMap — one lock, rarely best choice

                All three reject null keys (CHM + Hashtable); HashMap allows one null key
                """);
    }

    static void summary() {
        LessonConsole.heading("=== Summary: ConcurrentHashMap ===");
        System.out.println("""
                Use when:     many threads read/write same map
                Internal:     buckets + per-bin lock/CAS (not whole-map lock)
                Also see:     Lesson 27e ThreadSafeVersions
                Next:         Lesson 13f PriorityQueue
                """);
    }

    static void runTwoWriters(Runnable writer) throws InterruptedException {
        Thread t1 = new Thread(writer);
        Thread t2 = new Thread(writer);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}
