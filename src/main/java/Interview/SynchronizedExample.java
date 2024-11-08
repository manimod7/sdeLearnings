package Interview;
class Counter {
    private int count = 0;

    // Synchronized method to increment the counter and print its value
    public synchronized void increment(String threadName) {
        count++;
        System.out.println("In " + threadName + ", counter=" + count);
    }

    public int getCount() {
        return count;
    }
}

public class SynchronizedExample {
    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();

        // Create two threads that increment the counter
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                counter.increment("thread1");
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 15; i++) {
                counter.increment("thread2");
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("Final Counter Value: " + counter.getCount());
    }
}
