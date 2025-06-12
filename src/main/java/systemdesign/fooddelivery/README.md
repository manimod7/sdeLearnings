# Food Delivery

## Overview
A simple representation of a food ordering system. An order references a restaurant and an item, and the `DeliveryService` prints dispatch information.

## Why this design
The goal is to demonstrate very basic interactions between restaurant, order and delivery components without modelling real world complexity.

## Pros
- Minimal code for quick understanding.
- Clear separation between domain objects and service.

## Cons
- No concept of delivery personnel or tracking.
- Orders are not persisted.

## Possible improvements
- Maintain order status and assignment to delivery agents.
- Add menu management and pricing.
- Persist restaurants and orders in a database.

## Patterns and principles
- Encapsulates delivery logic in a `DeliveryService`.
- Uses simple domain models (`Restaurant`, `Order`).

## High-level design
The user creates an `Order` for a `Restaurant` and passes it to `DeliveryService.dispatch`, which currently just prints a message.

## Low-level design
- `Restaurant` holds the restaurant name.
- `Order` references a `Restaurant` and an item to be delivered.
- `DeliveryService` handles dispatching of the order.
