package threads;

import java.util.concurrent.TimeUnit;

/**
 * Shows common lifecycle methods of {@link Thread} such as starting,
 * joining and interrupting. Each worker prints messages so the order
 * of operations is easy to follow.
 */
public class ThreadMethodsDemo {

    private static class Worker extends Thread {
        Worker(String name) {
            super(name);           // sets the thread's name
        }

        @Override
        public void run() {
            try {
                for (int i = 1; i <= 3; i++) {
                    System.out.printf("[%s] iteration %d%n", getName(), i);
                    TimeUnit.MILLISECONDS.sleep(200);
                }
            } catch (InterruptedException e) {
                System.out.printf("[%s] interrupted%n", getName());
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Worker("worker-1");
        t1.setPriority(Thread.NORM_PRIORITY + 1);    // adjust priority

        t1.start();          // start background work
        System.out.println("t1 alive? " + t1.isAlive());

        // Wait for completion of t1
        t1.join();
        System.out.println("t1 alive after join? " + t1.isAlive());

        // Demonstrate interrupt
        Thread t2 = new Worker("interruptible");
        t2.start();
        TimeUnit.MILLISECONDS.sleep(300);
        t2.interrupt();
    }
}
