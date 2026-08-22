// Lesson 27k: Semaphore — N permits (not just 0/1 like a mutex)
// Compile: javac Lesson27k_Semaphore.java
// Run:     java Lesson27k_Semaphore
//
// After: Lesson 27j (StampedLock)
//
// Semaphore(3) = at most 3 threads inside the "room" at once
// acquire() = take a permit (wait if 0 left)
// release() = return a permit

import java.util.concurrent.Semaphore;

public class Lesson27k_Semaphore {

    // Only 2 parking spots
    static final Semaphore parking = new Semaphore(2);

    public static void main(String[] args) throws InterruptedException {
        explain();
        parkingLotDemo();
        binarySemaphoreNote();
    }

    static void explain() {
        System.out.println("=== What is Semaphore? ===");
        System.out.println("""
                Lock / synchronized     → usually 1 thread (mutex)
                Semaphore(N)            → up to N threads at once

                acquire() → permits--
                release() → permits++

                Use: connection pool, rate limit, "max 5 downloads"
                """);
    }

    static void parkingLotDemo() throws InterruptedException {
        System.out.println("=== Demo: parking lot (2 spots, 5 cars) ===");

        Thread[] cars = new Thread[5];
        for (int i = 1; i <= 5; i++) {
            int id = i;
            cars[i - 1] = new Thread(() -> {
                try {
                    System.out.println("  Car-" + id + " waiting (available="
                            + parking.availablePermits() + ")");
                    parking.acquire(); // wait for a free spot
                    System.out.println("  Car-" + id + " PARKED");
                    Thread.sleep(300); // stay parked
                    System.out.println("  Car-" + id + " left");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    parking.release();
                }
            }, "Car-" + id);
            cars[i - 1].start();
        }
        for (Thread car : cars) {
            car.join();
        }
        System.out.println("  All cars done. permits left=" + parking.availablePermits());
        System.out.println();
    }

    static void binarySemaphoreNote() {
        System.out.println("=== Binary semaphore ===");
        System.out.println("""
                Semaphore(1) ≈ a mutex (exclusive lock)
                But prefer ReentrantLock / synchronized for normal mutual exclusion.
                Use Semaphore when you need N > 1.

                Next: Lock-free CAS (Lesson 27l)
                """);
    }
}
