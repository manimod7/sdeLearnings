package threads;

/**
 * Demonstrates creating a thread by extending {@link Thread}.
 */
public class ThreadExample extends Thread {
    public ThreadExample(String name) {
        super(name);
    }

    @Override
    public void run() {
        System.out.println(getName() + " is running");
    }

    public static void main(String[] args) {
        ThreadExample thread = new ThreadExample("MyThread");
        thread.start();
    }
}
