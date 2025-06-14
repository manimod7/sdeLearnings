package threads;

import java.util.concurrent.Semaphore;

/**
 * Uses a {@link Semaphore} to limit concurrent access, behaving like a lock when only one permit is used.
 */
public class SemaphoreDemo {
    private final Semaphore semaphore = new Semaphore(1);
    private int count = 0;

    public void increment() throws InterruptedException {
        semaphore.acquire();
        try {
            count++;
        } finally {
            semaphore.release();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SemaphoreDemo demo = new SemaphoreDemo();
        Runnable task = () -> {
            try {
                demo.increment();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("Final count: " + demo.count);
    }
}
