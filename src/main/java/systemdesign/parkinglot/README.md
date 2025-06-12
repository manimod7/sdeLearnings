# Parking Lot

## Overview
Simplified design for tracking parking spots and vehicles.

## Design
- **ParkingSpot**: represents a spot with type and occupancy status.
- **ParkingLot**: collection of spots and helper methods.
- **ParkingService**: assigns and frees spots for vehicles.
- **VehicleType**: enumerates sizes/types of vehicles.
- **Main**: example of parking workflow.

## Principles & Patterns
- Single Responsibility for each entity.
- Service layer handles the core business operations.

## Features
- Allocate available parking spot for a vehicle.
- Free spots when a vehicle leaves.

## Possible Improvements
- Add multiple floors and spot pricing.
- Track time parked and calculate fees.
- Support reservations and sensors for real-time status.

## LLD Notes
The parking lot is stored in memory and assumes sequential operations. Real implementations would require concurrency control and persistent storage.
