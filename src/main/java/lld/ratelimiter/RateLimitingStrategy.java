package lld.ratelimiter;

/**
 * Strategy interface for different rate limiting algorithms.
 * 
 * This interface defines the contract for all rate limiting strategies,
 * enabling the system to use different algorithms interchangeably.
 * 
 * Design Pattern: Strategy Pattern
 * Benefits:
 * - Algorithm independence
 * - Runtime strategy switching
 * - Easy addition of new strategies
 * - Single responsibility principle
 */
public interface RateLimitingStrategy {
    
    /**
     * Checks if a request should be allowed based on the rate limiting strategy.
     * 
     * @param userId Unique identifier for the user/client
     * @param currentTimeMillis Current timestamp in milliseconds
     * @return RateLimitResult containing decision and metadata
     * 
     * Time Complexity: Depends on implementation (O(1) to O(log n))
     * Space Complexity: O(1) per call
     */
    RateLimitResult allowRequest(String userId, long currentTimeMillis);
    
    /**
     * Gets the remaining quota for a user.
     * 
     * @param userId Unique identifier for the user/client
     * @param currentTimeMillis Current timestamp in milliseconds
     * @return Remaining number of requests allowed
     */
    int getRemainingQuota(String userId, long currentTimeMillis);
    
    /**
     * Resets the rate limit for a specific user.
     * 
     * @param userId Unique identifier for the user/client
     */
    void reset(String userId);
    
    /**
     * Gets the strategy name for logging and monitoring.
     * 
     * @return Strategy name
     */
    String getStrategyName();
    
    /**
     * Gets the configuration for this strategy.
     * 
     * @return Rate limiter configuration
     */
    RateLimiterConfig getConfig();
    
    /**
     * Cleans up expired data to prevent memory leaks.
     * Should be called periodically.
     * 
     * @param currentTimeMillis Current timestamp in milliseconds
     */
    void cleanup(long currentTimeMillis);
}