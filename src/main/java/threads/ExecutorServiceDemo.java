package threads;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Demonstrates various ways to use {@link ExecutorService} with
 * {@link Runnable} and {@link Callable} tasks.
 */
public class ExecutorServiceDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Runnable runnableTask = () -> System.out.println("Runnable executed");

        Callable<String> callableTask = () -> {
            Thread.sleep(100);
            return "Callable result";
        };

        // Using a fixed thread pool
        ExecutorService fixedPool = Executors.newFixedThreadPool(2);
        fixedPool.execute(runnableTask); // returns no result
        Future<String> future = fixedPool.submit(callableTask); // returns a Future
        System.out.println(future.get());
        fixedPool.shutdown();

        // Using a single thread executor
        ExecutorService singleThread = Executors.newSingleThreadExecutor();
        singleThread.submit(runnableTask);
        singleThread.shutdown();

        // Using a cached thread pool
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.submit(callableTask);
        cachedPool.shutdown();

        // Using invokeAll with multiple callables
        ExecutorService pool = Executors.newFixedThreadPool(3);
        List<Callable<String>> tasks = Arrays.asList(
                () -> "task1",
                () -> "task2",
                () -> "task3"
        );
        List<Future<String>> results = pool.invokeAll(tasks);
        for (Future<String> r : results) {
            System.out.println(r.get());
        }
        pool.shutdown();

        // Using a scheduled executor service
        ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1);
        scheduled.schedule(() -> System.out.println("Delayed task"), 1, TimeUnit.SECONDS);
        scheduled.shutdown();
    }
}
