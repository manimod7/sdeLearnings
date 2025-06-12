# Messaging Service

## Overview
A small in-memory messaging service allowing one user to send a message to another. Messages are stored in a list.

## Why this design
This example demonstrates basic message sending without the complexity of persistence or message queues.

## Pros
- Clear separation of domain classes (`User`, `Message`).
- Easy to extend with features like history or notifications.

## Cons
- Messages are not persisted or indexed.
- No support for concurrent conversations.

## Possible improvements
- Store messages per user and allow retrieval by conversation.
- Add persistence and asynchronous delivery.
- Include features like read receipts.

## Patterns and principles
- Uses a simple service layer (`MessagingService`) to encapsulate operations.

## High-level design
`MessagingService.send` prints and stores a message object in memory. Clients can request the stored messages through `getMessages`.

## Low-level design
- `User` represents a chat participant.
- `Message` captures sender, recipient and text.
- `MessagingService` maintains the in-memory message store and exposes send/retrieve APIs.
