// Lesson 27b: CompletableFuture (async without managing Thread yourself)
// Compile: javac Lesson27b_CompletableFuture.java
// Run:     java Lesson27b_CompletableFuture
//
// Place: after threads / synchronized / atomic (Lessons 25–27).
//
// Cheat sheet:
//   supplyAsync()  → async task + result
//   runAsync()     → async task, no result
//   thenApply()    → transform result (T → U)
//   thenAccept()   → consume result (T → void)
//   thenRun()      → after done, run something (no input)
//   thenCompose()  → dependent futures (flatMap-style)
//   thenCombine()  → combine 2 independent futures
//   allOf()        → wait until ALL complete
//   anyOf()        → wait until ANY one completes
//   exceptionally()→ recover from error
//   handle()       → see result AND error together

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Lesson27b_CompletableFuture {
    public static void main(String[] args) throws Exception {
        System.out.println("Main thread: " + Thread.currentThread().getName());

        // ===== 1) supplyAsync + thenApply + thenAccept + thenRun =====
        CompletableFuture<Void> chain = CompletableFuture
                .supplyAsync(() -> {                 // async + RESULT
                    sleep(200);
                    System.out.println("supplyAsync on: " + Thread.currentThread().getName());
                    return "Shashwat";
                })
                .thenApply(name -> "Hello, " + name + "!")  // transform
                .thenApply(String::toUpperCase)             // transform again
                .thenAccept(msg -> System.out.println("thenAccept: " + msg)) // consume
                .thenRun(() -> System.out.println("thenRun: chain finished")); // no value

        chain.get(); // wait until the whole chain is done

        // ===== 2) thenCombine — 2 independent futures =====
        CompletableFuture<Integer> price = CompletableFuture.supplyAsync(() -> {
            sleep(150);
            return 100;
        });
        CompletableFuture<Integer> qty = CompletableFuture.supplyAsync(() -> {
            sleep(100);
            return 3;
        });
        int total = price.thenCombine(qty, (p, q) -> p * q).get();
        System.out.println("thenCombine total: " + total);

        // ===== 3) thenCompose — dependent future (next needs previous result) =====
        // Like: get userId, THEN fetch profile for that id (flatMap style)
        String profile = CompletableFuture
                .supplyAsync(() -> {
                    sleep(100);
                    return 42; // user id
                })
                .thenCompose(userId -> CompletableFuture.supplyAsync(() -> {
                    sleep(100);
                    return "profile-of-" + userId;
                }))
                .get();
        System.out.println("thenCompose: " + profile);

        // ===== 4) allOf — wait for ALL (side-effect tasks via runAsync) =====
        long t0 = System.currentTimeMillis();
        CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> fakeApi("users")),
                CompletableFuture.runAsync(() -> fakeApi("orders")),
                CompletableFuture.runAsync(() -> fakeApi("products"))
        ).get();
        System.out.println("allOf done in " + (System.currentTimeMillis() - t0) + " ms");

        // ===== 5) anyOf — wait for FIRST finished =====
        CompletableFuture<Object> first = CompletableFuture.anyOf(
                CompletableFuture.supplyAsync(() -> {
                    sleep(300);
                    return "slow";
                }),
                CompletableFuture.supplyAsync(() -> {
                    sleep(80);
                    return "fast";
                })
        );
        System.out.println("anyOf winner: " + first.get());

        // ===== 6) exceptionally — error → fallback value =====
        String safe = CompletableFuture
                .supplyAsync(() -> {
                    if (true) throw new RuntimeException("boom");
                    return "ok";
                })
                .exceptionally(ex -> "fallback: " + ex.getCause().getMessage())
                .get();
        System.out.println("exceptionally: " + safe);

        // ===== 7) handle — always runs; you get (result, error) =====
        String handledOk = CompletableFuture
                .supplyAsync(() -> "success")
                .handle((result, err) -> err == null ? "OK:" + result : "ERR:" + err.getMessage())
                .get();
        String handledErr = CompletableFuture
                .supplyAsync(() -> {
                    throw new RuntimeException("fail");
                })
                .handle((result, err) -> err == null ? "OK:" + result : "ERR:" + err.getCause().getMessage())
                .get();
        System.out.println("handle success: " + handledOk);
        System.out.println("handle error:   " + handledErr);

        // ===== 8) Custom thread pool (good practice) =====
        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            String fromPool = CompletableFuture
                    .supplyAsync(() -> "from-pool on " + Thread.currentThread().getName(), pool)
                    .get(1, TimeUnit.SECONDS);
            System.out.println(fromPool);
        } finally {
            pool.shutdown();
        }
    }

    static void fakeApi(String name) {
        sleep(100);
        System.out.println("Fetched " + name + " on " + Thread.currentThread().getName());
    }

    static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
