package systemdesign.ratelimiter;

/**
 * Simple fixed window rate limiter.
 */
public class RateLimiter {
    private final int maxRequests;
    private final long windowMillis;
    private int requestCount;
    private long windowStart;

    public RateLimiter(int maxRequests, long windowMillis) {
        this.maxRequests = maxRequests;
        this.windowMillis = windowMillis;
        this.windowStart = System.currentTimeMillis();
    }

    public synchronized boolean allowRequest() {
        long now = System.currentTimeMillis();
        if (now - windowStart >= windowMillis) {
            requestCount = 0;
            windowStart = now;
        }
        if (requestCount < maxRequests) {
            requestCount++;
            return true;
        }
        return false;
    }
}
