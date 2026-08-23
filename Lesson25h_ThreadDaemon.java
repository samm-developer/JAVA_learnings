// Lesson 25h: Daemon threads
// Compile: javac LessonConsole.java Lesson25h_ThreadDaemon.java
// Run:     java Lesson25h_ThreadDaemon
//
// After: Lesson 25 (Threads basics), 25g (deep dive)
// Study: 25h Daemon → 25i Priority → 25j Communication
//
// ========== CHEAT SHEET ==========
// User thread (default)  → JVM keeps running until ALL user threads finish
// Daemon thread          → setDaemon(true) BEFORE start()
//                        → JVM exits when only daemon threads remain
// Use for: background helpers (auto-save, heartbeat) — must not hold critical work alone

public class Lesson25h_ThreadDaemon {

    public static void main(String[] args) throws InterruptedException {
        userVsDaemonConcept();
        problemUserThreadBlocksExit();
        solutionDaemonThread();
        daemonRules();
        summary();
    }

    static void userVsDaemonConcept() {
        LessonConsole.heading("=== 0) User thread vs daemon thread ===");
        System.out.println("""
                User thread (default):
                  main starts worker → worker still running → JVM STAYS ALIVE

                Daemon thread:
                  setDaemon(true) before start()
                  when only daemons left → JVM EXITS (daemons are stopped abruptly)

                main thread is always a user thread.
                """);
    }

    static void problemUserThreadBlocksExit() throws InterruptedException {
        LessonConsole.heading("=== PROBLEM: user thread keeps JVM alive ===");
        System.out.println("  Starting background user thread (5s job)...");

        Thread background = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                sleep(1000);
                System.out.println("  [user] background tick " + i);
            }
            System.out.println("  [user] background finished");
        }, "Background-User");
        // default isDaemon = false (user thread)

        background.start();
        System.out.println("  main done its work — but must wait for user thread ❌");
        background.join(); // without join, main would exit only after background finishes anyway
        System.out.println("  JVM waited because user thread was still alive");
        System.out.println();
    }

    static void solutionDaemonThread() throws InterruptedException {
        LessonConsole.heading("=== SOLUTION: daemon thread — JVM won't wait forever ===");
        System.out.println("""
                  Timeline (this demo):
                    0.0s  daemon starts (planned 10 ticks, 1s apart)
                    1.0s  daemon prints tick 1
                    1.5s  main finishes → JVM exits → daemon KILLED mid-work
                    2.0s  tick 2 never runs ❌

                  Seeing ONLY tick 1 is correct ✅ — daemon did not get to finish.
                """);

        Thread daemon = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                sleep(1000);
                System.out.println("  [daemon] tick " + i + "  (at ~" + i + "s)");
            }
            System.out.println("  [daemon] finished all 10 ticks"); // never reached
        }, "Background-Daemon");
        daemon.setDaemon(true); // MUST be before start()
        daemon.start();

        sleep(1500);
        System.out.println("  [main] exiting at ~1.5s — JVM stops daemon (only daemons left)");
        // no join() — main ends here → JVM shutdown → daemon thread terminated
        System.out.println();
    }

    static void daemonRules() {
        LessonConsole.heading("=== 2) Rules ===");
        System.out.println("""
                setDaemon(true) only BEFORE start() — otherwise IllegalThreadStateException
                Cannot set current thread (main) to daemon

                Good daemon use:  GC, JVM housekeeping, optional background cleanup
                Bad daemon use:    must-finish save/upload — use user thread + join/shutdown hook
                """);
    }

    static void summary() {
        LessonConsole.heading("=== Summary: Daemon threads ===");
        System.out.println("""
                User thread   → JVM waits for it to finish
                Daemon thread → JVM exits without waiting (abrupt stop)
                Next:         Lesson 25i Thread priority
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
