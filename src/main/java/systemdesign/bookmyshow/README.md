# Book My Show

## Overview
Mini booking system for reserving movie seats. Demonstrates a straightforward object-oriented approach.

## Design
- **Movie**: film details such as title and duration.
- **Seat**: represents individual seat status.
- **Show**: scheduled movie show with a list of seats.
- **BookingService**: handles seat reservations.
- **Main**: sample flow creating shows and booking seats.

## Principles & Patterns
- *Single Responsibility Principle*: each class focuses on one concept.
- Basic service layer pattern separating business logic from data objects.

## Features
- Book available seats for a given show.
- Prevent double booking of seats.

## Possible Improvements
- Handle user accounts and payments.
- Support seat categories and pricing.
- Persist data in a database and add search APIs.

## LLD Notes
A minimal model focusing on seat availability within a single process. Scaling would require distributed locking and more sophisticated data stores.
