// Lesson 25d: Thread pool (reuse worker threads instead of new Thread every time)
// Compile: javac Lesson25d_ThreadPool.java
// Run:     java Lesson25d_ThreadPool
//
// Place: after Lesson 25 (Threads) / 25c (ThreadLocal).
//
// Without pool: 100 tasks → create 100 Threads → slow + heavy
// With pool:     100 tasks → 4 worker threads reuse → faster + controlled
//
// Main API: ExecutorService (java.util.concurrent)

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Lesson25d_ThreadPool {

    public static void main(String[] args) throws Exception {
        whyPool();
        fixedPoolExecute();
        fixedPoolSubmitAndFuture();
        manyTasksFewWorkers();
        shutdownProperly();
        poolTypes();
    }

    static void whyPool() {
        System.out.println("=== 0) Why thread pool? ===");
        System.out.println("""
                new Thread(...).start()  → new OS thread each time (expensive)
                ExecutorService pool      → fixed workers pick tasks from a queue

                  Task queue:  [T1][T2][T3][T4][T5]...
                                    ↓
                  Pool (e.g. 2 threads):  Worker-1  Worker-2
                                            ↑ reuse same threads
                """);
    }

    static void fixedPoolExecute() throws InterruptedException {
        System.out.println("=== 1) Fixed pool + execute() — fire-and-forget ===");

        // 2 worker threads in the pool (names like pool-1-thread-1)
        ExecutorService pool = Executors.newFixedThreadPool(2);

        pool.execute(() -> task("Email-1"));
        pool.execute(() -> task("Email-2"));
        pool.execute(() -> task("Email-3")); // waits in queue until a worker is free

        pool.shutdown();           // no new tasks; finish existing ones
        pool.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println();
    }

    static void fixedPoolSubmitAndFuture() throws Exception {
        System.out.println("=== 2) submit() + Future — get a result back ===");

        ExecutorService pool = Executors.newFixedThreadPool(2);

        // Runnable → Future<?> (no return value)
        Future<?> f1 = pool.submit(() -> task("Report"));

        // Callable → Future<T> (returns a value)
        Future<Integer> f2 = pool.submit(new Callable<Integer>() {
            @Override
            public Integer call() {
                task("Calculate");
                return 42;
            }
        });

        // Lambda Callable (shorter)
        Future<String> f3 = pool.submit(() -> {
            task("Fetch-user");
            return "Asha";
        });

        f1.get(); // wait until Report finishes
        System.out.println("  Result from calculate: " + f2.get());
        System.out.println("  Result from fetch:     " + f3.get());

        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println();
    }

    static void manyTasksFewWorkers() throws InterruptedException {
        System.out.println("=== 3) 6 tasks, 2 workers — queue + reuse ===");

        ExecutorService pool = Executors.newFixedThreadPool(2);
        long start = System.currentTimeMillis();

        for (int i = 1; i <= 6; i++) {
            int taskNo = i;
            pool.execute(() -> {
                System.out.println("  Task-" + taskNo + " on "
                        + Thread.currentThread().getName());
                sleep(300);
            });
        }

        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println("  Done in ~" + (System.currentTimeMillis() - start) + " ms");
        System.out.println("  (6 × 300ms tasks with 2 workers ≈ 900ms, not 1800ms sequential)");
        System.out.println();
    }

    static void shutdownProperly() throws InterruptedException {
        System.out.println("=== 4) Always shutdown the pool ===");

        ExecutorService pool = Executors.newFixedThreadPool(1);
        pool.execute(() -> System.out.println("  Working..."));

        // shutdown()     → stop accepting new tasks; let running ones finish
        // shutdownNow()→ try to interrupt running tasks (emergency)
        pool.shutdown();

        if (!pool.awaitTermination(3, TimeUnit.SECONDS)) {
            System.out.println("  Timed out — force shutdownNow()");
            pool.shutdownNow();
        }
        System.out.println("  Pool terminated: " + pool.isTerminated());
        System.out.println();
    }

    static void poolTypes() throws InterruptedException, ExecutionException {
        System.out.println("=== 5) Common pool types (pick by use case) ===");

        // Fixed — stable worker count (most common for CPU/IO work)
        ExecutorService fixed = Executors.newFixedThreadPool(4);

        // Cached — grows/shrinks as needed (good for many short tasks; can grow large!)
        ExecutorService cached = Executors.newCachedThreadPool();

        // Single — one worker, tasks run in order (like a serial queue)
        ExecutorService single = Executors.newSingleThreadExecutor();

        fixed.execute(() -> System.out.println("  fixed:   " + Thread.currentThread().getName()));
        cached.execute(() -> System.out.println("  cached:  " + Thread.currentThread().getName()));
        single.execute(() -> System.out.println("  single:  " + Thread.currentThread().getName()));

        fixed.shutdown();
        cached.shutdown();
        single.shutdown();

        fixed.awaitTermination(2, TimeUnit.SECONDS);
        cached.awaitTermination(2, TimeUnit.SECONDS);
        single.awaitTermination(2, TimeUnit.SECONDS);

        System.out.println();
        System.out.println("Summary:");
        System.out.println("  execute(Runnable)  → void, no result");
        System.out.println("  submit(Callable)   → Future<T> with result");
        System.out.println("  shutdown() + awaitTermination() → clean exit");
        System.out.println("  Reused threads → remember ThreadLocal.remove() (Lesson 25c)");
    }

    static void task(String name) {
        System.out.println("  " + name + " on " + Thread.currentThread().getName());
        sleep(200);
    }

    static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
