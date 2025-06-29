package lld.ratelimiter;

/**
 * Immutable configuration class for rate limiters.
 * 
 * Uses Builder pattern for flexible and readable configuration creation.
 * Supports validation and defaults for robust configuration management.
 * 
 * Design Pattern: Builder Pattern
 * Benefits:
 * - Fluent API for configuration
 * - Immutable configuration objects
 * - Parameter validation
 * - Default value handling
 */
public class RateLimiterConfig {
    private final int maxRequests;
    private final long windowSizeMillis;
    private final String strategyType;
    private final long cleanupIntervalMillis;
    private final int maxUsers;
    private final boolean enableMetrics;
    
    // Token bucket specific configurations
    private final double refillRate;
    private final int bucketCapacity;
    
    private RateLimiterConfig(Builder builder) {
        this.maxRequests = builder.maxRequests;
        this.windowSizeMillis = builder.windowSizeMillis;
        this.strategyType = builder.strategyType;
        this.cleanupIntervalMillis = builder.cleanupIntervalMillis;
        this.maxUsers = builder.maxUsers;
        this.enableMetrics = builder.enableMetrics;
        this.refillRate = builder.refillRate;
        this.bucketCapacity = builder.bucketCapacity;
        
        validate();
    }
    
    /**
     * Validates the configuration parameters.
     * 
     * @throws IllegalArgumentException if configuration is invalid
     */
    private void validate() {
        if (maxRequests <= 0) {
            throw new IllegalArgumentException("Max requests must be positive");
        }
        if (windowSizeMillis <= 0) {
            throw new IllegalArgumentException("Window size must be positive");
        }
        if (strategyType == null || strategyType.trim().isEmpty()) {
            throw new IllegalArgumentException("Strategy type cannot be null or empty");
        }
        if (maxUsers <= 0) {
            throw new IllegalArgumentException("Max users must be positive");
        }
        if (refillRate < 0) {
            throw new IllegalArgumentException("Refill rate cannot be negative");
        }
        if (bucketCapacity < 0) {
            throw new IllegalArgumentException("Bucket capacity cannot be negative");
        }
    }
    
    // Factory methods for common configurations
    public static RateLimiterConfig fixedWindow(int maxRequests, long windowSizeMillis) {
        return new Builder()
                .maxRequests(maxRequests)
                .windowSizeMillis(windowSizeMillis)
                .strategyType("FIXED_WINDOW")
                .build();
    }
    
    public static RateLimiterConfig slidingWindow(int maxRequests, long windowSizeMillis) {
        return new Builder()
                .maxRequests(maxRequests)
                .windowSizeMillis(windowSizeMillis)
                .strategyType("SLIDING_WINDOW")
                .build();
    }
    
    public static RateLimiterConfig tokenBucket(int bucketCapacity, double refillRate) {
        return new Builder()
                .bucketCapacity(bucketCapacity)
                .refillRate(refillRate)
                .maxRequests(bucketCapacity)
                .windowSizeMillis(1000) // 1 second for token bucket
                .strategyType("TOKEN_BUCKET")
                .build();
    }
    
    // Getters
    public int getMaxRequests() { return maxRequests; }
    public long getWindowSizeMillis() { return windowSizeMillis; }
    public String getStrategyType() { return strategyType; }
    public long getCleanupIntervalMillis() { return cleanupIntervalMillis; }
    public int getMaxUsers() { return maxUsers; }
    public boolean isEnableMetrics() { return enableMetrics; }
    public double getRefillRate() { return refillRate; }
    public int getBucketCapacity() { return bucketCapacity; }
    
    // Convenience methods
    public long getWindowSizeSeconds() { return windowSizeMillis / 1000; }
    
    /**
     * Builder class for creating RateLimiterConfig instances.
     */
    public static class Builder {
        private int maxRequests = 100;
        private long windowSizeMillis = 60000; // 1 minute
        private String strategyType = "FIXED_WINDOW";
        private long cleanupIntervalMillis = 300000; // 5 minutes
        private int maxUsers = 10000;
        private boolean enableMetrics = true;
        private double refillRate = 1.0; // tokens per second
        private int bucketCapacity = 100;
        
        public Builder maxRequests(int maxRequests) {
            this.maxRequests = maxRequests;
            return this;
        }
        
        public Builder windowSizeMillis(long windowSizeMillis) {
            this.windowSizeMillis = windowSizeMillis;
            return this;
        }
        
        public Builder windowSizeSeconds(long windowSizeSeconds) {
            this.windowSizeMillis = windowSizeSeconds * 1000;
            return this;
        }
        
        public Builder strategyType(String strategyType) {
            this.strategyType = strategyType;
            return this;
        }
        
        public Builder cleanupIntervalMillis(long cleanupIntervalMillis) {
            this.cleanupIntervalMillis = cleanupIntervalMillis;
            return this;
        }
        
        public Builder maxUsers(int maxUsers) {
            this.maxUsers = maxUsers;
            return this;
        }
        
        public Builder enableMetrics(boolean enableMetrics) {
            this.enableMetrics = enableMetrics;
            return this;
        }
        
        public Builder refillRate(double refillRate) {
            this.refillRate = refillRate;
            return this;
        }
        
        public Builder bucketCapacity(int bucketCapacity) {
            this.bucketCapacity = bucketCapacity;
            return this;
        }
        
        public RateLimiterConfig build() {
            return new RateLimiterConfig(this);
        }
    }
    
    @Override
    public String toString() {
        return String.format("RateLimiterConfig{maxRequests=%d, windowSizeMs=%d, strategy='%s', maxUsers=%d}", 
                           maxRequests, windowSizeMillis, strategyType, maxUsers);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        RateLimiterConfig that = (RateLimiterConfig) obj;
        return maxRequests == that.maxRequests &&
               windowSizeMillis == that.windowSizeMillis &&
               maxUsers == that.maxUsers &&
               enableMetrics == that.enableMetrics &&
               Double.compare(that.refillRate, refillRate) == 0 &&
               bucketCapacity == that.bucketCapacity &&
               strategyType.equals(that.strategyType);
    }
    
    @Override
    public int hashCode() {
        int result = maxRequests;
        result = 31 * result + Long.hashCode(windowSizeMillis);
        result = 31 * result + strategyType.hashCode();
        result = 31 * result + maxUsers;
        result = 31 * result + Boolean.hashCode(enableMetrics);
        result = 31 * result + Double.hashCode(refillRate);
        result = 31 * result + bucketCapacity;
        return result;
    }
}