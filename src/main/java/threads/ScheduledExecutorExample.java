package threads;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Demonstrates {@link ScheduledExecutorService} methods for delayed and
 * periodic execution. Each scheduling variant is shown along with cancelling
 * the returned {@link ScheduledFuture}.
 */
public class ScheduledExecutorExample {

    public static void main(String[] args) throws InterruptedException {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Single execution after a delay
        Runnable oneTime = () -> System.out.println("Delayed task executed");
        ScheduledFuture<?> single = scheduler.schedule(oneTime, 1, TimeUnit.SECONDS);

        // Repeated execution at a fixed rate
        Runnable rateTask = () -> System.out.println("fixedRate " + System.currentTimeMillis());
        ScheduledFuture<?> rateFuture = scheduler.scheduleAtFixedRate(rateTask, 0, 500, TimeUnit.MILLISECONDS);

        // Repeated execution with a fixed delay between completions
        Runnable delayTask = () -> System.out.println("fixedDelay " + System.currentTimeMillis());
        ScheduledFuture<?> delayFuture = scheduler.scheduleWithFixedDelay(delayTask, 0, 500, TimeUnit.MILLISECONDS);

        // Let the tasks run for a short period
        TimeUnit.SECONDS.sleep(2);

        // Cancel periodic tasks
        rateFuture.cancel(true);
        delayFuture.cancel(true);

        // Wait for the one-time task to complete
        try {
            single.get();
        } catch (java.util.concurrent.ExecutionException e) {
            System.err.println("Error executing scheduled task: " + e.getMessage());
        }

        // Shut down the scheduler gracefully
        scheduler.shutdown();
        scheduler.awaitTermination(1, TimeUnit.SECONDS);
        System.out.println("Scheduler terminated: " + scheduler.isTerminated());
    }
}
