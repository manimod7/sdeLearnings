# Ride Hailing

## Overview
A tiny ride hailing example that matches a rider with a driver via a service call.

## Why this design
The classes illustrate the minimum pieces required to request a ride and assign it to a driver without location tracking or pricing.

## Pros
- Straightforward demonstration of service coordination.
- Domain objects (`Rider`, `Driver`) are simple.

## Cons
- No search for nearby drivers or ride lifecycle.
- No persistence or real-time updates.

## Possible improvements
- Integrate driver availability and location tracking.
- Add pricing, trip management and payment handling.

## Patterns and principles
- Service layer (`RideService`) coordinates domain entities.

## High-level design
A rider and driver are provided to `RideService.requestRide`, which prints a match confirmation.

## Low-level design
- `Rider` and `Driver` store participant names.
- `RideService` exposes a method to match a rider with a driver.
