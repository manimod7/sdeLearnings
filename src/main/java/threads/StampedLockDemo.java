package threads;

import java.util.concurrent.locks.StampedLock;

/**
 * Example using {@link StampedLock} for optimistic reads.
 */
public class StampedLockDemo {
    private final StampedLock lock = new StampedLock();
    private int data = 0;

    public void write(int value) {
        long stamp = lock.writeLock();
        try {
            data = value;
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    public int read() {
        long stamp = lock.tryOptimisticRead();
        int result = data;
        if (!lock.validate(stamp)) {
            stamp = lock.readLock();
            try {
                result = data;
            } finally {
                lock.unlockRead(stamp);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        StampedLockDemo demo = new StampedLockDemo();
        demo.write(5);
        System.out.println("Read: " + demo.read());
    }
}
