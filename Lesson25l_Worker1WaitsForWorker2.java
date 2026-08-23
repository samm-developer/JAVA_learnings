// Demo: Worker-1 waits until Worker-2 finishes (join)
// Compile: javac Lesson25l_Worker1WaitsForWorker2.java
// Run:     java Lesson25l_Worker1WaitsForWorker2

public class Lesson25l_Worker1WaitsForWorker2 {

    public static void main(String[] args) throws InterruptedException {
        Thread worker2 = new Thread(() -> {
            for (int i = 1; i <= 3; i++) {
                sleep(1000);
                System.out.println("[Worker-2] step " + i);
            }
            System.out.println("[Worker-2] finished");
        }, "Worker-2");

        Thread worker1 = new Thread(() -> {
            System.out.println("[Worker-1] started — waiting for Worker-2...");
            try {
                worker2.join(); // STOP here until Worker-2 finishes
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            System.out.println("[Worker-1] Worker-2 done — now I run");
            for (int i = 1; i <= 2; i++) {
                sleep(500);
                System.out.println("[Worker-1] step " + i);
            }
            System.out.println("[Worker-1] finished");
        }, "Worker-1");

        worker2.start();
        worker1.start();

        worker1.join(); // main waits for Worker-1 (optional — so main prints last)
        System.out.println("[main] all done");
    }

    static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
