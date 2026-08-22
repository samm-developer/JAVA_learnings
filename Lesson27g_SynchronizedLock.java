// Lesson 27g: synchronized — Java's built-in lock (monitor)
// Compile: javac Lesson27g_SynchronizedLock.java
// Run:     java Lesson27g_SynchronizedLock
//
// After: Lesson 27f (What is a lock)
//
// synchronized = exclusive lock on an object (the "monitor")
// Re-entrant: same thread can enter synchronized again on same object

public class Lesson27g_SynchronizedLock {

    static final Object LOCK = new Object(); // lock object (monitor)
    static int balance = 100;

    public static void main(String[] args) throws InterruptedException {
        problemThenSolution();
        syncMethodVsBlock();
        reentrantDemo();
        summary();
    }

    static void problemThenSolution() throws InterruptedException {
        System.out.println("=== PROBLEM: no lock ===");
        balance = 100;
        runTwo(() -> {
            for (int i = 0; i < 10_000; i++) {
                balance++;
            }
        }, () -> {
            for (int i = 0; i < 10_000; i++) {
                balance--;
            }
        });
        System.out.println("  balance=" + balance + "  ❌ expected 100");

        System.out.println("=== SOLUTION: synchronized (exclusive lock) ===");
        balance = 100;
        runTwo(() -> {
            for (int i = 0; i < 10_000; i++) {
                synchronized (LOCK) {
                    balance++;
                }
            }
        }, () -> {
            for (int i = 0; i < 10_000; i++) {
                synchronized (LOCK) {
                    balance--;
                }
            }
        });
        System.out.println("  balance=" + balance + "  ✅");
        System.out.println();
    }

    static void syncMethodVsBlock() {
        System.out.println("=== synchronized method vs block ===");
        System.out.println("""
                // locks 'this'
                public synchronized void deposit(int n) { balance += n; }

                // locks LOCK object only for critical lines
                synchronized (LOCK) {
                    balance += n;
                }

                Prefer block when only a few lines need protection.
                """);
    }

    // Same thread can lock again (re-entrant)
    static void reentrantDemo() {
        System.out.println("=== Re-entrant: same thread can re-enter ===");
        synchronized (LOCK) {
            System.out.println("  outer sync");
            synchronized (LOCK) { // OK — same thread already owns LOCK
                System.out.println("  inner sync (re-entered) ✅");
            }
        }
        System.out.println();
    }

    static void summary() {
        System.out.println("=== Summary: synchronized ===");
        System.out.println("""
                Type:        exclusive lock (one thread at a time)
                Unlock:      automatic when leaving block/method
                tryLock?:    NO
                Interrupt?:  NO while waiting for monitor
                Next:        Lesson 27h ReentrantLock (more control)
                """);
    }

    static void runTwo(Runnable a, Runnable b) throws InterruptedException {
        Thread t1 = new Thread(a);
        Thread t2 = new Thread(b);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}
