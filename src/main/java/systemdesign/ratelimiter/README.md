# Rate Limiter

## Overview
Implements a fixed window rate limiting algorithm. It allows only a certain number of requests within a given time window.

## Why this design
Fixed window counters are easy to implement and sufficient for simple use cases where precision is not critical.

## Pros
- Very small memory footprint.
- Simple synchronized logic.

## Cons
- Boundary effects at window rollover can allow bursts.
- Not distributed across multiple instances.

## Possible improvements
- Use sliding window or token bucket algorithms for smoother limiting.
- Share counters across servers using a distributed store.

## Patterns and principles
- Encapsulates rate limiting logic within a single `RateLimiter` class.

## High-level design
Each request invokes `allowRequest`. The class resets the counter when the window expires and returns whether the request is allowed.

## Low-level design
- Stores current window start time and request count.
- `allowRequest` updates these fields in a thread-safe manner.
