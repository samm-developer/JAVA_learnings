// Lesson 27j: StampedLock — stamp + optimistic read
// Compile: javac Lesson27j_StampedLock.java
// Run:     java Lesson27j_StampedLock
//
// After: Lesson 27i (ReadWriteLock)
//
// StampedLock returns a long "stamp" for each lock mode:
//   writeLock()           → exclusive
//   readLock()            → shared (pessimistic)
//   tryOptimisticRead()   → NO lock; validate later (fast path)

import java.util.concurrent.locks.StampedLock;

public class Lesson27j_StampedLock {

    static final StampedLock lock = new StampedLock();
    static double x = 1.0;
    static double y = 2.0;

    public static void main(String[] args) throws InterruptedException {
        explain();
        optimisticReadDemo();
        writeDemo();
        summary();
    }

    static void explain() {
        System.out.println("=== StampedLock modes ===");
        System.out.println("""
                1) writeLock()          exclusive write
                2) readLock()           shared read (like ReadWriteLock)
                3) tryOptimisticRead()  assume no writer; if writer came → retry

                Optimistic read = lock-free style check (validate stamp)
                """);
    }

    static double distanceOptimistic() {
        long stamp = lock.tryOptimisticRead(); // no blocking
        double localX = x;
        double localY = y;
        if (!lock.validate(stamp)) {
            // a write happened — fall back to real read lock
            stamp = lock.readLock();
            try {
                localX = x;
                localY = y;
            } finally {
                lock.unlockRead(stamp);
            }
        }
        return Math.sqrt(localX * localX + localY * localY);
    }

    static void optimisticReadDemo() throws InterruptedException {
        System.out.println("=== Optimistic read while another thread writes ===");

        Thread writer = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                long stamp = lock.writeLock();
                try {
                    x += 1;
                    y += 1;
                    System.out.println("  WRITE x=" + x + " y=" + y);
                } finally {
                    lock.unlockWrite(stamp);
                }
                sleep(30);
            }
        }, "Writer");

        Thread reader = new Thread(() -> {
            for (int i = 0; i < 8; i++) {
                double d = distanceOptimistic();
                System.out.println("  READ  distance≈" + String.format("%.2f", d));
                sleep(20);
            }
        }, "Reader");

        writer.start();
        reader.start();
        writer.join();
        reader.join();
        System.out.println();
    }

    static void writeDemo() {
        System.out.println("=== Exclusive write with stamp ===");
        long stamp = lock.writeLock();
        try {
            x = 10;
            y = 20;
            System.out.println("  set x=10 y=20 under writeLock, stamp=" + stamp);
        } finally {
            lock.unlockWrite(stamp);
        }
        System.out.println("  distance now=" + String.format("%.2f", distanceOptimistic()));
        System.out.println();
    }

    static void summary() {
        System.out.println("=== Summary: StampedLock ===");
        System.out.println("""
                + optimistic reads (fast when writes rare)
                + shared + exclusive modes
                - NOT reentrant (don't lock again on same thread)
                - unlock with the correct stamp
                Next: Semaphore (Lesson 27k)
                """);
    }

    static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
