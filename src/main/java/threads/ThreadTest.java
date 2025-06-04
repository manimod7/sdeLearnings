package threads;

public class ThreadTest  extends Thread {
        private String name;

        public ThreadTest(String name) {
            super(name);  // sets Thread’s name
            this.name = name;
        }

        @Override
        public void run() {
            // This code runs when .start() is invoked
            for (int i = 1; i <= 5; i++) {
                System.out.printf("[%s] Count: %d%n", name, i);
                try {
                    Thread.sleep(500); // pause 500ms
                } catch (InterruptedException e) {
                    System.out.printf("[%s] Interrupted!%n", name);
                    return;
                }
            }
            System.out.printf("[%s] Finished.%n", name);
        }

        public static void main(String[] args) throws InterruptedException {
            ThreadTest t1 = new ThreadTest("T1");
            ThreadTest t2 = new ThreadTest("T2");

            t1.start(); // Internally, this creates a new OS thread (or JVM thread)
            t2.start();
            Thread.sleep(5000);
            t1.start();

            // main thread continues here
            System.out.println("Main thread is done launching t1 & t2.");
        }
    }
