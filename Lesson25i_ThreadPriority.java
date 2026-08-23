// Lesson 25i: Thread priority
// Compile: javac LessonConsole.java Lesson25i_ThreadPriority.java
// Run:     java Lesson25i_ThreadPriority
//
// After: Lesson 25h (Daemon)
//
// ========== CHEAT SHEET ==========
// Priority range: Thread.MIN_PRIORITY(1) … NORM(5) … MAX_PRIORITY(10)
// setPriority(n) BEFORE or after start (on live thread) — hint to OS scheduler
// WARNING: modern OS often ignore Java priority — do NOT rely on it for correctness
// Use: ExecutorService, locks, queues — not priority — for fair/ordered behavior

public class Lesson25i_ThreadPriority {

    public static void main(String[] args) throws InterruptedException {
        priorityScale();
        problemExpectingStrictOrder();
        priorityDemo();
        dontRelyOnPriority();
        summary();
    }

    static void priorityScale() {
        LessonConsole.heading("=== 0) Priority scale ===");
        System.out.println("  MIN_PRIORITY  = " + Thread.MIN_PRIORITY);
        System.out.println("  NORM_PRIORITY = " + Thread.NORM_PRIORITY);
        System.out.println("  MAX_PRIORITY  = " + Thread.MAX_PRIORITY);
        System.out.println("  setPriority() = hint to scheduler — NOT a guarantee");
        System.out.println();
    }

    static void problemExpectingStrictOrder() {
        LessonConsole.heading("=== PROBLEM: assuming high priority always runs first ===");
        System.out.println("""
                Thread low  = new Thread(task);  low.setPriority(MIN_PRIORITY);
                Thread high = new Thread(task);  high.setPriority(MAX_PRIORITY);

                You might expect high ALWAYS wins — on Linux/macOS/Windows often it does NOT ❌
                OS time-slicing + CPU cores override Java priority hints
                """);
    }

    static void priorityDemo() throws InterruptedException {
        LessonConsole.heading("=== DEMO: priority hint (may vary on your OS) ===");
        Runnable counter = () -> {
            long sum = 0;
            for (long i = 0; i < 50_000_000L; i++) {
                sum += i;
            }
            System.out.println("  " + Thread.currentThread().getName()
                    + " priority=" + Thread.currentThread().getPriority()
                    + " done sum=" + sum);
        };

        Thread low = new Thread(counter, "Low-Priority");
        Thread high = new Thread(counter, "High-Priority");
        low.setPriority(Thread.MIN_PRIORITY);
        high.setPriority(Thread.MAX_PRIORITY);

        high.start();
        low.start();
        low.join();
        high.join();
        System.out.println("  finish order varies — priority is hint only");
        System.out.println();
    }

    static void dontRelyOnPriority() {
        LessonConsole.heading("=== 2) What to use instead ===");
        System.out.println("""
                Need ordering?        → ExecutorService single thread / BlockingQueue
                Need fairness?        → ReentrantLock(true) fair mode (27m)
                Need urgent tasks?    → separate thread pool, not MAX_PRIORITY

                Real-time systems may map priorities better — still test on target OS.
                """);
    }

    static void summary() {
        LessonConsole.heading("=== Summary: Thread priority ===");
        System.out.println("""
                setPriority(1–10)  → scheduler hint, not contract
                Never use priority for program correctness
                Next:              Lesson 25j Inter-thread communication (wait/notify)
                """);
    }
}
