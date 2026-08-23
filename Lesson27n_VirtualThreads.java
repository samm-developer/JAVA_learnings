// Lesson 27n: Virtual threads (Java 21+)
// Compile: javac LessonConsole.java Lesson27n_VirtualThreads.java
// Run:     java Lesson27n_VirtualThreads
//
// Left-topics series (core gaps) — start here:
//   27n Virtual threads     ← YOU ARE HERE
//   27o BlockingQueue
//   10b Polymorphism
//   10c Inner classes
//   … (see summary at end)
//
// After: 25d (thread pools), 27b (CompletableFuture)
//
// ========== CHEAT SHEET ==========
// Platform thread = OS thread (~1 MB stack) — expensive, limited count
// Virtual thread  = JVM-managed, cheap — millions for I/O-bound work (HTTP, DB, sleep)
// API: Thread.ofVirtual().start(...) | Executors.newVirtualThreadPerTaskExecutor()
// Bad for: heavy CPU work (use platform pool / ForkJoinPool instead)

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Lesson27n_VirtualThreads {

    static final int TASKS = 2_000;
    static final int SLEEP_MS = 20; // simulate I/O wait

    public static void main(String[] args) throws Exception {
        platformVsVirtualIdea();
        problemSmallPlatformPool();
        solutionVirtualThreads();
        threadOfVirtual();
        whenNotToUse();
        summary();
    }

    static void platformVsVirtualIdea() {
        LessonConsole.heading("=== 0) Platform thread vs virtual thread ===");
        System.out.println("""
                Platform thread (classic):
                  1 Thread = 1 OS thread
                  ~1 MB stack each → few thousand max on typical machine

                Virtual thread (Java 21+):
                  millions of virtual threads → few platform "carrier" threads
                  great when task WAITS (HTTP, DB, sleep) — not computing

                  Task waits → carrier thread runs another virtual thread
                """);
    }

    static void problemSmallPlatformPool() throws Exception {
        LessonConsole.heading("=== PROBLEM: small platform pool + many I/O tasks ===");
        System.out.println("  " + TASKS + " tasks (each sleeps " + SLEEP_MS + "ms), pool size = 50");

        long start = System.nanoTime();
        ExecutorService pool = Executors.newFixedThreadPool(50); // 50 OS threads
        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < TASKS; i++) {
            futures.add(pool.submit(() -> sleep(SLEEP_MS)));
        }
        for (Future<?> f : futures) {
            f.get();
        }
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.MINUTES);

        long ms = (System.nanoTime() - start) / 1_000_000;
        System.out.println("  platform pool (50 threads): " + ms + " ms  ❌ tasks queued behind pool size");
        System.out.println();
    }

    static void solutionVirtualThreads() throws Exception {
        LessonConsole.heading("=== SOLUTION: virtual thread per task ===");
        System.out.println("  same " + TASKS + " tasks — one virtual thread each");

        long start = System.nanoTime();
        try (ExecutorService vte = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<?>> futures = new ArrayList<>();
            for (int i = 0; i < TASKS; i++) {
                futures.add(vte.submit(() -> sleep(SLEEP_MS)));
            }
            for (Future<?> f : futures) {
                f.get();
            }
        }

        long ms = (System.nanoTime() - start) / 1_000_000;
        System.out.println("  virtual threads: " + ms + " ms  ✅ much faster for I/O-style waits");
        System.out.println("  (" + Thread.class.getSimpleName() + ".isVirtual() = "
                + Thread.currentThread().isVirtual() + " for main — main is platform thread)");
        System.out.println();
    }

    static void threadOfVirtual() throws InterruptedException {
        LessonConsole.heading("=== 2) Thread.ofVirtual() — direct API ===");
        Thread v = Thread.ofVirtual().name("vt-demo").start(() -> {
            System.out.println("  running in " + Thread.currentThread()
                    + " isVirtual=" + Thread.currentThread().isVirtual());
        });
        v.join();
        System.out.println();
    }

    static void whenNotToUse() {
        LessonConsole.heading("=== 3) When NOT to use virtual threads ===");
        System.out.println("""
                CPU-heavy math (no waiting)     → platform pool / ForkJoinPool (25f)
                synchronized + long work inside → can "pin" carrier (advanced — keep critical sections short)
                Legacy code with thread-per-thread-local assumptions → test carefully

                Spring Boot 3.2+ can enable virtual threads for web requests (separate lesson).
                """);
    }

    static void summary() {
        LessonConsole.heading("=== Summary: Virtual threads ===");
        System.out.println("""
                Use for:     many concurrent I/O-bound tasks (API calls, DB, sleep)
                Create:      Executors.newVirtualThreadPerTaskExecutor()
                             Thread.ofVirtual().start(...)
                Next topic:  Lesson 27o BlockingQueue
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
