# Log System

## Overview
Demonstrates a basic logging repository where log messages are stored and retrieved.

## Design
- **LogMessage**: encapsulates message text and timestamp.
- **LogRepository**: stores messages in memory.
- **LogService**: adds messages to the repository and returns them.
- **Main**: simple example writing and reading logs.

## Principles & Patterns
- *Single Responsibility* for each component.
- Repository pattern to abstract data storage.

## Features
- Persist log messages in memory.
- Retrieve all stored messages.

## Possible Improvements
- Use persistent storage or log rotation.
- Add filtering and search by time or level.
- Support distributed or asynchronous logging.

## LLD Notes
This straightforward design can be extended with different repository implementations (e.g., file, database) via an interface for flexibility.
