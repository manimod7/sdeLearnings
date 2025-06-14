package threads;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Illustrates using {@link java.util.concurrent.locks.ReadWriteLock} to allow multiple readers but exclusive writers.
 */
public class ReadWriteLockDemo {
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private int value = 0;

    public void write(int newValue) {
        rwLock.writeLock().lock();
        try {
            value = newValue;
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public int read() {
        rwLock.readLock().lock();
        try {
            return value;
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReadWriteLockDemo demo = new ReadWriteLockDemo();

        Thread writer = new Thread(() -> demo.write(42));
        Thread reader = new Thread(() -> System.out.println("Read value: " + demo.read()));
        writer.start();
        reader.start();
        writer.join();
        reader.join();
    }
}
