// Lesson 26 Mini Practice: BankAccount + synchronized
// Compile: javac Lesson26_MiniPractice.java
// Run:     java Lesson26_MiniPractice

public class Lesson26_MiniPractice {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== WITHOUT synchronized (may be WRONG) ===");
        runTest(false);

        System.out.println();
        System.out.println("=== WITH synchronized (should be correct) ===");
        runTest(true);
    }

    static void runTest(boolean useLock) throws InterruptedException {
        BankAccount account = new BankAccount(useLock);
        final int TIMES = 1000; // each thread deposits 1, TIMES times

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < TIMES; i++) {
                account.deposit(1);
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < TIMES; i++) {
                account.deposit(1);
            }
        });

        long start = System.nanoTime();

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        long end = System.nanoTime();
        double timeMs = (end - start) / 1_000_000.0;

        System.out.println("Final balance: " + account.getBalance());
        System.out.println("Expected:      " + (TIMES * 2)); // 2000
        System.out.println("Time taken:    " + timeMs + " ms");
    }
}

class BankAccount {
    private int balance = 0;
    private final boolean useLock;

    BankAccount(boolean useLock) {
        this.useLock = useLock;
    }

    void deposit(int amount) {
        if (useLock) {
            synchronized (this) {
                balance += amount;
            }
        } else {
            balance += amount; // NOT thread-safe
        }
    }

    int getBalance() {
        return balance;
    }
}
