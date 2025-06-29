# Rate Limiter Low Level Design

## Problem Statement
Design a Rate Limiter for a high-traffic service that allows "X requests in Y seconds" per user. If the rate exceeds the limit, further requests should be blocked for the remaining duration.

## Requirements

### Functional Requirements
- ✅ Handle X requests per Y seconds per user/client
- ❌ Block requests when limit is exceeded
- 📈 Support different rate-limiting strategies (extensible)
- 🔧 Configuration-based limits for different APIs/users
- 📊 Provide rate limit status and remaining quota

### Non-Functional Requirements
- ⚡ Low-latency response for high-volume requests
- 📊 Support millions of users
- 🧵 Thread-safe for concurrent access
- 🚀 Scalable architecture
- 💾 Memory efficient

## Design Patterns Used

### 1. Strategy Pattern
- **Purpose**: Allow different rate limiting algorithms to be used interchangeably
- **Implementation**: `RateLimitingStrategy` interface with multiple implementations
- **Benefits**: Easy to add new algorithms, runtime strategy switching

### 2. Factory Pattern
- **Purpose**: Create appropriate rate limiter instances based on configuration
- **Implementation**: `RateLimiterFactory` class
- **Benefits**: Centralized object creation, configuration-driven instantiation

### 3. Builder Pattern
- **Purpose**: Create complex rate limiter configurations
- **Implementation**: `RateLimiterConfig.Builder`
- **Benefits**: Flexible configuration, immutable objects

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                    Rate Limiter System                      │
├─────────────────────────────────────────────────────────────┤
│  Client Request → RateLimiterService → Strategy → Response  │
└─────────────────────────────────────────────────────────────┘

Components:
1. RateLimiterService (Main Facade)
2. RateLimitingStrategy (Strategy Interface)
3. Concrete Strategies (Fixed Window, Sliding Window, Token Bucket)
4. RateLimiterFactory (Factory)
5. RateLimiterConfig (Configuration)
6. UserRateLimitInfo (Data Model)
```

## UML Class Diagram

```
                    ┌─────────────────────┐
                    │  RateLimiterService │
                    ├─────────────────────┤
                    │ - strategies: Map   │
                    │ - factory: Factory  │
                    ├─────────────────────┤
                    │ + allowRequest()    │
                    │ + getRateLimit()    │
                    │ + updateConfig()    │
                    └─────────────────────┘
                              │
                              ▼
                    ┌─────────────────────┐
                    │ RateLimitingStrategy│◄──────────┐
                    ├─────────────────────┤           │
                    │ + allowRequest()    │           │
                    │ + getRemainingQuota()│          │
                    │ + reset()           │           │
                    └─────────────────────┘           │
                              △                       │
                              │                       │
         ┌────────────────────┼────────────────────┐  │
         │                    │                    │  │
┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐
│ FixedWindowRate │  │SlidingWindowRate│  │ TokenBucketRate │
│    Limiter      │  │    Limiter      │  │    Limiter      │
├─────────────────┤  ├─────────────────┤  ├─────────────────┤
│ - windowStart   │  │ - requestTimes  │  │ - tokens        │
│ - requestCount  │  │ - windowSize    │  │ - lastRefill    │
├─────────────────┤  ├─────────────────┤  ├─────────────────┤
│ + allowRequest()│  │ + allowRequest()│  │ + allowRequest()│
└─────────────────┘  └─────────────────┘  └─────────────────┘
                              │
                              │
                    ┌─────────────────────┐
                    │ RateLimiterFactory  │
                    ├─────────────────────┤
                    │ + createRateLimiter()│
                    │ + createFromConfig()│
                    └─────────────────────┘
```

## Flow Chart

```
Start Request
      │
      ▼
┌─────────────────┐
│ Extract User ID │
│ and Endpoint    │
└─────────────────┘
      │
      ▼
┌─────────────────┐      ┌─────────────────┐
│ Get Rate Limiter│─────▶│ Create New      │
│ for User        │      │ Rate Limiter    │
└─────────────────┘      └─────────────────┘
      │                           │
      ▼                           │
┌─────────────────┐              │
│ Check if Request│◄─────────────┘
│ is Allowed      │
└─────────────────┘
      │
      ▼
┌─────────────────┐      ┌─────────────────┐
│ Request Allowed?│ No   │ Return 429      │
│                 │─────▶│ Too Many        │
└─────────────────┘      │ Requests        │
      │ Yes               └─────────────────┘
      ▼
┌─────────────────┐
│ Process Request │
│ and Return 200  │
└─────────────────┘
      │
      ▼
    End
```

## Time & Space Complexity

### Fixed Window Rate Limiter
- **Time Complexity**: O(1) for each request
- **Space Complexity**: O(U) where U is number of users

### Sliding Window Rate Limiter
- **Time Complexity**: O(log W) where W is window size (for cleanup)
- **Space Complexity**: O(U × W) where U is users, W is requests in window

### Token Bucket Rate Limiter
- **Time Complexity**: O(1) for each request
- **Space Complexity**: O(U) where U is number of users

## Pros and Cons

### Fixed Window
**Pros:**
- Simple implementation
- Low memory usage
- Fast O(1) operations

**Cons:**
- Burst traffic at window boundaries
- Not precise for distributed systems

### Sliding Window
**Pros:**
- More precise rate limiting
- Handles burst traffic better
- Better user experience

**Cons:**
- Higher memory usage
- More complex implementation
- Cleanup overhead

### Token Bucket
**Pros:**
- Smooth rate limiting
- Allows controlled bursts
- Good for API gateways

**Cons:**
- Complex token refill logic
- May allow temporary over-limit

## Scalability & Extensibility

### Scalability
- **Horizontal Scaling**: Can be deployed across multiple instances
- **Redis Integration**: For distributed rate limiting
- **Database Backing**: For persistent rate limit data
- **Memory Optimization**: LRU eviction for inactive users

### Extensibility
- **New Algorithms**: Easy to add via Strategy pattern
- **Custom Configurations**: Flexible config system
- **Monitoring Integration**: Metrics and alerting hooks
- **Multi-tier Limits**: User, API, global limits

## Edge Cases Handled
1. **Concurrent Requests**: Thread-safe operations
2. **Clock Skew**: Relative time calculations
3. **Memory Pressure**: LRU eviction
4. **Configuration Changes**: Runtime updates
5. **Negative Time**: Time validation
6. **Zero Limits**: Special handling
7. **Very Small Windows**: Precision handling

## Production Considerations
1. **Monitoring**: Request rates, rejection rates, latency
2. **Alerting**: Rate limit breaches, system overload
3. **Configuration**: Dynamic config updates
4. **Testing**: Load testing, edge case testing
5. **Documentation**: API docs, runbooks