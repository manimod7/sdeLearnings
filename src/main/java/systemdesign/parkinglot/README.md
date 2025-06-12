# Parking Lot

## Overview
A more feature rich parking lot example demonstrating floors, entry and exit gates and ticket based billing.

## Why this design
Vehicles are assigned to spots through gates and receive a ticket with the entry time. On leaving the lot the ticket is used to calculate the parking fee.

## Pros
- Shows the flow from vehicle entry to exit.
- Keeps responsibilities separated between the lot, gates and payment service.

## Cons
- In-memory state only with no concurrency control.
- Spot search is naive and does not find the nearest spot.

## Possible improvements
- Persist tickets and payments.
- Support reservations and sensors for real time availability.

## Patterns and principles
- Simple composition of objects representing the domain.

## High-level design
`EntryGate` requests an available `ParkingSpot` from the `ParkingLot` and issues a `ParkingTicket`. `ExitGate` calculates the fee using `PaymentService` and frees the spot.

## Low-level design
- `ParkingLot` contains multiple `ParkingFloor` objects each with `ParkingSpot`s.
- `EntryGate` assigns vehicles to spots and creates tickets.
- `ExitGate` computes the charge and releases the vehicle from the spot.
