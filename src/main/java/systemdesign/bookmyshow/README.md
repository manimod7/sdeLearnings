# BookMyShow

## Overview
A minimal booking service that reserves seats for a movie show. It models movies, shows and seats with basic in-memory classes.

## Why this design
The example focuses on illustrating seat reservation logic without external dependencies. A simple list of seats is sufficient for demonstration.

## Pros
- Easy to understand and run.
- Encapsulates booking logic in a service class.

## Cons
- No persistence or concurrency handling.
- Seat search is linear and inefficient for large shows.

## Possible improvements
- Use a database to store shows and bookings.
- Introduce locking to avoid double booking.
- Support seat categories and pricing.

## Patterns and principles
- Separates concerns using a small `BookingService`.
- Uses domain objects (`Movie`, `Show`, `Seat`).

## High-level design
Clients interact with `BookingService` to book a seat for a given `Show`. The service iterates over available seats and marks one as booked.

## Low-level design
- `Movie` holds movie metadata.
- `Show` aggregates `Seat` objects for a movie.
- `Seat` maintains its booking state.
- `BookingService` performs the booking operation.
