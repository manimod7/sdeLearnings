# Splitwise

## Overview
Simple expense splitting service that keeps track of who owes money to whom.

## Design
- **User**: represents a participant in shared expenses.
- **Expense**: amount paid by one user with a map of shares owed by others.
- **SplitwiseService**: updates balances when expenses are added.
- **Main**: example of recording an expense and checking balances.

## Principles & Patterns
- *Single Responsibility* for each data type.
- Service layer manages the balance map.

## Features
- Add expenses with arbitrary sharing ratios.
- Query balances per user.

## Possible Improvements
- Handle group management and settle-up workflows.
- Add persistent storage and support different currencies.
- Incorporate notifications and history tracking.

## LLD Notes
Currently the service uses a single in-memory map of balances. A real solution would maintain transactions and support multiple groups and advanced splitting logic.
