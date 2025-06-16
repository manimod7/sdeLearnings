package threads;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * Demonstrates using {@link Future} and {@link FutureTask} to obtain results
 * from asynchronous computations. The example shows common methods such as
 * {@code get()}, {@code isDone()}, {@code cancel(boolean)} and
 * {@code isCancelled()}.
 */
public class FutureDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Submit a Callable to obtain a Future representing the pending result
        Callable<Integer> task = () -> {
            TimeUnit.MILLISECONDS.sleep(100);
            return 42;
        };
        Future<Integer> future = executor.submit(task);

        System.out.println("Future done? " + future.isDone());
        System.out.println("Result: " + future.get());
        System.out.println("Future cancelled? " + future.isCancelled());

        // Demonstrate Future.cancel()
        Future<Integer> toCancel = executor.submit(task);
        boolean cancelled = toCancel.cancel(true); // attempt to interrupt if running
        System.out.println("Cancelled successfully? " + cancelled);
        System.out.println("toCancel.isCancelled(): " + toCancel.isCancelled());

        // Using FutureTask directly with a Thread
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            TimeUnit.MILLISECONDS.sleep(50);
            return "FutureTask result";
        });
        Thread t = new Thread(futureTask);
        t.start();
        System.out.println("FutureTask.get(): " + futureTask.get());

        executor.shutdown();
    }
}
