package threads;

/**
 * Demonstrates creating a thread using the {@link Runnable} interface.
 */
public class RunnableExample implements Runnable {
    @Override
    public void run() {
        System.out.println("Runnable task is running");
    }

    public static void main(String[] args) {
        Thread t = new Thread(new RunnableExample());
        t.start();
    }
}
