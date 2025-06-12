# URL Shortener

## Overview
A simplified service that maps long URLs to short codes. It demonstrates basic low level design ideas for a typical URL shortening system.

## Design
- **ShortUrl**: data object holding original URL and generated code.
- **UrlRepository**: in-memory store for short code mappings.
- **UrlShortenerService**: generates codes and resolves them back to URLs.
- **Main**: example usage of the service.

## Principles & Patterns
- *Single Responsibility Principle* for each class.
- *Repository pattern* abstracts persistence of URL mappings.
- Random code generation for simplicity.

## Features
- Create a short code for any URL.
- Retrieve the original URL from the code.
- Uses an in-memory map for storage.

## Possible Improvements
- Use hash-based or sequential ID generation to avoid collisions.
- Add database persistence and caching layer.
- Provide analytics, expiration and custom aliases.

## LLD Notes
This small design focuses on modularity: the service handles logic, while the repository manages storage. Scaling would require distributed ID generation and persistent storage.
