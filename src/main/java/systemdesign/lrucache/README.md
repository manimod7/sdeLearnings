# LRU Cache

## Overview
Implements a Least Recently Used cache with constant time get and put operations.

## Design
- **Node**: doubly linked list node holding key/value pairs.
- **LruCache**: stores nodes and maintains LRU order using a hash map and list.
- **Main**: small example demonstrating cache operations.

## Principles & Patterns
- *Single Responsibility* for classes.
- Uses a combination of hashmap and doubly linked list for efficiency.

## Features
- O(1) get and put.
- Evicts least recently used entry when capacity is reached.

## Possible Improvements
- Thread-safety for concurrent access.
- Persistence of frequently used entries.
- Parameterize eviction policy for other strategies.

## LLD Notes
The cache exposes basic APIs and hides internal list management. In large systems this component could be adapted for distributed caches or integrated with a service layer.
