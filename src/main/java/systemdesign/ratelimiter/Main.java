package systemdesign.ratelimiter;

/**
 * Demonstrates RateLimiter usage.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        RateLimiter limiter = new RateLimiter(2, 1000);
        for (int i = 0; i < 5; i++) {
            System.out.println(limiter.allowRequest());
            Thread.sleep(300);
        }
    }
}
