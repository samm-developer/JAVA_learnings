// Lesson 25 Mini Practice
// Two threads print "Hello from X" five times each (with sleep)
// Compile: javac Lesson25_MiniPractice.java
// Run:     java Lesson25_MiniPractice

public class Lesson25_MiniPractice {
    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                System.out.println("Hello from Alpha (" + i + ")");
                sleep(250);
            }
        }, "Alpha");

        Thread t2 = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                System.out.println("Hello from Beta (" + i + ")");
                sleep(250);
            }
        }, "Beta");

        t1.start();
        t2.start();

        // Wait so main doesn't end too early
        t1.join();
        t2.join();

        System.out.println("Both threads finished.");
    }

    static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
