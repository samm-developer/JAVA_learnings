// Lesson 27 Mini Practice: BankAccount with AtomicInteger (no synchronized)
// Compile: javac Lesson27_MiniPractice.java
// Run:     java Lesson27_MiniPractice

import java.util.concurrent.atomic.AtomicInteger;

public class Lesson27_MiniPractice {
    public static void main(String[] args) throws InterruptedException {
        AtomicBankAccount account = new AtomicBankAccount();
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

        double timeMs = (System.nanoTime() - start) / 1_000_000.0;

        System.out.println("Final balance: " + account.getBalance());
        System.out.println("Expected:      " + (TIMES * 2)); // 2000
        System.out.println("Time taken:    " + timeMs + " ms");
        System.out.println("(No synchronized — AtomicInteger handles safety)");
    }
}

class AtomicBankAccount {
    // Thread-safe balance — no synchronized needed for deposit/get
    private final AtomicInteger balance = new AtomicInteger(0);

    void deposit(int amount) {
        balance.addAndGet(amount);
    }

    void withdraw(int amount) {
        balance.addAndGet(-amount);
    }

    int getBalance() {
        return balance.get();
    }
}
