package lld.ratelimiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Sliding Window Rate Limiter Implementation.
 * 
 * This implementation maintains a sliding window of request timestamps
 * and removes old requests that fall outside the current window.
 * 
 * Algorithm: Sliding Window Log
 * Time Complexity: O(log W) per request (where W is requests in window)
 * Space Complexity: O(U × W) where U is users, W is requests in window
 * 
 * Pros:
 * - Precise rate limiting
 * - No burst traffic issues at boundaries
 * - Smooth user experience
 * - Accurate request counting
 * 
 * Cons:
 * - Higher memory usage (stores all timestamps)
 * - More complex implementation
 * - Cleanup overhead for old requests
 * - Can be slower for high request rates
 * 
 * Use Cases:
 * - When precise rate limiting is required
 * - APIs with sensitive rate limits
 * - When burst traffic should be prevented
 */
public class SlidingWindowRateLimiter implements RateLimitingStrategy {
    
    private final RateLimiterConfig config;
    private final ConcurrentHashMap<String, UserRequestWindow> userWindows;
    private final ReentrantReadWriteLock cleanupLock;
    private volatile long lastCleanupTime;
    
    /**
     * Data structure to hold sliding window of requests for a user.
     * 
     * Uses ConcurrentLinkedQueue for thread-safe operations.
     * Maintains timestamps of all requests within the window.
     */
    private static class UserRequestWindow {
        private final ConcurrentLinkedQueue<Long> requestTimes;
        private volatile long lastAccessTime;
        
        public UserRequestWindow() {
            this.requestTimes = new ConcurrentLinkedQueue<>();
            this.lastAccessTime = System.currentTimeMillis();
        }
        
        /**
         * Removes expired requests from the window.
         * 
         * @param currentTimeMillis Current timestamp
         * @param windowSizeMillis Window size in milliseconds
         * @return Number of requests removed
         */
        public synchronized int removeExpiredRequests(long currentTimeMillis, long windowSizeMillis) {
            long windowStart = currentTimeMillis - windowSizeMillis;
            int removedCount = 0;
            
            while (!requestTimes.isEmpty() && requestTimes.peek() <= windowStart) {
                requestTimes.poll();
                removedCount++;
            }
            
            this.lastAccessTime = currentTimeMillis;
            return removedCount;
        }
        
        /**
         * Adds a new request timestamp.
         * 
         * @param timestamp Request timestamp
         */
        public synchronized void addRequest(long timestamp) {
            requestTimes.offer(timestamp);
            this.lastAccessTime = timestamp;
        }
        
        /**
         * Gets the current number of requests in the window.
         * 
         * @return Number of requests
         */
        public synchronized int getCurrentRequestCount() {
            return requestTimes.size();
        }
        
        /**
         * Gets the timestamp of the oldest request in the window.
         * 
         * @return Oldest request timestamp, or -1 if no requests
         */
        public synchronized long getOldestRequestTime() {
            Long oldest = requestTimes.peek();
            return oldest != null ? oldest : -1;
        }
        
        public long getLastAccessTime() {
            return lastAccessTime;
        }
        
        public boolean isEmpty() {
            return requestTimes.isEmpty();
        }
    }
    
    public SlidingWindowRateLimiter(RateLimiterConfig config) {
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
            UserRequestWindow window = getOrCreateUserWindow(userId);
            
            synchronized (window) {
                // Remove expired requests
                window.removeExpiredRequests(currentTimeMillis, config.getWindowSizeMillis());
                
                int currentCount = window.getCurrentRequestCount();
                
                // Check if request can be allowed
                if (currentCount >= config.getMaxRequests()) {
                    long oldestRequestTime = window.getOldestRequestTime();
                    long resetTime = oldestRequestTime != -1 ? 
                        oldestRequestTime + config.getWindowSizeMillis() : 
                        currentTimeMillis + config.getWindowSizeMillis();
                    
                    return RateLimitResult.denied(resetTime, 
                        String.format("Rate limit exceeded: %d/%d requests in sliding window", 
                                    currentCount, config.getMaxRequests()), 
                        currentTimeMillis - config.getWindowSizeMillis());
                }
                
                // Allow the request and add timestamp
                window.addRequest(currentTimeMillis);
                int remaining = config.getMaxRequests() - (currentCount + 1);
                
                // Calculate reset time based on oldest request
                long oldestRequestTime = window.getOldestRequestTime();
                long resetTime = oldestRequestTime != -1 ? 
                    oldestRequestTime + config.getWindowSizeMillis() : 
                    currentTimeMillis + config.getWindowSizeMillis();
                
                return RateLimitResult.allowed(remaining, resetTime, 
                    currentTimeMillis - config.getWindowSizeMillis());
            }
        } finally {
            cleanupLock.readLock().unlock();
        }
    }
    
    @Override
    public int getRemainingQuota(String userId, long currentTimeMillis) {
        if (userId == null) return config.getMaxRequests();
        
        UserRequestWindow window = userWindows.get(userId);
        if (window == null) {
            return config.getMaxRequests();
        }
        
        synchronized (window) {
            window.removeExpiredRequests(currentTimeMillis, config.getWindowSizeMillis());
            return Math.max(0, config.getMaxRequests() - window.getCurrentRequestCount());
        }
    }
    
    @Override
    public void reset(String userId) {
        if (userId == null) return;
        
        userWindows.remove(userId);
    }
    
    @Override
    public String getStrategyName() {
        return "Sliding Window Rate Limiter";
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
            
            // Remove expired requests and inactive users
            userWindows.entrySet().removeIf(entry -> {
                UserRequestWindow window = entry.getValue();
                synchronized (window) {
                    window.removeExpiredRequests(currentTimeMillis, config.getWindowSizeMillis());
                    
                    // Remove users who haven't made requests recently
                    boolean inactive = (currentTimeMillis - window.getLastAccessTime()) > 
                                     (config.getWindowSizeMillis() * 2);
                    return inactive && window.isEmpty();
                }
            });
            
            // If too many users, remove least recently used
            if (userWindows.size() > config.getMaxUsers()) {
                int toRemove = userWindows.size() - config.getMaxUsers();
                userWindows.entrySet().stream()
                    .sorted((e1, e2) -> Long.compare(e1.getValue().getLastAccessTime(), 
                                                   e2.getValue().getLastAccessTime()))
                    .limit(toRemove)
                    .forEach(entry -> userWindows.remove(entry.getKey()));
            }
        } finally {
            cleanupLock.writeLock().unlock();
        }
    }
    
    /**
     * Gets or creates a user request window.
     * 
     * @param userId User identifier
     * @return UserRequestWindow for the user
     */
    private UserRequestWindow getOrCreateUserWindow(String userId) {
        return userWindows.computeIfAbsent(userId, k -> new UserRequestWindow());
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
     * Gets total number of requests currently being tracked across all users.
     * 
     * @return Total request count in memory
     */
    public int getTotalTrackedRequests() {
        return userWindows.values().stream()
                .mapToInt(window -> {
                    synchronized (window) {
                        return window.getCurrentRequestCount();
                    }
                })
                .sum();
    }
    
    /**
     * Gets detailed information about a user's current window.
     * For testing and monitoring purposes.
     * 
     * @param userId User identifier
     * @param currentTimeMillis Current timestamp for cleanup
     * @return Window information or null if user not found
     */
    public String getWindowInfo(String userId, long currentTimeMillis) {
        UserRequestWindow window = userWindows.get(userId);
        if (window == null) {
            return String.format("User %s: No active window", userId);
        }
        
        synchronized (window) {
            window.removeExpiredRequests(currentTimeMillis, config.getWindowSizeMillis());
            long oldestRequest = window.getOldestRequestTime();
            
            return String.format("User %s: Requests=%d/%d, Oldest=%d, Window=[%d-%d]", 
                               userId, window.getCurrentRequestCount(), config.getMaxRequests(),
                               oldestRequest, currentTimeMillis - config.getWindowSizeMillis(), 
                               currentTimeMillis);
        }
    }
}