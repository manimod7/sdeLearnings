package threads;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Demonstrates using {@link Callable} to produce a result from a thread.
 */
public class CallableExample implements Callable<String> {
    @Override
    public String call() {
        return "Callable result";
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CallableExample callable = new CallableExample();
        FutureTask<String> future = new FutureTask<>(callable);
        Thread thread = new Thread(future);
        thread.start();
        System.out.println(future.get());
    }
}
