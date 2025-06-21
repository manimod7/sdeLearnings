package threads.forkjoin;

import java.util.concurrent.RecursiveTask;

/**
 * RecursiveTask example that calculates Fibonacci numbers.
 * Returns the nth Fibonacci number using the fork/join framework.
 */
public class FibonacciRecursiveTask extends RecursiveTask<Integer> {
    private final int n;

    public FibonacciRecursiveTask(int n) {
        this.n = n;
    }

    @Override
    protected Integer compute() {
        if (n <= 1) {
            return n;
        }
        FibonacciRecursiveTask f1 = new FibonacciRecursiveTask(n - 1);
        f1.fork();
        FibonacciRecursiveTask f2 = new FibonacciRecursiveTask(n - 2);
        int result2 = f2.compute();
        int result1 = f1.join();
        return result1 + result2;
    }
}
