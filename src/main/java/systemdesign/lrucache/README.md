# LRU Cache

## Overview
This module demonstrates a simple Least Recently Used (LRU) cache implemented with a hashmap and a doubly linked list.

## Why this design
An LRU cache keeps recently accessed items in memory while evicting the least recently used ones. Using a hashmap for O(1) lookups and a linked list for ordering provides efficient access and eviction.

## Pros
- Constant time `get` and `put` operations.
- Simple to understand and extend.

## Cons
- Not thread‑safe.
- Each entry requires extra memory for list pointers.

## Possible improvements
- Add synchronization to make the cache concurrent.
- Allow configurable eviction policies.
- Persist entries to disk if needed.

## Patterns and principles
- Combines a hashmap with a doubly linked list to implement the LRU caching pattern.

## High-level design
Clients call `LruCache` which stores key/value pairs and maintains a usage order list. When capacity is exceeded, the tail node is removed.

## Low-level design
- `LruCache` manages the hashmap and linked list.
- `Node` represents each cached entry with `prev` and `next` pointers.
