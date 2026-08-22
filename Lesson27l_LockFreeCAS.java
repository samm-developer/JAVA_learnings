// Lesson 27l: Lock-free concurrency using CAS (Compare-And-Swap)
// Compile: javac Lesson27l_LockFreeCAS.java
// Run:     java Lesson27l_LockFreeCAS
//
// After: Lesson 27k (Semaphore)
//
// CAS = CPU instruction: "if value still equals EXPECTED, set NEW, else fail"
// No lock object. Threads retry on failure.
// AtomicInteger uses CAS under the hood.

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Lesson27l_LockFreeCAS {

    static final int TIMES = 100_000;

    public static void main(String[] args) throws InterruptedException {
        explainCas();
        atomicUsesCas();
        manualCompareAndSet();
        vsLockBased();
    }

    static void explainCas() {
        System.out.println("=== What is CAS? ===");
        System.out.println("""
                compareAndSet(expected, update):
                  if (current == expected) {
                      current = update;
                      return true;   // success
                  } else {
                      return false;  // someone else changed it — retry
                  }

                Lock-based:  wait for key, then update
                Lock-free:   try CAS; if fail, try again (no parking on a lock)
                """);
    }

    static void atomicUsesCas() throws InterruptedException {
        System.out.println("=== AtomicInteger = lock-free counter (CAS inside) ===");
        AtomicInteger count = new AtomicInteger(0);

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < TIMES; i++) {
                count.incrementAndGet(); // CAS loop inside JDK
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < TIMES; i++) {
                count.incrementAndGet();
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("  count=" + count.get() + "  ✅ expected " + (TIMES * 2));
        System.out.println();
    }

    static void manualCompareAndSet() {
        System.out.println("=== Manual CAS with AtomicReference ===");

        AtomicReference<String> ref = new AtomicReference<>("Asha");

        boolean ok = ref.compareAndSet("Asha", "Riya");
        System.out.println("  CAS Asha→Riya: " + ok + "  value=" + ref.get());

        boolean fail = ref.compareAndSet("Asha", "Dev"); // expected stale
        System.out.println("  CAS Asha→Dev:  " + fail + "  value=" + ref.get() + " (failed — expected)");

        // Spin until success (simple lock-free update pattern)
        String prev;
        do {
            prev = ref.get();
        } while (!ref.compareAndSet(prev, prev + "!"));
        System.out.println("  after spin CAS: " + ref.get());
        System.out.println();
    }

    static void vsLockBased() {
        System.out.println("=== Lock-free vs Lock-based ===");
        System.out.println("""
                Lock-free (CAS / Atomic*)
                  + no thread blocked on a lock
                  + great for counters, flags, simple updates
                  - hard for multi-step logic (ABA problem, retries)

                Lock-based (synchronized / ReentrantLock / ...)
                  + clear critical sections
                  + easier for complex operations
                  - threads may wait / context switch

                You already used lock-free: AtomicInteger (Lesson 27c)
                ConcurrentHashMap also uses CAS-style techniques internally.

                Next: Condition + fair locks (Lesson 27m)
                """);
    }
}
