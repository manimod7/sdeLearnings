package threads.forkjoin;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;

/**
 * Demonstrates how to use {@link ForkJoinPool} with both {@link java.util.concurrent.RecursiveAction}
 * and {@link java.util.concurrent.RecursiveTask}.
 */
public class ForkJoinDemo {
    public static void main(String[] args) {
        ForkJoinPool pool = ForkJoinPool.commonPool();

        // RecursiveAction example
        int[] numbers = new int[10_000];
        Arrays.fill(numbers, 1);
        SumRecursiveAction action = new SumRecursiveAction(numbers, 0, numbers.length);
        pool.invoke(action);
        System.out.println("Sum via RecursiveAction: " + numbers[0]);

        // RecursiveTask example
        int n = 10;
        FibonacciRecursiveTask fib = new FibonacciRecursiveTask(n);
        int result = pool.invoke(fib);
        System.out.println("Fibonacci(" + n + ") via RecursiveTask: " + result);
    }
}
