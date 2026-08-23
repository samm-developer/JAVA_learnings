// Lesson 25k: User (normal) thread keeps JVM alive after main exits
// Compile: javac LessonConsole.java Lesson25k_UserThreadKeepsJvmAlive.java
// Run:     java Lesson25k_UserThreadKeepsJvmAlive
//
// Compare with: Lesson 25h (daemon — JVM kills worker when main exits)
//
// ========== CHEAT SHEET ==========
// User thread (default, NOT setDaemon) → JVM stays alive until ALL user threads finish
// main() can end — JVM does NOT exit if another user thread is still running
// No join() needed on main for JVM to wait — JVM waits automatically for user threads

public class Lesson25k_UserThreadKeepsJvmAlive {

    public static void main(String[] args) throws InterruptedException {
        compareWithDaemon();
        demoUserThreadAfterMainExits();
        summary();
    }

    static void compareWithDaemon() {
        LessonConsole.heading("=== 0) Same setup as 25h — but NORMAL thread ===");
        System.out.println("""
                25h daemon demo:  main exits at 1.5s → only tick 1 → JVM kills daemon
                This file:        main exits at 1.5s → ticks 2..10 STILL run ✅

                Difference: setDaemon(true) vs default user thread (isDaemon=false)
                """);
    }

    static void demoUserThreadAfterMainExits() throws InterruptedException {
        LessonConsole.heading("=== DEMO: user thread keeps working after main exits ===");
        System.out.println("""
                  Timeline:
                    0.0s  user worker starts (10 ticks, 1s apart)
                    1.0s  tick 1
                    1.5s  main() ends — but JVM STAYS ALIVE
                    2.0s  tick 2  ← still runs!
                    ...
                   10.0s  tick 10 → worker done → JVM exits
                """);

        Thread worker = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                sleep(1000);
                System.out.println("  [user] tick " + i + "  (worker still running, isDaemon="
                        + Thread.currentThread().isDaemon() + ")");
            }
            System.out.println("  [user] finished all 10 ticks ✅");
        }, "Background-User");
        // NOT setDaemon(true) → normal user thread

        worker.start();

        sleep(1500);
        System.out.println("  [main] main() ending at ~1.5s — NO join() called");
        System.out.println("  [main] watch: worker continues below even though main is done");
        // main ends here — JVM keeps running because worker is a user thread
    }

    static void summary() {
        LessonConsole.heading("=== Summary ===");
        System.out.println("""
                User thread + main exits  →  JVM waits  →  worker finishes all work
                Daemon thread + main exits  →  JVM exits  →  worker killed (see 25h)

                Use user thread when work MUST complete (save, upload, cleanup).
                Compare: Lesson 25h_ThreadDaemon
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
