// Lesson 25 Mini Practice — using a Thread subclass
// Compile: javac Lesson25_MiniPractice.java
// Run:     java Lesson25_MiniPractice

public class Lesson25_MiniPractice {
    public static void main(String[] args) throws InterruptedException {

        HelloThread t1 = new HelloThread("Alpha");
        HelloThread t2 = new HelloThread("Beta");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Both threads finished.");
    }
}

// Custom thread class
class HelloThread extends Thread {
    private final String label;

    HelloThread(String label) {
        super(label); // thread name
        this.label = label;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            System.out.println("Hello from " + label + " (" + i + ")");
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                interrupt();
            }
        }
    }
}
