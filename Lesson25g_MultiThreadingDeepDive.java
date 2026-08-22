// Lesson 25g: Multi-threading DEEP DIVE (ties Lessons 25–27 + pools together)
// Compile: javac Lesson25g_MultiThreadingDeepDive.java
// Run:     java Lesson25g_MultiThreadingDeepDive
//
// Study path:
//   25  basics → 25b memory → 25c ThreadLocal → 25d/e/f pools
//   26  synchronized → 27 atomic → 27b CompletableFuture
//   25g this file = big picture + lifecycle + volatile + wait/notify + deadlock

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class Lesson25g_MultiThreadingDeepDive {

    public static void main(String[] args) throws Exception {
        roadmap();
        threadLifecycle();
        visibilityProblemAndVolatile();
        synchronizedRecap();
        waitNotifyProducerConsumer();
        deadlockDemo();
        countDownLatchDemo();
        decisionGuide();
    }

    // =========================================================================
    static void roadmap() {
        System.out.println("=== 0) Your multi-threading roadmap ===");
        System.out.println("""
                CREATE          MEMORY           COORDINATE           POOLS
                ──────          ──────           ──────────           ─────
                25 Thread       25b stack/heap   26 synchronized      25d ExecutorService
                start/join      25c ThreadLocal  27 Atomic*           25e cached/single/scheduled
                                                27b CompletableFuture 25f ForkJoinPool

                Problems threads cause:
                  1) Race      → lost updates (counter)
                  2) Visibility→ one thread doesn't see another's write
                  3) Deadlock  → A waits B, B waits A
                """);
    }

    // =========================================================================
    // 1) Thread lifecycle
    // =========================================================================
    static void threadLifecycle() throws InterruptedException {
        System.out.println("=== 1) Thread lifecycle ===");
        System.out.println("  NEW → RUNNABLE → (BLOCKED/WAITING/TIMED_WAITING) → TERMINATED");

        Thread t = new Thread(() -> {
            System.out.println("  RUNNABLE → sleeping (TIMED_WAITING)");
            sleep(300);
            System.out.println("  back to RUNNABLE → finishing");
        }, "Lifecycle-Demo");

        System.out.println("  State: " + t.getState()); // NEW
        t.start();
        System.out.println("  State after start: " + t.getState()); // RUNNABLE
        sleep(50);
        System.out.println("  State while sleeping: " + t.getState()); // TIMED_WAITING
        t.join();
        System.out.println("  State after join: " + t.getState()); // TERMINATED
        System.out.println();
    }

    // =========================================================================
    // 2) Visibility — without volatile/synchronized, writes may stay in CPU cache
    // =========================================================================
    static volatile boolean stopFlag = false; // volatile = visible to all threads immediately

    static void visibilityProblemAndVolatile() throws InterruptedException {
        System.out.println("=== 2) Visibility + volatile ===");
        System.out.println("  Without volatile, worker might NEVER see main set stop=true");

        stopFlag = false;
        Thread worker = new Thread(() -> {
            int spins = 0;
            while (!stopFlag) { // reads stopFlag repeatedly
                spins++;
            }
            System.out.println("  Worker saw stopFlag=true after " + spins + " spins");
        }, "Volatile-Worker");

        worker.start();
        sleep(100);
        stopFlag = true; // main writes — volatile ensures worker sees it
        worker.join();
        System.out.println("  volatile = force read/write through main memory (visibility guarantee)");
        System.out.println();
    }

    // =========================================================================
    // 3) synchronized — mutual exclusion + visibility
    // =========================================================================
    static void synchronizedRecap() throws InterruptedException {
        System.out.println("=== 3) synchronized recap (Lesson 26) ===");

        Object lock = new Object();
        AtomicInteger count = new AtomicInteger(0);

        Runnable task = () -> {
            for (int i = 0; i < 50_000; i++) {
                synchronized (lock) {
                    count.set(count.get() + 1); // only one thread inside at a time
                }
            }
        };

        Thread a = new Thread(task, "Sync-A");
        Thread b = new Thread(task, "Sync-B");
        a.start();
        b.start();
        a.join();
        b.join();

        System.out.println("  Counter with synchronized: " + count.get() + " (expected 100000)");
        System.out.println("  synchronized = lock + unlock; also flushes visibility between threads");
        System.out.println();
    }

    // =========================================================================
    // 4) wait / notify — threads wait for a condition (classic producer-consumer)
    // =========================================================================
    static final Object bufferLock = new Object();
    static String message = null;

    static void waitNotifyProducerConsumer() throws InterruptedException {
        System.out.println("=== 4) wait() / notify() — producer → consumer ===");

        Thread consumer = new Thread(() -> {
            synchronized (bufferLock) {
                while (message == null) {
                    try {
                        System.out.println("  Consumer waiting (WAITING state)...");
                        bufferLock.wait(); // release lock + sleep until notify
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                System.out.println("  Consumer got: " + message);
            }
        }, "Consumer");

        consumer.start();
        sleep(200);

        synchronized (bufferLock) {
            message = "Hello from producer";
            System.out.println("  Producer set message + notify()");
            bufferLock.notify(); // wake consumer
        }

        consumer.join();
        message = null;
        System.out.println();
    }

    // =========================================================================
    // 5) Deadlock — two locks, wrong order
    // =========================================================================
    static final Object lockA = new Object();
    static final Object lockB = new Object();

    static void deadlockDemo() throws InterruptedException {
        System.out.println("=== 5) Deadlock (avoid in real code!) ===");
        System.out.println("  Thread-1: lock A then B | Thread-2: lock B then A → can freeze forever");
        System.out.println("  Fix: always acquire locks in SAME order, or use java.util.concurrent locks");
        System.out.println("  (Skipping live deadlock demo — would hang the program)");
        System.out.println();
    }

    // =========================================================================
    // 6) CountDownLatch — wait until N tasks finish
    // =========================================================================
    static void countDownLatchDemo() throws InterruptedException {
        System.out.println("=== 6) CountDownLatch — main waits for 3 workers ===");

        CountDownLatch latch = new CountDownLatch(3);

        for (int i = 1; i <= 3; i++) {
            int id = i;
            new Thread(() -> {
                sleep(100 * id);
                System.out.println("  Worker-" + id + " done");
                latch.countDown(); // 3 → 2 → 1 → 0
            }, "Latch-" + id).start();
        }

        System.out.println("  Main waiting on latch...");
        latch.await(); // blocks until count = 0
        System.out.println("  Main continues — all 3 finished");
        System.out.println();
    }

    // =========================================================================
    static void decisionGuide() {
        System.out.println("=== 7) What to use when ===");
        System.out.println("""
                Situation                          Tool
                ─────────                          ────
                Simple parallel tasks              ExecutorService (25d)
                Timers / repeat                    ScheduledThreadPool (25e)
                Divide big array/job               ForkJoinPool (25f)
                Per-request user context           ThreadLocal (25c)
                Shared counter                     AtomicInteger (27)
                Critical section                   synchronized / Lock (26)
                Flag visible to all threads        volatile
                Wait for condition                 wait/notify or BlockingQueue
                Wait for N threads to finish       CountDownLatch / CompletableFuture.allOf
                Async pipeline                     CompletableFuture (27b)

                Golden rules:
                  • Prefer pools over raw new Thread()
                  • Prefer java.util.concurrent over wait/notify for new code
                  • ThreadLocal.remove() in pools (25c)
                  • Never hold two locks in different orders (deadlock)
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
