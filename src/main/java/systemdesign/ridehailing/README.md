# Ride Hailing

## Overview
A lightweight example of matching riders with drivers for a ride hailing application.

## Design
- **Rider** and **Driver**: participant entities with simple attributes.
- **RideService**: pairs riders with drivers when a request is made.
- **Main**: demonstration of requesting a ride.

## Principles & Patterns
- Entities encapsulate user data.
- Service layer handles matching logic.

## Features
- Request a ride and see a textual match result.

## Possible Improvements
- Implement geographic location and driver availability search.
- Calculate fares and track trip states.
- Support payments and ratings.

## LLD Notes
The design omits persistence and route planning. A full solution would integrate a dispatch algorithm, maps and a database for trips and drivers.
