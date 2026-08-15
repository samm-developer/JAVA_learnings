// Lesson 25: Threads basics (run work in parallel)
// Compile: javac Lesson25_Threads.java
// Run:     java Lesson25_Threads

public class Lesson25_Threads {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Main thread starts: " + Thread.currentThread().getName());

        // ===== Way 1: Thread + lambda (Runnable) =====
        Thread t1 = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                System.out.println("Worker-A: " + i);
                sleep(300);
            }
        }, "Worker-A");

        // ===== Way 2: extend Thread =====
        Thread t2 = new MyWorker("Worker-B");

        t1.start(); // start() runs in a NEW thread
        t2.start();
        // t1.run(); // DON'T — that would run on main thread, not parallel

        // Main can do other work at the same time
        for (int i = 1; i <= 5; i++) {
            System.out.println("Main: " + i);
            sleep(200);
        }

        // Wait for workers to finish before exiting main
        t1.join();
        t2.join();

        System.out.println("All threads done.");
    }

    static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class MyWorker extends Thread {
    MyWorker(String name) {
        super(name); // thread name
    }

    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            System.out.println(getName() + ": " + i);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                interrupt();
            }
        }
    }
}
