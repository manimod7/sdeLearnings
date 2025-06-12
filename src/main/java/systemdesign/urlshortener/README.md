# URL Shortener

## Overview
Provides a basic service to generate short codes for URLs and retrieve the original URL from the code.

## Why this design
Random codes combined with an in-memory repository keep the example concise while demonstrating the core workflow of shortening and expanding URLs.

## Pros
- Simple and self-contained.
- Repository abstraction makes it easy to switch storage mechanisms.

## Cons
- Does not check for code collisions.
- Data is not persisted across restarts.

## Possible improvements
- Implement collision detection or deterministic code generation.
- Add analytics and expiration policies.
- Persist mappings in a database or distributed cache.

## Patterns and principles
- Uses a repository to separate persistence from business logic.
- Encapsulates functionality in a service class.

## High-level design
`UrlShortenerService.shorten` generates a random code and stores the mapping in `UrlRepository`. `getOriginalUrl` looks up the code.

## Low-level design
- `ShortUrl` is a data holder for code and original URL.
- `UrlRepository` provides `save` and `find` methods for mappings.
- `UrlShortenerService` orchestrates code generation and lookups.
