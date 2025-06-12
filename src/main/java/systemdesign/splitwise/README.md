# Splitwise

## Overview
Models a simplified Splitwise-like expense sharing system. Balances are maintained in memory for each user.

## Why this design
The focus is on how expenses affect user balances rather than UI or persistence. Balances are kept in a map for quick demonstration.

## Pros
- Shows how expenses are divided and tracked per user.
- Small number of classes with clear responsibilities.

## Cons
- No user authentication or groups.
- Data is not persisted.

## Possible improvements
- Persist data so balances survive restarts.
- Add support for splitting by percentages or unequal shares.
- Provide reporting of who owes whom.

## Patterns and principles
- Keeps domain logic in `SplitwiseService` with plain data objects.

## High-level design
Users and their balances are stored in a map. When an expense is added, the map is updated for the payer and each share.

## Low-level design
- `User` represents a participant in the expense.
- `Expense` contains the amount, payer and share breakdown.
- `SplitwiseService` updates the `balanceMap` when new expenses are added.
