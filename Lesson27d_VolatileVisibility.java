// Lesson 27d: Visibility problem — what volatile solves (NOT counters)
// Compile: javac Lesson27d_VolatileVisibility.java
// Run:     java Lesson27d_VolatileVisibility
//
// Place: after Lesson 27c (volatile vs Atomic for counters).
// This file is ONLY about: "Thread B doesn't see Thread A's write"
//
// volatile fixes VISIBILITY (see latest value).
// volatile does NOT fix count++ — see Lesson 27c for that.

public class Lesson27d_VolatileVisibility {

    public static void main(String[] args) throws InterruptedException {
        explainProblem();
        demoWithoutVolatile();
        demoWithVolatile();
        whatVolatileGuarantees();
    }

    static void explainProblem() {
        System.out.println("=== 0) The visibility problem ===");
        System.out.println("""
                Thread MAIN writes:  ready = true
                Thread WORKER reads:  while (!ready) { ... }

                Without volatile, WORKER may read a STALE copy from its CPU cache
                → loop forever (in theory; JVM may still "accidentally" work)

                volatile = every read goes to main memory, every write flushes there
                → WORKER is GUARANTEED to see ready = true
                """);
    }

    // -------------------------------------------------------------------------
    // PROBLEM: plain boolean — no visibility guarantee (Java Memory Model)
    // -------------------------------------------------------------------------
    static boolean readyWithoutVolatile = false;

    static void demoWithoutVolatile() throws InterruptedException {
        System.out.println("=== PROBLEM: boolean without volatile ===");

        readyWithoutVolatile = false;

        Thread worker = new Thread(() -> {
            long spins = 0;
            while (!readyWithoutVolatile) {
                spins++;
                if (spins > 500_000_000L) {
                    // Safety: stop demo if visibility bug happens (would hang forever)
                    System.out.println("  Worker STILL sees ready=false after 500M spins ❌");
                    System.out.println("  (This is the visibility bug — no guarantee without volatile)");
                    return;
                }
            }
            System.out.println("  Worker saw ready=true after " + spins + " spins");
            System.out.println("  (May work on your machine by luck — still NOT guaranteed by Java!)");
        }, "Worker-NoVolatile");

        worker.start();
        Thread.sleep(50);
        readyWithoutVolatile = true; // MAIN writes — worker may NOT see this
        worker.join(2000);

        if (worker.isAlive()) {
            worker.interrupt();
            System.out.println("  ⚠️  Worker still running → visibility problem shown!");
        }
        System.out.println();
    }

    // -------------------------------------------------------------------------
    // SOLUTION: volatile boolean — visibility guaranteed
    // -------------------------------------------------------------------------
    static volatile boolean readyWithVolatile = false;

    static void demoWithVolatile() throws InterruptedException {
        System.out.println("=== SOLUTION: volatile boolean ===");

        readyWithVolatile = false;

        Thread worker = new Thread(() -> {
            long spins = 0;
            while (!readyWithVolatile) {
                spins++;
            }
            System.out.println("  Worker saw ready=true after " + spins + " spins ✅");
            System.out.println("  volatile guarantees MAIN's write is visible to WORKER");
        }, "Worker-Volatile");

        worker.start();
        Thread.sleep(50);
        readyWithVolatile = true; // MAIN writes — worker MUST see this (JMM guarantee)
        worker.join();

        System.out.println();
    }

    static void whatVolatileGuarantees() {
        System.out.println("=== What volatile solves vs does NOT ===");
        System.out.println("""
                volatile SOLVES:
                  ✅ Thread B sees Thread A's latest write (visibility)
                  ✅ Good for: flags, status, one-time publish (ready, stop, done)

                volatile does NOT solve:
                  ❌ count++  (read + add + write = 3 steps → use AtomicInteger)
                  ❌ check-then-act: if (x==0) x=1  (use synchronized or Atomic)

                Remember:
                  Lesson 27d → visibility problem  → volatile flag
                  Lesson 27c → counter race problem → AtomicInteger
                """);
    }
}
