// Lesson 25e: Thread pool types — cached, single, scheduled
// Compile: javac Lesson25e_ThreadPoolTypes.java
// Run:     java Lesson25e_ThreadPoolTypes
//
// Place: after Lesson 25d (Thread pool basics).
//
//   newFixedThreadPool(n)     → fixed n workers (Lesson 25d)
//   newCachedThreadPool()     → grows/shrinks; good for many SHORT tasks
//   newSingleThreadExecutor() → ONE worker; tasks run one-by-one in order
//   newScheduledThreadPool(n) → run after delay / repeat on a timer

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class Lesson25e_ThreadPoolTypes {

    public static void main(String[] args) throws Exception {
        // compareOverview();
        // cachedThreadPoolDemo();
        // singleThreadExecutorDemo();
        scheduledThreadPoolDemo();
        // summary();
    }

    static void compareOverview() {
        System.out.println("=== 0) Quick compare ===");
        System.out.println("""
                cached   → 0..many threads; creates new if all busy; idle threads die after ~60s
                single   → exactly 1 thread; queue keeps order
                scheduled→ run task once after delay, or repeat every N seconds
                """);
    }

    // -------------------------------------------------------------------------
    // 1) CACHED — many short tasks; pool grows, then reuses / shrinks
    // -------------------------------------------------------------------------
    static void cachedThreadPoolDemo() throws InterruptedException {
        System.out.println("=== 1) newCachedThreadPool() ===");

        ExecutorService cached = Executors.newCachedThreadPool();

        // --- Part A: burst of 5 at once → NO reuse (all start before any finishes) ---
        System.out.println("  Part A: 5 tasks submitted together (each sleeps 100ms)");
        for (int i = 1; i <= 5; i++) {
            int n = i;
            cached.execute(() -> {
                System.out.println("    task-" + n + " on " + Thread.currentThread().getName());
                sleep(100);
            });
        }
        cached.awaitTermination(500, TimeUnit.MILLISECONDS); // wait for Part A to finish

        // --- Part B: 2 workers finish first, then pick up more → REUSE same threads ---
        System.out.println("  Part B: 2 threads only; 4 tasks queued → reuse visible");
        for (int i = 1; i <= 4; i++) {
            int n = i;
            cached.execute(() -> {
                System.out.println("    wave2-task-" + n + " on " + Thread.currentThread().getName());
                sleep(200);
            });
        }

        cached.shutdown();
        cached.awaitTermination(3, TimeUnit.SECONDS);
        System.out.println("""
                  Part A: 5 parallel starts → pool created ~5 threads (none free yet)
                  Part B: only 2 needed at a time → same thread names appear again = REUSE
                  Idle cached threads are removed after ~60s if unused
                """);
        System.out.println();
    }

    // -------------------------------------------------------------------------
    // 2) SINGLE — one worker; strict order (task-2 never before task-1 finishes)
    // -------------------------------------------------------------------------
    static void singleThreadExecutorDemo() throws InterruptedException {
        System.out.println("=== 2) newSingleThreadExecutor() ===");

        ExecutorService single = Executors.newSingleThreadExecutor();

        single.execute(() -> {
            System.out.println("  task-1 start  " + Thread.currentThread().getName());
            sleep(300);
            System.out.println("  task-1 end");
        });
        single.execute(() -> {
            System.out.println("  task-2 start  " + Thread.currentThread().getName());
            sleep(100);
            System.out.println("  task-2 end");
        });
        single.execute(() -> {
            System.out.println("  task-3 start  " + Thread.currentThread().getName());
            System.out.println("  task-3 end");
        });

        single.shutdown();
        single.awaitTermination(3, TimeUnit.SECONDS);
        System.out.println("  (All on SAME thread name; always 1 → 2 → 3 order)");
        System.out.println();
    }

    // -------------------------------------------------------------------------
    // 3) SCHEDULED — delay + repeat
    // -------------------------------------------------------------------------
    static void scheduledThreadPoolDemo() throws InterruptedException {
        System.out.println("=== 3) newScheduledThreadPool() ===");

        ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(2);

        // A) Run ONCE after 500ms delay
        scheduled.schedule(() -> {
            System.out.println("  [once] ran after 500ms on " + Thread.currentThread().getName());
        }, 500, TimeUnit.MILLISECONDS);

        // B) Run FIRST after 200ms, then every 400ms (3 times total)
        var repeated = scheduled.scheduleAtFixedRate(() -> {
            System.out.println("  [repeat] tick on " + Thread.currentThread().getName()
                    + " at " + System.currentTimeMillis() % 100_000);
        }, 200, 400, TimeUnit.MILLISECONDS);

        sleep(1500); // let a few ticks run
        repeated.cancel(false); // stop future ticks

        // C) scheduleWithFixedDelay = wait AFTER task finishes, then delay again
        scheduled.scheduleWithFixedDelay(() -> {
            System.out.println("  [delay] start");
            sleep(200); // task takes 200ms
            System.out.println("  [delay] end (next run 300ms after THIS ends)");
        }, 0, 300, TimeUnit.MILLISECONDS);

        sleep(1200);

        scheduled.shutdown();
        scheduled.awaitTermination(2, TimeUnit.SECONDS);
        System.out.println();
    }

    static void summary() {
        System.out.println("=== Summary — when to pick ===");
        System.out.println("""
                newCachedThreadPool()
                  → many short async jobs; don't use if tasks can pile up forever

                newSingleThreadExecutor()
                  → must run tasks in order (log writer, serial queue)

                newScheduledThreadPool(n)
                  → timers: retry after 5s, heartbeat every 30s, cron-like jobs

                All pools: shutdown() when done!
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
