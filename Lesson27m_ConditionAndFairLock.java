// Lesson 27m: Condition + Fair locks (+ quick map of other locks)
// Compile: javac Lesson27m_ConditionAndFairLock.java
// Run:     java Lesson27m_ConditionAndFairLock
//
// After: Lesson 27l (CAS)
//
// Condition = wait/signal with Lock (like Object.wait/notify, but multiple conditions)
// Fair lock = longest-waiting thread gets lock next (no barging)

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Lesson27m_ConditionAndFairLock {

    static final Lock lock = new ReentrantLock();
    static final Condition notEmpty = lock.newCondition();
    static final Queue<String> queue = new LinkedList<>();

    public static void main(String[] args) throws InterruptedException {
        conditionProducerConsumer();
        fairVsUnfair();
        otherLocksMap();
    }

    static void conditionProducerConsumer() throws InterruptedException {
        System.out.println("=== 1) Condition — producer / consumer ===");

        Thread consumer = new Thread(() -> {
            lock.lock();
            try {
                while (queue.isEmpty()) {
                    System.out.println("  Consumer await (queue empty)...");
                    notEmpty.await(); // like wait() — releases lock until signal
                }
                System.out.println("  Consumer got: " + queue.poll());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }, "Consumer");

        Thread producer = new Thread(() -> {
            sleep(150);
            lock.lock();
            try {
                queue.offer("task-1");
                System.out.println("  Producer put task-1 + signal");
                notEmpty.signal(); // like notify()
            } finally {
                lock.unlock();
            }
        }, "Producer");

        consumer.start();
        producer.start();
        consumer.join();
        producer.join();
        System.out.println();
    }

    static void fairVsUnfair() throws InterruptedException {
        System.out.println("=== 2) Fair vs unfair ReentrantLock ===");

        // fair=true → roughly FIFO among waiters
        ReentrantLock fair = new ReentrantLock(true);
        ReentrantLock unfair = new ReentrantLock(false); // default

        System.out.println("  fair.isFair()   = " + fair.isFair());
        System.out.println("  unfair.isFair() = " + unfair.isFair());
        System.out.println("""
                  Unfair (default): faster, a new thread may barge ahead of waiters
                  Fair:             slower, waiters served in order

                Prefer unfair unless starvation is a real problem.
                """);
    }

    static void otherLocksMap() {
        System.out.println("=== 3) Full lock toolkit (your series) ===");
        System.out.println("""
                Lesson  What                         Kind
                ------  ----                         ----
                27f     What is a lock               concepts
                27g     synchronized                 exclusive, auto unlock
                27h     ReentrantLock                exclusive, explicit
                27i     ReadWriteLock                shared + exclusive
                27j     StampedLock                  optimistic + stamps
                27k     Semaphore                    N permits
                27l     CAS / Atomic*                lock-free
                27m     Condition + fair lock        wait/signal, fairness

                Related (already / later):
                  CountDownLatch   — wait for N events (25g)
                  CyclicBarrier    — N threads wait for each other
                  Phaser           — flexible barrier phases
                  Object.wait/notify — older Condition-style API

                Pick quickly:
                  simple mutex          → synchronized / ReentrantLock
                  many readers          → ReadWriteLock / StampedLock
                  limit concurrency N   → Semaphore
                  counter / flag        → Atomic* / volatile
                  producer-consumer     → Condition or BlockingQueue
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
