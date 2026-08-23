// Lesson 25m: ThreadPoolExecutor — core (min) vs max pool size
// Compile: javac LessonConsole.java Lesson25m_ThreadPoolExecutor.java
// Run:     java Lesson25m_ThreadPoolExecutor
//
// After: Lesson 25d (Thread pool), 25e (pool types)
//
// ========== CHEAT SHEET ==========
// ThreadPoolExecutor(core, max, keepAlive, unit, queue)
//   corePoolSize     = min workers kept ready (create first)
//   maximumPoolSize  = absolute ceiling
// Flow:  create up to core → then QUEUE → then create up to max → then REJECT
// Factories: newFixedThreadPool(n) = core=max=n (no growth past n)

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Lesson25m_ThreadPoolExecutor {

    public static void main(String[] args) throws Exception {
        // concept();
        // problemFactoryHidesMinMax();
        eightTasksDemo();           // core=2, max=4, queue=2 — main demo
        // summary();
    }

    static void concept() {
        LessonConsole.heading("=== 0) core (min) vs max ===");
        System.out.println("""
                corePoolSize (min):  threads created first; kept ready for work
                maximumPoolSize:     hard limit — never more than this many workers

                New task:
                  1) workers < core     → create thread
                  2) workers == core    → put in QUEUE
                  3) queue FULL + < max → create EXTRA thread (up to max)
                  4) queue FULL + == max → REJECT
                """);
    }

    static void problemFactoryHidesMinMax() {
        LessonConsole.heading("=== PROBLEM: factory hides core/max ===");
        System.out.println("""
                Executors.newFixedThreadPool(2)
                  → under the hood ThreadPoolExecutor with core=2, max=2
                  → you cannot grow past 2 when queue is busy

                Need control? Use ThreadPoolExecutor constructor yourself.
                """);
    }

    static void eightTasksDemo() throws InterruptedException {
        LessonConsole.heading("=== DEMO: 8 tasks — core=2, max=4, queue=2 ===");
        // System.out.println("""
        //           Capacity before reject:
        //             2 core workers + 2 queued + 2 extra (grow to max=4) = 6 tasks OK
        //             task 7 & 8 → REJECT (max workers busy + queue full)

        //           Submit loop:
        //             T1,T2 → create core threads (poolSize 1→2)
        //             T3,T4 → queue fills (queued 1→2)
        //             T5,T6 → create extra threads (poolSize 3→4)
        //             T7,T8 → ❌ RejectedExecutionException
        //         """);

        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                2,                          // min (core)
                4,                          // max
                30, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(2)   // queue size 2
        );

        AtomicInteger accepted = new AtomicInteger();
        AtomicInteger rejected = new AtomicInteger();

        for (int i = 1; i <= 8; i++) {
            final int n = i;
            try {
                pool.execute(() -> {
                    accepted.incrementAndGet();
                    System.out.println("    [task-" + n + "] start on " + Thread.currentThread().getName());
                    sleep(800);
                    System.out.println("    [task-" + n + "] done");
                });
                System.out.println("  submit task-" + n + " OK"
                        + "  poolSize=" + pool.getPoolSize()
                        + " active=" + pool.getActiveCount()
                        + " queued=" + pool.getQueue().size());
            } catch (RejectedExecutionException e) {
                rejected.incrementAndGet();
                System.out.println("  submit task-" + n + " ❌ REJECTED");
            }
            sleep(30); // small gap so you can read each line
        }

        pool.shutdown();
        pool.awaitTermination(15, TimeUnit.SECONDS);
        System.out.println("  accepted=" + accepted.get() + " rejected=" + rejected.get() + "  ✅");
        System.out.println();
    }

    static void summary() {
        LessonConsole.heading("=== Summary: ThreadPoolExecutor ===");
        System.out.println("""
                core (min)  → create first; keep warm
                max         → never exceed
                queue       → absorb burst BEFORE creating extra threads
                reject      → when max workers busy AND queue full

                newFixedThreadPool(n)  ≈ ThreadPoolExecutor(n, n, ..., unbounded queue)
                See also: 25d ExecutorService factories, 25e pool types
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
