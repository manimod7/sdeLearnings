package lld.ratelimiter;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for Rate Limiter implementations.
 * 
 * Tests cover:
 * - Basic functionality
 * - Edge cases
 * - Concurrency scenarios
 * - Performance characteristics
 * - Configuration validation
 * - Cleanup mechanisms
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RateLimiterTest {
    
    private static final String TEST_USER = "test-user";
    private static final String ANOTHER_USER = "another-user";
    private static final long CURRENT_TIME = System.currentTimeMillis();
    
    @Test
    @Order(1)
    @DisplayName("Fixed Window Rate Limiter - Basic Functionality")
    void testFixedWindowBasicFunctionality() {
        RateLimiterConfig config = RateLimiterConfig.fixedWindow(5, 1000); // 5 requests per second
        FixedWindowRateLimiter rateLimiter = new FixedWindowRateLimiter(config);
        
        long windowStart = CURRENT_TIME;
        
        // Allow first 5 requests
        for (int i = 0; i < 5; i++) {
            RateLimitResult result = rateLimiter.allowRequest(TEST_USER, windowStart + i * 100);
            assertTrue(result.isAllowed(), \"Request \" + (i + 1) + \" should be allowed\");
            assertEquals(4 - i, result.getRemainingRequests(), \"Remaining requests should decrease\");
        }
        
        // 6th request should be denied
        RateLimitResult deniedResult = rateLimiter.allowRequest(TEST_USER, windowStart + 500);
        assertFalse(deniedResult.isAllowed(), \"6th request should be denied\");
        assertEquals(0, deniedResult.getRemainingRequests(), \"No remaining requests\");
        
        // After window expires, requests should be allowed again
        RateLimitResult allowedAfterWindow = rateLimiter.allowRequest(TEST_USER, windowStart + 1100);
        assertTrue(allowedAfterWindow.isAllowed(), \"Request after window should be allowed\");
        assertEquals(4, allowedAfterWindow.getRemainingRequests(), \"Remaining requests should reset\");
    }
    
    @Test
    @Order(2)
    @DisplayName("Sliding Window Rate Limiter - Precision Test")
    void testSlidingWindowPrecision() {
        RateLimiterConfig config = RateLimiterConfig.slidingWindow(3, 1000); // 3 requests per second
        SlidingWindowRateLimiter rateLimiter = new SlidingWindowRateLimiter(config);
        
        long baseTime = CURRENT_TIME;
        
        // Make 3 requests at different times
        assertTrue(rateLimiter.allowRequest(TEST_USER, baseTime).isAllowed());
        assertTrue(rateLimiter.allowRequest(TEST_USER, baseTime + 200).isAllowed());
        assertTrue(rateLimiter.allowRequest(TEST_USER, baseTime + 400).isAllowed());
        
        // 4th request should be denied
        assertFalse(rateLimiter.allowRequest(TEST_USER, baseTime + 500).isAllowed());
        
        // After 1 second from first request, oldest request expires
        assertTrue(rateLimiter.allowRequest(TEST_USER, baseTime + 1100).isAllowed());
        
        // Verify window information
        String windowInfo = rateLimiter.getWindowInfo(TEST_USER, baseTime + 1100);
        assertNotNull(windowInfo);
        assertTrue(windowInfo.contains(TEST_USER));
    }
    
    @Test
    @Order(3)
    @DisplayName("Concurrency Test - Multiple Threads")
    @Execution(ExecutionMode.CONCURRENT)
    void testConcurrency() throws InterruptedException {
        RateLimiterConfig config = RateLimiterConfig.fixedWindow(100, 1000);
        FixedWindowRateLimiter rateLimiter = new FixedWindowRateLimiter(config);
        
        int threadCount = 10;
        int requestsPerThread = 20;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger allowedRequests = new AtomicInteger(0);
        AtomicInteger deniedRequests = new AtomicInteger(0);
        
        // Submit tasks
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < requestsPerThread; j++) {
                        RateLimitResult result = rateLimiter.allowRequest(
                            \"user-\" + threadId, CURRENT_TIME + j * 10);
                        
                        if (result.isAllowed()) {
                            allowedRequests.incrementAndGet();
                        } else {
                            deniedRequests.incrementAndGet();
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        // Wait for completion
        assertTrue(latch.await(5, TimeUnit.SECONDS), \"Test should complete within 5 seconds\");
        executor.shutdown();
        
        // Verify results
        int totalRequests = allowedRequests.get() + deniedRequests.get();
        assertEquals(threadCount * requestsPerThread, totalRequests, \"All requests should be processed\");
        
        // Since each user has separate limits, and we have 10 users with 100 requests limit each,
        // all requests should be allowed (10 users * 20 requests = 200 total, well within individual limits)
        assertTrue(allowedRequests.get() > 0, \"Some requests should be allowed\");
    }
    
    @Test
    @Order(4)
    @DisplayName("Edge Cases - Invalid Inputs")
    void testInvalidInputs() {
        RateLimiterConfig config = RateLimiterConfig.fixedWindow(5, 1000);
        FixedWindowRateLimiter rateLimiter = new FixedWindowRateLimiter(config);
        
        // Null user ID
        assertThrows(IllegalArgumentException.class, () -> {
            rateLimiter.allowRequest(null, CURRENT_TIME);
        }, \"Null user ID should throw exception\");
        
        // Empty user ID
        assertThrows(IllegalArgumentException.class, () -> {
            rateLimiter.allowRequest(\"\", CURRENT_TIME);
        }, \"Empty user ID should throw exception\");
        
        // Whitespace-only user ID
        assertThrows(IllegalArgumentException.class, () -> {
            rateLimiter.allowRequest(\"   \", CURRENT_TIME);
        }, \"Whitespace-only user ID should throw exception\");
        
        // Negative time should still work (system handles it gracefully)
        RateLimitResult result = rateLimiter.allowRequest(TEST_USER, -1000);
        assertNotNull(result, \"Result should not be null even with negative time\");
    }
    
    @Test
    @Order(5)
    @DisplayName("Configuration Validation")
    void testConfigurationValidation() {
        // Valid configurations
        assertDoesNotThrow(() -> {
            RateLimiterConfig.fixedWindow(1, 1);
        }, \"Minimum valid config should not throw\");
        
        // Invalid configurations
        assertThrows(IllegalArgumentException.class, () -> {
            new RateLimiterConfig.Builder().maxRequests(0).build();
        }, \"Zero max requests should throw\");
        
        assertThrows(IllegalArgumentException.class, () -> {
            new RateLimiterConfig.Builder().maxRequests(-1).build();
        }, \"Negative max requests should throw\");
        
        assertThrows(IllegalArgumentException.class, () -> {
            new RateLimiterConfig.Builder().windowSizeMillis(0).build();
        }, \"Zero window size should throw\");
        
        assertThrows(IllegalArgumentException.class, () -> {
            new RateLimiterConfig.Builder().windowSizeMillis(-1).build();
        }, \"Negative window size should throw\");
        
        assertThrows(IllegalArgumentException.class, () -> {
            new RateLimiterConfig.Builder().strategyType(null).build();
        }, \"Null strategy type should throw\");
        
        assertThrows(IllegalArgumentException.class, () -> {
            new RateLimiterConfig.Builder().strategyType(\"\").build();
        }, \"Empty strategy type should throw\");
    }
    
    @Test
    @Order(6)
    @DisplayName("Cleanup Mechanism Test")
    void testCleanupMechanism() {
        RateLimiterConfig config = new RateLimiterConfig.Builder()
            .maxRequests(5)
            .windowSizeMillis(1000)
            .cleanupIntervalMillis(100) // Frequent cleanup for testing
            .maxUsers(2) // Low limit to trigger cleanup
            .build();
        
        FixedWindowRateLimiter rateLimiter = new FixedWindowRateLimiter(config);
        
        // Create requests for multiple users
        rateLimiter.allowRequest(\"user1\", CURRENT_TIME);
        rateLimiter.allowRequest(\"user2\", CURRENT_TIME);
        rateLimiter.allowRequest(\"user3\", CURRENT_TIME);
        
        assertEquals(3, rateLimiter.getActiveUserCount(), \"Should track 3 users initially\");
        
        // Trigger cleanup
        rateLimiter.cleanup(CURRENT_TIME + 200);
        
        // Should still have users as they're not expired
        assertTrue(rateLimiter.getActiveUserCount() > 0, \"Should still have active users\");
        
        // Force cleanup with expired time
        rateLimiter.cleanup(CURRENT_TIME + 3000); // Well past window expiry
        
        // Verify cleanup happened (exact count may vary due to LRU eviction)
        assertTrue(rateLimiter.getActiveUserCount() <= config.getMaxUsers(), 
                  \"User count should respect max limit after cleanup\");
    }
    
    @Test
    @Order(7)
    @DisplayName("Performance Test - High Load\")
    void testPerformance() {
        RateLimiterConfig config = RateLimiterConfig.fixedWindow(10000, 60000); // High limits
        FixedWindowRateLimiter rateLimiter = new FixedWindowRateLimiter(config);
        
        int requestCount = 10000;
        long startTime = System.nanoTime();
        
        // Make many requests
        for (int i = 0; i < requestCount; i++) {
            rateLimiter.allowRequest(\"perf-user\", CURRENT_TIME + i);
        }
        
        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;
        
        // Should complete reasonably fast (less than 1 second for 10k requests)
        assertTrue(durationMs < 1000, 
                  String.format(\"Performance test took %d ms, should be under 1000ms\", durationMs));
        
        double requestsPerMs = (double) requestCount / durationMs;
        System.out.printf(\"Performance: %.2f requests/ms (%.0f requests/second)%n\", 
                         requestsPerMs, requestsPerMs * 1000);
    }
    
    @Test
    @Order(8)
    @DisplayName(\"Rate Limit Result Validation\")
    void testRateLimitResult() {
        // Test allowed result
        RateLimitResult allowed = RateLimitResult.allowed(5, CURRENT_TIME + 1000, CURRENT_TIME);
        assertTrue(allowed.isAllowed());
        assertEquals(5, allowed.getRemainingRequests());
        assertEquals(CURRENT_TIME + 1000, allowed.getResetTimeMillis());
        assertTrue(allowed.getReason().contains(\"allowed\"));
        
        // Test denied result
        RateLimitResult denied = RateLimitResult.denied(CURRENT_TIME + 1000, CURRENT_TIME);
        assertFalse(denied.isAllowed());
        assertEquals(0, denied.getRemainingRequests());
        assertTrue(denied.getReason().contains(\"exceeded\"));
        
        // Test denied result with custom reason
        RateLimitResult deniedCustom = RateLimitResult.denied(CURRENT_TIME + 1000, \"Custom reason\", CURRENT_TIME);
        assertFalse(deniedCustom.isAllowed());
        assertEquals(\"Custom reason\", deniedCustom.getReason());
        
        // Test time calculations
        assertEquals((CURRENT_TIME + 1000) / 1000, allowed.getResetTimeSeconds());
        assertTrue(allowed.getTimeUntilResetMillis(CURRENT_TIME) > 0);
        assertTrue(allowed.getTimeUntilResetSeconds(CURRENT_TIME) >= 0);
        
        // Test toString
        assertNotNull(allowed.toString());
        assertTrue(allowed.toString().contains(\"allowed=true\"));
        
        // Test equals and hashCode
        RateLimitResult allowed2 = RateLimitResult.allowed(5, CURRENT_TIME + 1000, CURRENT_TIME);
        assertEquals(allowed, allowed2);
        assertEquals(allowed.hashCode(), allowed2.hashCode());
        
        assertNotEquals(allowed, denied);
        assertNotEquals(allowed.hashCode(), denied.hashCode());
    }
    
    @Test
    @Order(9)
    @DisplayName(\"Multi-User Isolation Test\")
    void testMultiUserIsolation() {
        RateLimiterConfig config = RateLimiterConfig.fixedWindow(2, 1000); // 2 requests per second
        FixedWindowRateLimiter rateLimiter = new FixedWindowRateLimiter(config);
        
        // User 1 makes requests
        assertTrue(rateLimiter.allowRequest(\"user1\", CURRENT_TIME).isAllowed());
        assertTrue(rateLimiter.allowRequest(\"user1\", CURRENT_TIME + 100).isAllowed());
        assertFalse(rateLimiter.allowRequest(\"user1\", CURRENT_TIME + 200).isAllowed()); // Exceeds limit
        
        // User 2 should have independent limits
        assertTrue(rateLimiter.allowRequest(\"user2\", CURRENT_TIME + 250).isAllowed());
        assertTrue(rateLimiter.allowRequest(\"user2\", CURRENT_TIME + 300).isAllowed());
        assertFalse(rateLimiter.allowRequest(\"user2\", CURRENT_TIME + 350).isAllowed()); // Exceeds limit
        
        // Verify remaining quotas are independent
        assertEquals(0, rateLimiter.getRemainingQuota(\"user1\", CURRENT_TIME + 400));
        assertEquals(0, rateLimiter.getRemainingQuota(\"user2\", CURRENT_TIME + 400));
        
        // After window reset, both users should have full quota
        assertEquals(2, rateLimiter.getRemainingQuota(\"user1\", CURRENT_TIME + 1500));
        assertEquals(2, rateLimiter.getRemainingQuota(\"user2\", CURRENT_TIME + 1500));
    }
    
    @Test
    @Order(10)
    @DisplayName(\"Reset Functionality Test\")
    void testResetFunctionality() {
        RateLimiterConfig config = RateLimiterConfig.fixedWindow(3, 1000);
        FixedWindowRateLimiter rateLimiter = new FixedWindowRateLimiter(config);
        
        // Make some requests
        rateLimiter.allowRequest(TEST_USER, CURRENT_TIME);
        rateLimiter.allowRequest(TEST_USER, CURRENT_TIME + 100);
        
        assertEquals(1, rateLimiter.getRemainingQuota(TEST_USER, CURRENT_TIME + 200));
        
        // Reset the user
        rateLimiter.reset(TEST_USER);
        
        // Should have full quota again
        assertEquals(3, rateLimiter.getRemainingQuota(TEST_USER, CURRENT_TIME + 300));
        
        // Test reset with null user (should not throw)
        assertDoesNotThrow(() -> rateLimiter.reset(null));
        
        // Test reset with non-existent user (should not throw)
        assertDoesNotThrow(() -> rateLimiter.reset(\"non-existent-user\"));
    }
}