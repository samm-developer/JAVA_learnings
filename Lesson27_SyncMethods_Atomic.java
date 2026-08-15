// Lesson 27: synchronized methods + AtomicInteger
// Compile: javac Lesson27_SyncMethods_Atomic.java
// Run:     java Lesson27_SyncMethods_Atomic

import java.util.concurrent.atomic.AtomicInteger;

public class Lesson27_SyncMethods_Atomic {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 1) synchronized method ===");
        runSyncMethod();

        System.out.println();
        System.out.println("=== 2) AtomicInteger (no synchronized needed) ===");
        runAtomic();
    }

    static void runSyncMethod() throws InterruptedException {
        SyncCounter counter = new SyncCounter();
        final int TIMES = 100_000;

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < TIMES; i++) counter.increment();
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < TIMES; i++) counter.increment();
        });

        long start = System.nanoTime();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        double ms = (System.nanoTime() - start) / 1_000_000.0;

        System.out.println("Final: " + counter.getValue() + " (expected " + (TIMES * 2) + ")");
        System.out.println("Time:  " + ms + " ms");
    }

    static void runAtomic() throws InterruptedException {
        AtomicCounter counter = new AtomicCounter();
        final int TIMES = 100_000;

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < TIMES; i++) counter.increment();
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < TIMES; i++) counter.increment();
        });

        long start = System.nanoTime();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        double ms = (System.nanoTime() - start) / 1_000_000.0;

        System.out.println("Final: " + counter.getValue() + " (expected " + (TIMES * 2) + ")");
        System.out.println("Time:  " + ms + " ms");
    }
}

// Style A: whole method is synchronized (same idea as synchronized(this) { ... })
class SyncCounter {
    private int value = 0;

    // Only one thread at a time can run this method on the same object
    public synchronized void increment() {
        value++;
    }

    public synchronized int getValue() {
        return value;
    }
}

// Style B: AtomicInteger — thread-safe counter built into Java
class AtomicCounter {
    private final AtomicInteger value = new AtomicInteger(0);

    public void increment() {
        value.incrementAndGet(); // atomic: safe without synchronized
    }

    public int getValue() {
        return value.get();
    }
}
