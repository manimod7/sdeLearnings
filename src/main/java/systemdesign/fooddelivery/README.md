# Food Delivery

## Overview
A simple design for ordering food from restaurants and dispatching deliveries.

## Design
- **Restaurant**: vendor offering items.
- **Order**: customer's order associated with a restaurant.
- **DeliveryService**: dispatches orders for delivery.
- **Main**: demonstrates creating an order and sending it out.

## Principles & Patterns
- *Single Responsibility* for each class.
- Service layer coordinates high-level operations.

## Features
- Place orders linked to restaurants.
- Dispatch orders through a delivery service.

## Possible Improvements
- Add menus, user accounts and payment processing.
- Implement driver assignment and tracking.
- Introduce persistence and order states (preparing, delivered, etc.).

## LLD Notes
Current implementation is in-memory and sequential. A real service would require database-backed storage and a queue for delivery dispatching.
