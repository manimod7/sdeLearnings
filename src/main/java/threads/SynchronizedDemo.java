package threads;

/**
 * Uses the {@code synchronized} keyword which relies on an intrinsic lock.
 */
public class SynchronizedDemo {
    private int count = 0;

    /**
     * Increments the counter in a thread-safe manner.
     */
    public synchronized void increment() {
        count++;
    }

    public static void main(String[] args) throws InterruptedException {
        SynchronizedDemo demo = new SynchronizedDemo();
        Runnable task = demo::increment;

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Final count: " + demo.count);
    }
}
