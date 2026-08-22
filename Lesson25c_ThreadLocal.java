// Lesson 25c: ThreadLocal — per-thread copy on the heap (not shared)
// Compile: javac Lesson25c_ThreadLocal.java
// Run:     java Lesson25c_ThreadLocal
//
// Place: after Lesson 25b (threads in memory).
//
// Problem: normal static/shared field → all threads see the same value (race).
// ThreadLocal: each thread gets its OWN slot — no lock needed for that slot.
//
// Memory (simplified):
//   ThreadLocal object (heap) holds a map-like structure internally:
//     Thread-A → value "Asha"
//     Thread-B → value "Riya"
//   Each thread reads/writes only its own entry.

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Lesson25c_ThreadLocal {

    // ===== Shared (BAD for per-thread data) =====
    static String sharedUser = "nobody";

    // ===== ThreadLocal (GOOD for per-thread data) =====
    static final ThreadLocal<String> currentUser = ThreadLocal.withInitial(() -> "guest");

    static final ThreadLocal<Long> requestId = new ThreadLocal<>();

    public static void main(String[] args) throws Exception {
        // memoryIdea();
        // sharedVsThreadLocal();
        withInitialDemo();
        practicalRequestContext();
        threadPoolMustRemove();
    }

    static void memoryIdea() {
        System.out.println("=== 0) ThreadLocal idea ===");
        System.out.println("""
                Shared static String user     → 1 value, all threads fight over it
                ThreadLocal<String> user      → each thread has its own value

                  ThreadLocal (heap)
                    ├── main    → "guest"
                    ├── Worker-A → "Asha"
                    └── Worker-B → "Riya"

                get()  → read MY thread's value
                set()  → write MY thread's value
                remove() → clear MY thread's value (important in thread pools!)
                """);
    }

    static void sharedVsThreadLocal() throws InterruptedException {
        System.out.println("=== 1) Shared static vs ThreadLocal ===");

        Thread a = new Thread(() -> {
            sharedUser = "Asha";
            currentUser.set("Asha");
            sleep(200); // let B run in between
            System.out.println("  Thread-A sharedUser=" + sharedUser
                    + "  threadLocal=" + currentUser.get());
        }, "A");

        Thread b = new Thread(() -> {
            sleep(50);
            sharedUser = "Riya"; // overwrites A's shared value!
            currentUser.set("Riya");
            System.out.println("  Thread-B sharedUser=" + sharedUser
                    + "  threadLocal=" + currentUser.get());
        }, "B");

        a.start();
        b.start();
        a.join();
        b.join();

        System.out.println("  Main sees sharedUser=" + sharedUser
                + "  threadLocal=" + currentUser.get());
        System.out.println("  → shared changed by other threads; ThreadLocal did not leak across threads");
        System.out.println();
    }

    static void withInitialDemo() throws InterruptedException {
        System.out.println("=== 2) withInitial() — default when never set ===");

        Thread t = new Thread(() -> {
            // never called set() — gets default "guest"
            System.out.println("  Worker without set: " + currentUser.get());
            currentUser.set("Dev");
            System.out.println("  Worker after set:   " + currentUser.get());
        }, "Worker");

        System.out.println("  Main (never set):     " + currentUser.get());
        t.start();
        t.join();
        System.out.println("  Main still:           " + currentUser.get());
        System.out.println();
    }

    static void practicalRequestContext() throws InterruptedException {
        System.out.println("=== 3) Practical: request id per thread (like a web request) ===");

        Runnable handleRequest = () -> {
            long id = Thread.currentThread().threadId();
            requestId.set(id * 1000); // pretend unique request id

            log("start");
            doWork();
            log("end");

            // In real apps in a thread pool, always remove in finally { requestId.remove(); }
        };

        Thread r1 = new Thread(handleRequest, "Request-1");
        Thread r2 = new Thread(handleRequest, "Request-2");

        r1.start();
        r2.start();
        r1.join();
        r2.join();
        System.out.println();
    }

    static void doWork() {
        sleep(100);
        log("working...");
    }

    static void log(String msg) {
        Long id = requestId.get();
        System.out.println("  [req-" + id + "][" + Thread.currentThread().getName() + "] " + msg);
    }

    static void threadPoolMustRemove() throws Exception {
        System.out.println("=== 4) Thread pool: call remove() or old value leaks ===");

        ExecutorService pool = Executors.newFixedThreadPool(1); // same worker thread reused

        pool.submit(() -> {
            currentUser.set("Asha");
            System.out.println("  Task-1 sets user=Asha → " + currentUser.get());
            // BAD: forgot remove() — next task on same thread may see "Asha"
        }).get();

        pool.submit(() -> {
            System.out.println("  Task-2 without set sees → " + currentUser.get() + "  (leaked!)");
            currentUser.remove(); // fix for future tasks
        }).get();

        pool.submit(() -> {
            currentUser.set("Riya");
            System.out.println("  Task-3 sets user=Riya → " + currentUser.get());
            try {
                // work...
            } finally {
                currentUser.remove(); // ✅ always clean up in pool threads
            }
            System.out.println("  Task-3 removed → " + currentUser.get());
        }).get();

        pool.shutdown();
        pool.awaitTermination(2, TimeUnit.SECONDS);

        System.out.println();
        System.out.println("Summary:");
        System.out.println("  ThreadLocal = per-thread variable (no sharing, no race on that slot)");
        System.out.println("  Use for: user context, request id, locale, DB connection per thread (older patterns)");
        System.out.println("  Always remove() in thread pools to avoid memory leaks / stale data");
    }

    static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
