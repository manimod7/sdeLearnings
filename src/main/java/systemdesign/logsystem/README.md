# Log System

## Overview
Implements a basic logging service with an in-memory repository. The service accepts log messages and stores them through the repository.

## Why this design
A repository abstraction keeps storage concerns separate from the service that creates log entries. This example keeps everything in memory for simplicity.

## Pros
- Straightforward repository-service structure.
- Easy to replace the repository with another implementation.

## Cons
- Logs are lost when the process ends.
- No asynchronous or batched writes.

## Possible improvements
- Persist logs to disk or an external system.
- Support log levels and filtering before storage.
- Make logging asynchronous to avoid blocking callers.

## Patterns and principles
- Applies the Repository pattern to decouple storage from business logic.

## High-level design
`LogService` receives log requests and delegates persistence to `LogRepository`, which stores `LogMessage` objects in a list.

## Low-level design
- `LogMessage` holds level and text.
- `LogRepository` manages the list of messages.
- `LogService` offers the `log` operation to clients.
