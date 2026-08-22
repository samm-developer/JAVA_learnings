// Lesson 27h: ReentrantLock — explicit Lock API
// Compile: javac Lesson27h_ReentrantLock.java
// Run:     java Lesson27h_ReentrantLock
//
// After: Lesson 27g (synchronized)
//
// Same idea as synchronized, but YOU call lock() / unlock()
// Extra powers: tryLock(), lockInterruptibly(), fairness

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Lesson27h_ReentrantLock {

    static final Lock lock = new ReentrantLock();
    static int balance = 100;

    public static void main(String[] args) throws Exception {
        basicLockUnlock();
        tryLockDemo();
        reentrantAgain();
        vsSynchronized();
    }

    static void basicLockUnlock() throws InterruptedException {
        System.out.println("=== 1) lock() / unlock() — ALWAYS unlock in finally ===");
        balance = 100;

        Runnable deposit = () -> {
            for (int i = 0; i < 10_000; i++) {
                lock.lock();
                try {
                    balance++;
                } finally {
                    lock.unlock(); // must run even if exception
                }
            }
        };
        Runnable withdraw = () -> {
            for (int i = 0; i < 10_000; i++) {
                lock.lock();
                try {
                    balance--;
                } finally {
                    lock.unlock();
                }
            }
        };

        Thread t1 = new Thread(deposit);
        Thread t2 = new Thread(withdraw);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("  balance=" + balance + "  ✅ (expected 100)");
        System.out.println();
    }

    static void tryLockDemo() throws InterruptedException {
        System.out.println("=== 2) tryLock() — don't wait forever ===");

        Lock demo = new ReentrantLock();
        demo.lock(); // main holds the lock

        Thread other = new Thread(() -> {
            try {
                if (demo.tryLock(200, TimeUnit.MILLISECONDS)) {
                    try {
                        System.out.println("  Other got lock");
                    } finally {
                        demo.unlock();
                    }
                } else {
                    System.out.println("  Other: tryLock timed out — did other work instead ✅");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        other.start();
        other.join();
        demo.unlock();
        System.out.println();
    }

    static void reentrantAgain() {
        System.out.println("=== 3) Reentrant = same thread can lock twice ===");
        ReentrantLock rl = new ReentrantLock();
        rl.lock();
        try {
            System.out.println("  hold count=" + rl.getHoldCount());
            rl.lock();
            try {
                System.out.println("  hold count=" + rl.getHoldCount() + " (locked again)");
            } finally {
                rl.unlock();
            }
        } finally {
            rl.unlock();
        }
        System.out.println("  hold count after all unlocks=" + rl.getHoldCount());
        System.out.println();
    }

    static void vsSynchronized() {
        System.out.println("=== 4) synchronized vs ReentrantLock ===");
        System.out.println("""
                                   synchronized          ReentrantLock
                Unlock             automatic             unlock() in finally
                tryLock            no                    yes
                interrupt wait     no                    lockInterruptibly()
                fair queue         no                    new ReentrantLock(true)
                Condition/wait     wait/notify           lock.newCondition()

                Use synchronized for simple cases.
                Use ReentrantLock when you need tryLock / fairness / Condition.
                """);
    }
}
