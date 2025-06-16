package threads;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
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
        fixedPool.execute(runnableTask); // execute without expecting a result
        Future<String> future = fixedPool.submit(callableTask); // submit returns a Future
        System.out.println("Result from future: " + future.get());
        fixedPool.shutdown();
        fixedPool.awaitTermination(1, TimeUnit.SECONDS);
        System.out.println("isShutdown: " + fixedPool.isShutdown());
        System.out.println("isTerminated: " + fixedPool.isTerminated());

        // Using a single thread executor
        ExecutorService singleThread = Executors.newSingleThreadExecutor();
        singleThread.submit(runnableTask);
        System.out.println("invokeAny result: " + singleThread.invokeAny(Arrays.asList(callableTask)));
        singleThread.shutdownNow();

        // Using a cached thread pool
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.submit(callableTask);
        cachedPool.shutdown();
        cachedPool.awaitTermination(1, TimeUnit.SECONDS);

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
        System.out.println("invokeAny result: " + pool.invokeAny(tasks));
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.SECONDS);

        // Using a scheduled executor service
        ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1);
        ScheduledFuture<?> handle = scheduled.schedule(() -> System.out.println("Delayed task"),
                1, TimeUnit.SECONDS);
        scheduled.scheduleAtFixedRate(() -> System.out.println("Periodic task"), 0, 500, TimeUnit.MILLISECONDS);
        handle.get();
        scheduled.shutdown();
        scheduled.awaitTermination(1, TimeUnit.SECONDS);
    }
}
