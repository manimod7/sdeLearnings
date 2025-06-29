package lld.ratelimiter;

/**
 * Immutable result object for rate limit decisions.
 * 
 * Contains all relevant information about a rate limit check,
 * including the decision, remaining quota, and reset time.
 * 
 * Design Pattern: Value Object Pattern
 * Benefits:
 * - Immutable state
 * - Clear data contract
 * - Thread-safe
 * - Comprehensive information
 */
public class RateLimitResult {
    private final boolean allowed;
    private final int remainingRequests;
    private final long resetTimeMillis;
    private final String reason;
    private final long windowStartMillis;
    
    public RateLimitResult(boolean allowed, int remainingRequests, long resetTimeMillis, 
                          String reason, long windowStartMillis) {
        this.allowed = allowed;
        this.remainingRequests = Math.max(0, remainingRequests);
        this.resetTimeMillis = resetTimeMillis;
        this.reason = reason != null ? reason : "";
        this.windowStartMillis = windowStartMillis;
    }
    
    /**
     * Creates a result for an allowed request.
     */
    public static RateLimitResult allowed(int remainingRequests, long resetTimeMillis, long windowStartMillis) {
        return new RateLimitResult(true, remainingRequests, resetTimeMillis, "Request allowed", windowStartMillis);
    }
    
    /**
     * Creates a result for a denied request.
     */
    public static RateLimitResult denied(long resetTimeMillis, String reason, long windowStartMillis) {
        return new RateLimitResult(false, 0, resetTimeMillis, reason, windowStartMillis);
    }
    
    /**
     * Creates a result for a denied request with default reason.
     */
    public static RateLimitResult denied(long resetTimeMillis, long windowStartMillis) {
        return denied(resetTimeMillis, "Rate limit exceeded", windowStartMillis);
    }
    
    // Getters
    public boolean isAllowed() { return allowed; }
    public int getRemainingRequests() { return remainingRequests; }
    public long getResetTimeMillis() { return resetTimeMillis; }
    public String getReason() { return reason; }
    public long getWindowStartMillis() { return windowStartMillis; }
    
    /**
     * Gets reset time in seconds (commonly used in HTTP headers).
     */
    public long getResetTimeSeconds() {
        return resetTimeMillis / 1000;
    }
    
    /**
     * Gets time until reset in milliseconds.
     */
    public long getTimeUntilResetMillis(long currentTimeMillis) {
        return Math.max(0, resetTimeMillis - currentTimeMillis);
    }
    
    /**
     * Gets time until reset in seconds.
     */
    public long getTimeUntilResetSeconds(long currentTimeMillis) {
        return getTimeUntilResetMillis(currentTimeMillis) / 1000;
    }
    
    @Override
    public String toString() {
        return String.format("RateLimitResult{allowed=%s, remaining=%d, resetTime=%d, reason='%s'}", 
                           allowed, remainingRequests, resetTimeMillis, reason);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        RateLimitResult that = (RateLimitResult) obj;
        return allowed == that.allowed &&
               remainingRequests == that.remainingRequests &&
               resetTimeMillis == that.resetTimeMillis &&
               windowStartMillis == that.windowStartMillis &&
               reason.equals(that.reason);
    }
    
    @Override
    public int hashCode() {
        int result = Boolean.hashCode(allowed);
        result = 31 * result + remainingRequests;
        result = 31 * result + Long.hashCode(resetTimeMillis);
        result = 31 * result + Long.hashCode(windowStartMillis);
        result = 31 * result + reason.hashCode();
        return result;
    }
}