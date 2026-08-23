// Lesson 25j: Inter-thread communication — wait / notify / notifyAll
// Compile: javac LessonConsole.java Lesson25j_ThreadCommunication.java
// Run:     java Lesson25j_ThreadCommunication
//
// After: Lesson 25i | Also see 25g (brief), 27m (Condition)
//
// ========== CHEAT SHEET ==========
// Threads share heap but need signals to coordinate (not busy-spin forever)
// wait()     → release lock + sleep until notify (must be inside synchronized)
// notify()   → wake ONE waiter on same lock
// notifyAll()→ wake ALL waiters (safer when multiple consumers)
// Always: while (condition) wait();  NOT if — handles spurious wakeup

public class Lesson25j_ThreadCommunication {

    static final Object lock = new Object();
    static volatile String message = null; // volatile so busy-wait demo can see main's write

    public static void main(String[] args) throws InterruptedException {
        whyCommunication();
        problemBusyWait();
        solutionWaitNotify();
        notifyAllDemo();
        rulesAndModernAlternatives();
        summary();
    }

    static void whyCommunication() {
        LessonConsole.heading("=== 0) Why inter-thread communication? ===");
        System.out.println("""
                Thread A produces data → Thread B must wait until ready

                Bad:  while (message == null) { }  → burns CPU (busy-wait)
                Good: wait() / notify() under same lock → B sleeps until A signals
                """);
    }

    static void problemBusyWait() throws InterruptedException {
        LessonConsole.heading("=== PROBLEM: busy-wait (spin loop) ===");
        message = null;

        Thread spinner = new Thread(() -> {
            int spins = 0;
            while (message == null) {
                spins++;
                if (spins > 10_000_000) break; // safety cap for demo
            }
            if (message != null) {
                System.out.println("  spinner saw message after " + spins + " spins");
            }
        }, "Spinner");

        spinner.start();
        sleep(100);
        message = "Hello";
        spinner.join();
        System.out.println("  Wasted CPU spinning instead of sleeping");
        System.out.println();
    }

    static void solutionWaitNotify() throws InterruptedException {
        LessonConsole.heading("=== SOLUTION: wait() / notify() ===");
        message = null;

        Thread consumer = new Thread(() -> {
            synchronized (lock) {
                while (message == null) {          // while, not if!
                    try {
                        System.out.println("  Consumer: waiting (WAITING state)...");
                        lock.wait();               // release lock + sleep
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                System.out.println("  Consumer got: " + message);
            }
        }, "Consumer");

        consumer.start();
        sleep(200);

        synchronized (lock) {
            message = "Hello from producer";
            System.out.println("  Producer: set message + notify()");
            lock.notify(); // wake consumer
        }

        consumer.join();
        message = null;
        System.out.println("  ✅ consumer slept until producer signaled");
        System.out.println();
    }

    static void notifyAllDemo() throws InterruptedException {
        LessonConsole.heading("=== 2) notifyAll — multiple waiters ===");
        final int[] ready = {0};

        Runnable waiter = () -> {
            synchronized (lock) {
                while (ready[0] == 0) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                System.out.println("  " + Thread.currentThread().getName() + " woke up");
            }
        };

        Thread w1 = new Thread(waiter, "Waiter-1");
        Thread w2 = new Thread(waiter, "Waiter-2");
        w1.start();
        w2.start();
        sleep(100);

        synchronized (lock) {
            ready[0] = 1;
            System.out.println("  Producer: notifyAll()");
            lock.notifyAll(); // wake BOTH — notify() might leave one sleeping
        }

        w1.join();
        w2.join();
        ready[0] = 0;
        System.out.println();
    }

    static void rulesAndModernAlternatives() {
        LessonConsole.heading("=== 3) Rules + modern alternatives ===");
        System.out.println("""
                Rules:
                  • wait/notify must be in synchronized(lock) on SAME object
                  • always while (cond) wait(); — spurious wakeup
                  • calling wait releases the monitor; re-acquires before return

                Modern (prefer for new code):
                  BlockingQueue       → producer/consumer
                  CountDownLatch      → wait for N threads (25g)
                  Condition (Lock)    → multiple wait sets (27m)
                  CompletableFuture   → async pipeline (27b)
                """);
    }

    static void summary() {
        LessonConsole.heading("=== Summary: Inter-thread communication ===");
        System.out.println("""
                wait()      → sleep until signaled (releases lock)
                notify()    → wake one waiter
                notifyAll() → wake all waiters
                Also see:   Lesson 25 (join), 25g, 27m Condition
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
