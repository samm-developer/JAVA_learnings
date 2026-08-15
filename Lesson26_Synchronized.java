// Lesson 26: Shared data problem + synchronized
// Compile: javac Lesson26_Synchronized.java
// Run:     java Lesson26_Synchronized

public class Lesson26_Synchronized {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== WITHOUT synchronized (may be WRONG) ===");
        runTest(false);

        System.out.println();
        System.out.println("=== WITH synchronized (should be 20000) ===");
        runTest(true);
    }

    static void runTest(boolean useLock) throws InterruptedException {
        Counter counter = new Counter(useLock);

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                counter.increment();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                counter.increment();
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Final count: " + counter.getValue());
        System.out.println("Expected:    20000");
    }
}

class Counter {
    private int value = 0;
    private final boolean useLock;

    Counter(boolean useLock) {
        this.useLock = useLock;
    }

    // Two threads may read/write value at the same time → lost updates
    void increment() {
        if (useLock) {
            synchronized (this) {
                value++;
            }
        } else {
            value++; // NOT thread-safe
        }
    }

    int getValue() {
        return value;
    }
}
