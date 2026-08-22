// Lesson 27f: What is a LOCK? (start here before other lock lessons)
// Compile: javac Lesson27f_WhatIsALock.java
// Run:     java Lesson27f_WhatIsALock
//
// Study order:
//   27f What is a lock          ← YOU ARE HERE
//   27g synchronized
//   27h ReentrantLock
//   27i ReadWriteLock (shared + exclusive)
//   27j StampedLock
//   27k Semaphore
//   27l Lock-free (CAS)
//   27m Condition + fair locks
//
// ========== BIG PICTURE ==========
//
// LOCK-BASED concurrency
//   → Only ONE thread (or limited threads) enters critical section
//   → Tools: synchronized, ReentrantLock, ReadWriteLock, StampedLock, Semaphore
//
// LOCK-FREE concurrency
//   → No lock object; use CPU CAS (compare-and-swap)
//   → Tools: AtomicInteger, ConcurrentHashMap (internally), etc.
//
// SHARED lock  = many readers OK at the same time (ReadWriteLock.readLock)
// EXCLUSIVE lock = only one writer (ReadWriteLock.writeLock / normal Lock)

public class Lesson27f_WhatIsALock {

    static int unsafeBalance = 100;

    public static void main(String[] args) throws InterruptedException {
        whatIsALock();
        whyWeNeedLocks();
        lockBasedVsLockFree();
        sharedVsExclusive();
        roadmap();
    }

    static void whatIsALock() {
        System.out.println("=== 1) What is a lock? ===");
        System.out.println("""
                A lock is a door key for a critical section (shared data).

                  Thread A gets the key → enters room → changes data → leaves → returns key
                  Thread B waits outside until key is free

                Without a lock:
                  A and B both change the same variable → race / wrong result

                With a lock:
                  Changes happen one-at-a-time (or readers together, writers alone)
                """);
    }

    static void whyWeNeedLocks() throws InterruptedException {
        System.out.println("=== 2) Demo: shared money without lock (race) ===");
        unsafeBalance = 100;

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                unsafeBalance++; // not protected
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                unsafeBalance--; // not protected
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("  Expected balance: 100");
        System.out.println("  Actual balance:   " + unsafeBalance + "  ❌ (race — need a lock)");
        System.out.println();
    }

    static void lockBasedVsLockFree() {
        System.out.println("=== 3) Lock-based vs Lock-free ===");
        System.out.println("""
                LOCK-BASED
                  • Thread parks / waits if lock busy
                  • Clear critical section
                  • Examples: synchronized, ReentrantLock, ReadWriteLock

                LOCK-FREE (CAS)
                  • Thread never "holds a lock"
                  • Retry update if another thread changed the value first
                  • Examples: AtomicInteger.incrementAndGet(), ConcurrentHashMap

                Both are valid. Lock-free often faster for simple counters;
                locks clearer for complex multi-step logic.
                """);
    }

    static void sharedVsExclusive() {
        System.out.println("=== 4) Shared lock vs Exclusive lock ===");
        System.out.println("""
                EXCLUSIVE (write lock)
                  • Only ONE thread holds it
                  • Used when modifying data
                  • Example: ReentrantLock.lock(), ReadWriteLock.writeLock()

                SHARED (read lock)
                  • MANY threads can hold it together
                  • Used when only READING
                  • Example: ReadWriteLock.readLock()

                Rule of thumb:
                  many readers + rare writers  → ReadWriteLock / StampedLock
                  simple mutual exclusion      → synchronized / ReentrantLock
                """);
    }

    static void roadmap() {
        System.out.println("=== 5) Next lessons (one file each) ===");
        System.out.println("""
                27g  synchronized          — built-in monitor lock
                27h  ReentrantLock         — explicit Lock API (tryLock, unlock)
                27i  ReadWriteLock         — shared + exclusive
                27j  StampedLock           — optimistic reads + stamps
                27k  Semaphore             — N permits (not just 0/1)
                27l  Lock-free CAS         — Atomic / compareAndSet
                27m  Condition + fair lock — wait/signal, fairness
                """);
    }
}
