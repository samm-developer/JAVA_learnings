// Lesson 27i: ReadWriteLock — SHARED (read) + EXCLUSIVE (write)
// Compile: javac Lesson27i_ReadWriteLock.java
// Run:     java Lesson27i_ReadWriteLock
//
// After: Lesson 27h (ReentrantLock)
//
// Many readers together  = shared lock   (readLock)
// One writer alone       = exclusive lock (writeLock)
// Writer blocks readers; readers block writer

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Lesson27i_ReadWriteLock {

    static final ReadWriteLock rw = new ReentrantReadWriteLock();
    static String data = "v1";

    public static void main(String[] args) throws InterruptedException {
        explain();
        manyReadersOneWriter();
        summary();
    }

    static void explain() {
        System.out.println("=== Shared vs Exclusive (this lesson) ===");
        System.out.println("""
                readLock()  → SHARED    → many threads can hold at once
                writeLock() → EXCLUSIVE → only one thread; no readers allowed

                Good when: lots of reads, rare writes
                Bad when:  writes are frequent (writers starve / little gain)
                """);
    }

    static void manyReadersOneWriter() throws InterruptedException {
        System.out.println("=== Demo: 3 readers + 1 writer ===");

        Runnable reader = () -> {
            for (int i = 0; i < 3; i++) {
                rw.readLock().lock();
                try {
                    System.out.println("  READ  " + Thread.currentThread().getName()
                            + " sees " + data);
                    sleep(80);
                } finally {
                    rw.readLock().unlock();
                }
                sleep(20);
            }
        };

        Thread w = new Thread(() -> {
            sleep(50);
            rw.writeLock().lock();
            try {
                data = "v2";
                System.out.println("  WRITE " + Thread.currentThread().getName()
                        + " set data=" + data + " (exclusive)");
                sleep(100);
            } finally {
                rw.writeLock().unlock();
            }
        }, "Writer");

        Thread r1 = new Thread(reader, "R1");
        Thread r2 = new Thread(reader, "R2");
        Thread r3 = new Thread(reader, "R3");

        r1.start();
        r2.start();
        r3.start();
        w.start();

        r1.join();
        r2.join();
        r3.join();
        w.join();
        System.out.println("  Final data=" + data);
        System.out.println();
    }

    static void summary() {
        System.out.println("=== Summary: ReadWriteLock ===");
        System.out.println("""
                Shared lock:     readLock()
                Exclusive lock:  writeLock()
                Both reentrant:  ReentrantReadWriteLock
                Next:            StampedLock (optimistic read — Lesson 27j)
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
