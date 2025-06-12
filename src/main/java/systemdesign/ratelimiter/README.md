# Rate Limiter

## Overview
A fixed-window rate limiter restricting the number of allowed requests in a given time interval.

## Design
- **RateLimiter**: maintains request count and window timing.
- **Main**: demonstrates repeatedly calling the limiter.

## Principles & Patterns
- Encapsulates concurrency control in a single component.
- Uses synchronization to ensure thread safety.

## Features
- Configurable request limit and time window.
- Simple boolean API to check whether a request should be allowed.

## Possible Improvements
- Support sliding window or token bucket algorithms.
- Expose metrics and dynamic configuration.
- Integrate with distributed stores for multi-node deployments.

## LLD Notes
This implementation keeps counters in memory and is meant for single-instance usage. For distributed systems, a centralized or sharded store would be required.
