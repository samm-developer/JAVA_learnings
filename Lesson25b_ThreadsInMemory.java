// Lesson 25b: Threads BTS in memory (stack per thread, shared heap)
// Compile: javac Lesson25b_ThreadsInMemory.java
// Run:     java Lesson25b_ThreadsInMemory
//
// Place: after Lesson 25 (Threads) + Lesson 36c (JVM heap/stack).
//
// Big picture:
//   Each thread  → its OWN stack (local vars, method calls)
//   All threads  → share ONE heap (objects created with new)
//
// That sharing is why synchronized / atomic matter (Lessons 26–27).

public class Lesson25b_ThreadsInMemory {

    // HEAP: one Counter object, shared by all threads
    static class Counter {
        int value = 0; // on HEAP inside Counter object

        void increment() {
            value++; // NOT atomic: read → add → write (3 steps)
        }
    }

    public static void main(String[] args) throws InterruptedException {
        memoryDiagram();
        eachThreadHasOwnStack();
        heapIsShared();
        raceConditionDemo();
        threadObjectOnHeap();
    }

    static void memoryDiagram() {
        System.out.println("=== 0) Thread memory model (simplified) ===");
        System.out.println("""
                Process (your JVM)
                ┌─────────────────────────────────────────────────────────┐
                │  HEAP (shared by ALL threads)                           │
                │    Counter object { value=0 }                           │
                │    String "hello"                                       │
                │    Thread objects                                       │
                └─────────────────────────────────────────────────────────┘
                ┌──────────────┐  ┌──────────────┐  ┌──────────────┐
                │ Thread main  │  │ Thread A     │  │ Thread B     │
                │ STACK        │  │ STACK        │  │ STACK        │
                │  args        │  │  local i     │  │  local i     │
                │  local vars  │  │  run() frame │  │  run() frame │
                └──────────────┘  └──────────────┘  └──────────────┘

                Rule: locals live on THAT thread's stack.
                      Objects live on heap → visible to every thread.
                """);
    }

    static void eachThreadHasOwnStack() throws InterruptedException {
        System.out.println("=== 1) Each thread has its own stack (local vars) ===");

        Thread t = new Thread(() -> {
            int local = 100; // on Worker stack only — main cannot see this
            System.out.println("  Worker stack → local=" + local
                    + "  thread=" + Thread.currentThread().getName());
        }, "Worker");

        int mainLocal = 42; // on main stack only
        System.out.println("  Main stack   → mainLocal=" + mainLocal
                + "  thread=" + Thread.currentThread().getName());

        t.start();
        t.join();
        System.out.println();
    }

    static void heapIsShared() throws InterruptedException {
        System.out.println("=== 2) Heap is shared (same object, two threads) ===");

        StringBuilder shared = new StringBuilder("start"); // HEAP

        Thread a = new Thread(() -> {
            shared.append("-A"); // both threads touch SAME heap object
            System.out.println("  Thread-A sees: " + shared
                    + "  on " + Thread.currentThread().getName());
        }, "Thread-A");

        Thread b = new Thread(() -> {
            shared.append("-B");
            System.out.println("  Thread-B sees: " + shared
                    + "  on " + Thread.currentThread().getName());
        }, "Thread-B");

        System.out.println("  StringBuilder id (heap): " + System.identityHashCode(shared));
        a.start();
        b.start();
        a.join();
        b.join();
        System.out.println("  Final (main reads same heap object): " + shared);
        System.out.println("  (Order of -A / -B may vary — scheduling is not guaranteed)");
        System.out.println();
    }

    static void raceConditionDemo() throws InterruptedException {
        System.out.println("=== 3) Race condition — shared heap field, no sync ===");

        Counter counter = new Counter(); // HEAP — one object, two threads
        final int TIMES = 100_000;

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < TIMES; i++) {
                counter.increment(); // both read/write counter.value on HEAP
            }
        }, "Inc-A");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < TIMES; i++) {
                counter.increment();
            }
        }, "Inc-B");

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("  Expected: " + (TIMES * 2));
        System.out.println("  Actual:   " + counter.value);
        System.out.println("  Why wrong? Both threads interleave read/add/write on same heap field.");
        System.out.println("  Fix → Lesson 26 synchronized / Lesson 27 AtomicInteger");
        System.out.println();
    }

    static void threadObjectOnHeap() throws InterruptedException {
        System.out.println("=== 4) Thread object itself lives on HEAP ===");

        Thread worker = new Thread(() -> {
            System.out.println("  Running on: " + Thread.currentThread().getName());
            System.out.println("  Thread id:  " + Thread.currentThread().threadId());
        }, "Worker");

        // 'worker' reference on main STACK → Thread object on HEAP
        System.out.println("  worker ref on main stack → Thread object on heap");
        System.out.println("  Thread object hash: " + System.identityHashCode(worker));
        System.out.println("  State before start: " + worker.getState()); // NEW

        worker.start();
        worker.join();
        System.out.println("  State after join:   " + worker.getState()); // TERMINATED
        System.out.println();
        System.out.println("Summary:");
        System.out.println("  Stack  = per thread, private locals");
        System.out.println("  Heap   = shared objects → need sync when mutating");
    }
}
