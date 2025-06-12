# Parking Lot

## Overview
A simplified parking lot system consisting of parking spots managed by a service. Each spot has a type such as car or bike.

## Why this design
The intention is to show basic allocation of parking spots without external storage or complex rules.

## Pros
- Clear separation of responsibilities (`ParkingLot`, `ParkingService`).
- Easy to extend with more spot types or pricing rules.

## Cons
- Does not handle concurrency or reservations.
- Parking spots exist only in memory.

## Possible improvements
- Add persistence so the lot state survives restarts.
- Support different pricing and billing strategies.
- Implement search policies for the nearest available spot.

## Patterns and principles
- Demonstrates composition of objects to represent the domain.

## High-level design
`ParkingService` asks the `ParkingLot` for an available `ParkingSpot` of a given `VehicleType` and marks it as occupied.

## Low-level design
- `ParkingSpot` stores its type and occupancy state.
- `ParkingLot` keeps a list of spots and can find one matching a type.
- `ParkingService` is a thin layer that handles park and unpark requests.
