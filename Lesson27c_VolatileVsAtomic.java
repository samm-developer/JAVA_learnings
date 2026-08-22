// Lesson 27c: volatile vs Atomic — problem then solution
// Compile: javac Lesson27c_VolatileVsAtomic.java
// Run:     java Lesson27c_VolatileVsAtomic
//
// Place: after Lesson 27 (Atomic) + Lesson 25g (volatile).

import java.util.concurrent.atomic.AtomicInteger;

public class Lesson27c_VolatileVsAtomic {

    static final int TIMES = 100_000;

    public static void main(String[] args) throws InterruptedException {
        problemPlainInt();
        problemVolatileInt();
        solutionAtomicInteger();
        volatileWorksForFlag();
        summary();
    }

    // -------------------------------------------------------------------------
    // PROBLEM 1: plain int — race + visibility issues
    // -------------------------------------------------------------------------
    static int plainCount = 0;

    static void problemPlainInt() throws InterruptedException {
        System.out.println("=== PROBLEM 1: plain int (no protection) ===");
        plainCount = 0;

        Thread t1 = new Thread(() -> incrementPlain(), "T1");
        Thread t2 = new Thread(() -> incrementPlain(), "T2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("  Expected: " + (TIMES * 2));
        System.out.println("  Actual:   " + plainCount + "  ❌ WRONG (lost updates)");
        System.out.println("  Why: count++ is read → add → write; threads interleave");
        System.out.println();
    }

    static void incrementPlain() {
        for (int i = 0; i < TIMES; i++) {
            plainCount++; // NOT atomic
        }
    }

    // -------------------------------------------------------------------------
    // PROBLEM 2: volatile int — visibility OK, but count++ STILL not atomic
    // -------------------------------------------------------------------------
    static volatile int volatileCount = 0;

    static void problemVolatileInt() throws InterruptedException {
        System.out.println("=== PROBLEM 2: volatile int (visibility only) ===");
        volatileCount = 0;

        Thread t1 = new Thread(() -> incrementVolatile(), "T1");
        Thread t2 = new Thread(() -> incrementVolatile(), "T2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("  Expected: " + (TIMES * 2));
        System.out.println("  Actual:   " + volatileCount + "  ❌ STILL WRONG");
        System.out.println("  volatile makes writes visible, but does NOT fix count++");
        System.out.println();
    }

    static void incrementVolatile() {
        for (int i = 0; i < TIMES; i++) {
            volatileCount++; // looks like one line, still 3 steps at CPU level
        }
    }

    // -------------------------------------------------------------------------
    // SOLUTION: AtomicInteger — visibility + atomic increment
    // -------------------------------------------------------------------------
    static AtomicInteger atomicCount = new AtomicInteger(0);

    static void solutionAtomicInteger() throws InterruptedException {
        System.out.println("=== SOLUTION: AtomicInteger ===");
        atomicCount.set(0);

        Thread t1 = new Thread(() -> incrementAtomic(), "T1");
        Thread t2 = new Thread(() -> incrementAtomic(), "T2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("  Expected: " + (TIMES * 2));
        System.out.println("  Actual:   " + atomicCount.get() + "  ✅ CORRECT");
        System.out.println("  incrementAndGet() = one atomic read-modify-write");
        System.out.println();
    }

    static void incrementAtomic() {
        for (int i = 0; i < TIMES; i++) {
            atomicCount.incrementAndGet();
        }
    }

    // -------------------------------------------------------------------------
    // WHERE volatile IS the right solution: simple flag (single write, single read)
    // -------------------------------------------------------------------------
    static volatile boolean running = true;

    static void volatileWorksForFlag() throws InterruptedException {
        System.out.println("=== volatile WORKS HERE: stop flag ===");
        running = true;

        Thread worker = new Thread(() -> {
            while (running) {
                // busy loop — waiting for main to set running = false
            }
            System.out.println("  Worker stopped (saw running=false)");
        }, "Worker");

        worker.start();
        Thread.sleep(50);
        running = false; // single write — volatile ensures worker sees it
        worker.join();

        System.out.println("  ✅ volatile is perfect for boolean/status flags");
        System.out.println();
    }

    static void summary() {
        System.out.println("=== Summary ===");
        System.out.println("""
                plain int       → race on count++                    ❌
                volatile int    → visible, but count++ still racy    ❌ for counters
                AtomicInteger   → safe increment / CAS               ✅ for counters
                volatile boolean→ safe stop/running flag              ✅ for flags
                """);
    }
}
