// Demo: main exits first — user thread keeps running (JVM waits)
// Compile: javac Lesson25k_UserThreadAfterMain.java
// Run:     java Lesson25k_UserThreadAfterMain

public class Lesson25k_UserThreadAfterMain {

    public static void main(String[] args) throws InterruptedException {
        Thread worker = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                sleep(1000);
                System.out.println("[worker] tick " + i);
            }
            System.out.println("[worker] done");
        }, "Worker");

        worker.start(); // normal thread (NOT daemon)

        sleep(1500);
        System.out.println("[main] exiting at ~1.5s");
        // main ends here — JVM stays alive until worker finishes
    }

    static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
