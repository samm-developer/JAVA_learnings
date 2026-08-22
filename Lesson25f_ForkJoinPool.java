// Lesson 25f: ForkJoinPool — divide work, workers steal from each other
// Compile: javac Lesson25f_ForkJoinPool.java
// Run:     java Lesson25f_ForkJoinPool
//
// Place: after Lesson 25d/25e (thread pools) + before/with Lesson 27b (CompletableFuture).
//
// Normal pool:     one shared task queue
// ForkJoinPool:    each worker has its own deque; idle workers STEAL tasks from others
//
// Best for: divide-and-conquer (split big job → smaller sub-jobs → merge result)
//
// CompletableFuture.supplyAsync() and parallelStream() use ForkJoinPool.commonPool() by default.

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

public class Lesson25f_ForkJoinPool {

    public static void main(String[] args) throws Exception {
        // whyForkJoin();
        // sumWithRecursiveTask();
        commonPoolDemo();
        // vsNormalPool();
    }

    static void whyForkJoin() {
        System.out.println("=== 0) ForkJoinPool idea ===");
        System.out.println("""
                Big task: sum 1..1_000_000
                  split → [1..500k] + [500k+1..1M]
                  split again until chunks are small
                  workers run chunks in parallel
                  merge partial sums

                  Worker-1 deque: [chunk-A][chunk-B]
                  Worker-2 deque: [chunk-C]  ← steals from Worker-1 if idle (work-stealing)
                """);
    }

    // -------------------------------------------------------------------------
    // 1) RecursiveTask<V> — split, fork subtasks, join results
    // -------------------------------------------------------------------------
    static void sumWithRecursiveTask() throws Exception {
        System.out.println("=== 1) RecursiveTask — parallel sum of 1..N ===");

        long n = 1_000_000;

        // Sequential baseline
        long startSeq = System.nanoTime();
        long seq = LongStream.rangeClosed(1, n).sum();
        double seqMs = (System.nanoTime() - startSeq) / 1_000_000.0;
        System.out.println("  Sequential sum: " + seq + "  (" + String.format("%.2f", seqMs) + " ms)");

        // ForkJoin: custom pool with 4 workers
        try (ForkJoinPool pool = new ForkJoinPool(4)) {
            long start = System.nanoTime();
            long parallel = pool.invoke(new SumTask(1, n));
            double ms = (System.nanoTime() - start) / 1_000_000.0;
            System.out.println("  ForkJoin sum:   " + parallel + "  (" + String.format("%.2f", ms) + " ms)");
        }

        System.out.println("  (Speedup depends on CPU; correctness matters more here)");
        System.out.println();
    }

    /** Splits [from..to] until range <= THRESHOLD, then sums directly. */
    static class SumTask extends RecursiveTask<Long> {
        private static final int THRESHOLD = 50_000;
        private final long from;
        private final long to;

        SumTask(long from, long to) {
            this.from = from;
            this.to = to;
        }

        @Override
        protected Long compute() {
            long size = to - from + 1;
            if (size <= THRESHOLD) {
                // base case — small enough to do on one thread
                long sum = 0;
                for (long i = from; i <= to; i++) {
                    sum += i;
                }
                return sum;
            }

            // divide
            long mid = from + (to - from) / 2;
            SumTask left = new SumTask(from, mid);
            SumTask right = new SumTask(mid + 1, to);

            left.fork();              // run left async on pool
            long rightSum = right.compute(); // run right on current thread (or fork both)
            long leftSum = left.join();        // wait for left

            return leftSum + rightSum; // merge
        }
    }

    // -------------------------------------------------------------------------
    // 2) commonPool() — shared pool used by parallelStream / CompletableFuture
    // -------------------------------------------------------------------------
    static void commonPoolDemo() {
        System.out.println("=== 2) ForkJoinPool.commonPool() ===");

        ForkJoinPool common = ForkJoinPool.commonPool();
        System.out.println("  Parallelism: " + common.getParallelism());
        System.out.println("  Pool size:   " + common.getPoolSize());

        long sum = LongStream.rangeClosed(1, 100_000)
                .parallel() // uses commonPool under the hood
                .sum();
        System.out.println("  parallelStream sum: " + sum);
        System.out.println("  Thread names look like: ForkJoinPool.commonPool-worker-N");
        System.out.println();
    }

    // -------------------------------------------------------------------------
    // 3) ForkJoin vs fixed ExecutorService
    // -------------------------------------------------------------------------
    static void vsNormalPool() throws Exception {
        System.out.println("=== 3) When to use which ===");
        System.out.println("""
                ExecutorService (FixedThreadPool)
                  → independent tasks (emails, API calls)
                  → one task = one job

                ForkJoinPool + RecursiveTask
                  → one big job split into related sub-parts
                  → merge results (sum, sort, tree walk)

                Rule of thumb:
                  unrelated tasks     → ExecutorService
                  divide-and-conquer  → ForkJoinPool
                """);
    }
}
