package lld.ratelimiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Stream;

/**
 * Fixed Window Rate Limiter Implementation.
 * 
 * This implementation divides time into fixed windows and counts requests within each window.
 * When a window expires, the counter resets to zero.
 * 
 * Algorithm: Fixed Time Window
 * Time Complexity: O(1) per request
 * Space Complexity: O(U) where U is the number of users
 * 
 * Pros:
 * - Simple and fast implementation
 * - Low memory usage
 * - Predictable behavior
 * - Easy to understand and debug
 * 
 * Cons:
 * - Burst traffic at window boundaries
 * - Not perfectly smooth rate limiting
 * - Can allow 2x the limit in edge cases
 * 
 * Use Cases:
 * - Simple rate limiting requirements
 * - High-performance scenarios
 * - When burst traffic is acceptable
 */
public class FixedWindowRateLimiter implements RateLimitingStrategy {
    
    private final RateLimiterConfig config;
    private final ConcurrentHashMap<String, WindowData> userWindows;
    private final ReentrantReadWriteLock cleanupLock;
    private volatile long lastCleanupTime;
    Stream
    
    /**
     * Data structure to hold window information for a user.
     * 
     * Thread-safe implementation using AtomicInteger for request count.
     */
    private static class WindowData {
        private volatile long windowStart;
        private final AtomicInteger requestCount;
        
        public WindowData(long windowStart) {
            this.windowStart = windowStart;
            this.requestCount = new AtomicInteger(0);
        }
        
        public synchronized boolean isExpired(long currentTime, long windowSize) {
            return (currentTime - windowStart) >= windowSize;
        }
        
        public synchronized void reset(long newWindowStart) {
            this.windowStart = newWindowStart;
            this.requestCount.set(0);
        }
        
        public int incrementAndGet() {
            return requestCount.incrementAndGet();
        }
        
        public int get() {
            return requestCount.get();
        }
        
        public long getWindowStart() {
            return windowStart;
        }
    }
    
    public FixedWindowRateLimiter(RateLimiterConfig config) {
        this.config = config;
        this.userWindows = new ConcurrentHashMap<>();
        this.cleanupLock = new ReentrantReadWriteLock();
        this.lastCleanupTime = System.currentTimeMillis();
    }
    
    @Override
    public RateLimitResult allowRequest(String userId, long currentTimeMillis) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        
        cleanupLock.readLock().lock();
        try {
            WindowData windowData = getOrCreateWindowData(userId, currentTimeMillis);
            
            synchronized (windowData) {
                // Check if current window has expired
                if (windowData.isExpired(currentTimeMillis, config.getWindowSizeMillis())) {
                    long newWindowStart = calculateWindowStart(currentTimeMillis);
                    windowData.reset(newWindowStart);
                }
                
                int currentCount = windowData.get();
                long windowStart = windowData.getWindowStart();
                long resetTime = windowStart + config.getWindowSizeMillis();
                
                // Check if request can be allowed
                if (currentCount >= config.getMaxRequests()) {
                    return RateLimitResult.denied(resetTime, 
                        String.format("Rate limit exceeded: %d/%d requests in window", 
                                    currentCount, config.getMaxRequests()), windowStart);
                }
                
                // Allow the request and increment counter
                int newCount = windowData.incrementAndGet();
                int remaining = config.getMaxRequests() - newCount;
                
                return RateLimitResult.allowed(remaining, resetTime, windowStart);
            }
        } finally {
            cleanupLock.readLock().unlock();
        }
    }
    
    @Override
    public int getRemainingQuota(String userId, long currentTimeMillis) {
        if (userId == null) return config.getMaxRequests();
        
        WindowData windowData = userWindows.get(userId);
        if (windowData == null) {
            return config.getMaxRequests();
        }
        
        synchronized (windowData) {
            if (windowData.isExpired(currentTimeMillis, config.getWindowSizeMillis())) {
                return config.getMaxRequests();
            }
            
            return Math.max(0, config.getMaxRequests() - windowData.get());
        }
    }
    
    @Override
    public void reset(String userId) {
        if (userId == null) return;
        
        userWindows.remove(userId);
    }
    
    @Override
    public String getStrategyName() {
        return "Fixed Window Rate Limiter";
    }
    
    @Override
    public RateLimiterConfig getConfig() {
        return config;
    }
    
    @Override
    public void cleanup(long currentTimeMillis) {
        // Only perform cleanup if enough time has passed
        if (currentTimeMillis - lastCleanupTime < config.getCleanupIntervalMillis()) {
            return;
        }
        
        cleanupLock.writeLock().lock();
        try {
            lastCleanupTime = currentTimeMillis;
            
            // Remove expired windows
            userWindows.entrySet().removeIf(entry -> {
                WindowData windowData = entry.getValue();
                synchronized (windowData) {
                    return windowData.isExpired(currentTimeMillis, config.getWindowSizeMillis() * 2);
                }
            });
            
            // If too many users, remove oldest entries (LRU-like behavior)
            if (userWindows.size() > config.getMaxUsers()) {
                int toRemove = userWindows.size() - config.getMaxUsers();
                userWindows.entrySet().stream()
                    .sorted((e1, e2) -> Long.compare(e1.getValue().getWindowStart(), e2.getValue().getWindowStart()))
                    .limit(toRemove)
                    .forEach(entry -> userWindows.remove(entry.getKey()));
            }
        } finally {
            cleanupLock.writeLock().unlock();
        }
    }
    
    /**
     * Gets or creates window data for a user.
     * 
     * @param userId User identifier
     * @param currentTimeMillis Current timestamp
     * @return WindowData for the user
     */
    private WindowData getOrCreateWindowData(String userId, long currentTimeMillis) {
        return userWindows.computeIfAbsent(userId, 
            k -> new WindowData(calculateWindowStart(currentTimeMillis)));
    }
    
    /**
     * Calculates the start of the current window.
     * 
     * @param currentTimeMillis Current timestamp
     * @return Window start timestamp
     */
    private long calculateWindowStart(long currentTimeMillis) {
        return (currentTimeMillis / config.getWindowSizeMillis()) * config.getWindowSizeMillis();
    }
    
    /**
     * Gets the current number of active users being tracked.
     * 
     * @return Number of active users
     */
    public int getActiveUserCount() {
        return userWindows.size();
    }
    
    /**
     * Gets detailed information about a user's current window.
     * For testing and monitoring purposes.
     * 
     * @param userId User identifier
     * @return Window information or null if user not found
     */
    public String getWindowInfo(String userId) {
        WindowData windowData = userWindows.get(userId);
        if (windowData == null) {
            return String.format("User %s: No active window", userId);
        }
        
        synchronized (windowData) {
            return String.format("User %s: Window start=%d, Requests=%d/%d", 
                               userId, windowData.getWindowStart(), 
                               windowData.get(), config.getMaxRequests());
        }
    }
}