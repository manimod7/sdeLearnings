# Snake Game System - Low Level Design

## 🎯 Problem Statement (Verified Atlassian Question)
Design a classic Snake Game system where the snake moves in specified directions, grows when eating food, and the game ends when the snake collides with itself or boundaries. The system should handle real-time input, maintain game state, and provide smooth gameplay experience.

## 📋 Requirements

### Functional Requirements
- 🐍 Snake movement in four directions (up, down, left, right)
- 🍎 Food generation at random positions after consumption
- 📈 Snake growth mechanism when food is consumed
- 💥 Collision detection (self-collision and boundary collision)
- 🎯 Score tracking based on food consumed
- ⏸️ Game state management (running, paused, game over)
- 🔄 Game reset functionality
- 🎮 Input validation and handling

### Non-Functional Requirements
- ⚡ Real-time responsive controls (< 50ms input lag)
- 🎮 Smooth 60 FPS gameplay experience
- 📊 Extensible for different game modes
- 🧵 Thread-safe operations for multiplayer potential
- 💾 Memory efficient data structures
- 🔧 Easy to extend with new features

## 🏗️ Architecture & Design Patterns

### Primary Design Patterns

#### 1. State Pattern
- **Purpose**: Manage different game states cleanly
- **Implementation**: `GameState` interface with `RunningState`, `PausedState`, `GameOverState`
- **Benefits**: 
  - Clean state transitions
  - State-specific behavior encapsulation
  - Easy to add new states

#### 2. Command Pattern
- **Purpose**: Handle player input and enable undo functionality
- **Implementation**: `Command` interface with direction-specific commands
- **Benefits**: 
  - Decoupled input handling
  - Potential for input recording/replay
  - Input queuing capabilities

#### 3. Observer Pattern
- **Purpose**: Notify UI components of game events
- **Implementation**: `GameEventListener` for score updates, state changes
- **Benefits**: 
  - Loose coupling between game logic and UI
  - Multiple observers (UI, sound, analytics)

#### 4. Value Object Pattern
- **Purpose**: Immutable game coordinates and results
- **Implementation**: `Point`, `MoveResult` classes
- **Benefits**: Thread safety, clear data contracts

## 🎨 UML Class Diagram

```
                    ┌─────────────────────────┐
                    │      SnakeGame          │
                    ├─────────────────────────┤
                    │ - snake: Snake          │
                    │ - food: Food            │
                    │ - board: GameBoard      │
                    │ - state: GameState      │
                    │ - score: int            │
                    │ - gameTime: long        │
                    ├─────────────────────────┤
                    │ + start()               │
                    │ + pause()               │
                    │ + reset()               │
                    │ + processInput()        │
                    │ + update()              │
                    └─────────────────────────┘
                              │
                    ┌─────────┴─────────┐
                    │                   │
                    ▼                   ▼
        ┌─────────────────────┐ ┌─────────────────────┐
        │       Snake         │ │     GameBoard       │
        ├─────────────────────┤ ├─────────────────────┤
        │ - body: Deque<Point>│ │ - width: int        │
        │ - direction: Dir    │ │ - height: int       │
        │ - growing: boolean  │ │ - obstacles: Set    │
        │ - lock: RWLock      │ ├─────────────────────┤
        ├─────────────────────┤ │ + isValidPosition() │
        │ + move()            │ │ + isOutOfBounds()   │
        │ + grow()            │ │ + addObstacle()     │
        │ + changeDirection() │ └─────────────────────┘
        │ + checkCollision()  │
        └─────────────────────┘
                    │
                    │ uses
                    ▼
        ┌─────────────────────┐
        │    GameState        │◄──────────┐
        ├─────────────────────┤           │
        │ + update()          │           │
        │ + handleInput()     │           │
        │ + canMove()         │           │
        │ + isGameOver()      │           │
        │ + onEnter()         │           │
        │ + onExit()          │           │
        └─────────────────────┘           │
                    △                     │
                    │                     │
        ┌───────────┼───────────┐         │
        │           │           │         │
        ▼           ▼           ▼         │
┌─────────────┐ ┌─────────────┐ ┌─────────────────┐
│RunningState │ │PausedState  │ │GameOverState    │
├─────────────┤ ├─────────────┤ ├─────────────────┤
│+ update()   │ │+ update()   │ │+ update()       │
│+ canMove()  │ │+ canMove()  │ │+ canMove()      │
└─────────────┘ └─────────────┘ │+ setGameOver... │
                                │+ isWin()        │
                                │+ displayStats() │
                                └─────────────────┘

                    ┌─────────────────────┐
                    │     Direction       │
                    ├─────────────────────┤
                    │ UP(0, -1)           │
                    │ DOWN(0, 1)          │
                    │ LEFT(-1, 0)         │
                    │ RIGHT(1, 0)         │
                    ├─────────────────────┤
                    │ + getDeltaX()       │
                    │ + getDeltaY()       │
                    │ + getOpposite()     │
                    │ + isOpposite()      │
                    │ + fromString()      │
                    └─────────────────────┘

                    ┌─────────────────────┐
                    │       Point         │
                    ├─────────────────────┤
                    │ - x: int (final)    │
                    │ - y: int (final)    │
                    ├─────────────────────┤
                    │ + move(Direction)   │
                    │ + manhattanDist()   │
                    │ + isWithinBounds()  │
                    │ + isAdjacent()      │
                    │ + getDirectionTo()  │
                    └─────────────────────┘
```

## 📊 Time & Space Complexity Analysis

| Operation | Time Complexity | Space Complexity | Notes |
|-----------|----------------|------------------|--------|
| Snake Movement | O(1) | O(1) | Add head, remove tail |
| Snake Growth | O(1) | O(1) | Add head, keep tail |
| Self Collision Check | O(n) | O(1) | n = snake length |
| Wall Collision Check | O(1) | O(1) | Boundary comparison |
| Food Generation | O(1) avg, O(k) worst | O(1) | k = attempts to find valid position |
| Direction Change | O(1) | O(1) | Simple validation and update |
| Game State Update | O(n) | O(1) | Dominated by collision check |
| Render Game | O(n + w×h) | O(1) | n = snake length, w×h = board size |

**Overall Space Complexity**: O(n + w×h) where n = max snake length, w×h = board dimensions

## 🔄 Game State Flow

```
    Initialize Game
           │
           ▼
    ┌─────────────┐
    │ RunningState│
    └─────────────┘
           │
    ┌──────┼──────┐
    │      │      │
    ▼      ▼      ▼
┌─────┐ ┌─────┐ ┌─────────┐
│Pause│ │Move │ │Collision│
└─────┘ └─────┘ └─────────┘
    │      │          │
    ▼      │          ▼
┌─────────┐│     ┌─────────────┐
│PausedSt.││     │GameOverState│
└─────────┘│     └─────────────┘
    │      │          │
    │      ▼          │
    │  ┌─────────┐    │
    │  │Food Hit?│    │
    │  └─────────┘    │
    │      │ Yes       │
    │      ▼          │
    │  ┌─────────┐    │
    │  │Grow &   │    │
    │  │Score    │    │
    │  └─────────┘    │
    │      │          │
    └──────┼──────────┘
           ▼
    ┌─────────────┐
    │Continue Game│
    │or Restart   │
    └─────────────┘
```

## 🎮 Movement and Collision Logic

### Snake Movement Algorithm
```java
public MoveResult move(GameBoard gameBoard) {
    Point currentHead = getHead();
    Point newHead = currentHead.move(currentDirection);
    
    // 1. Check boundary collision
    if (!gameBoard.isValidPosition(newHead)) {
        return MoveResult.wallCollision(newHead, currentHead, currentDirection);
    }
    
    // 2. Check self collision
    if (checkSelfCollision(newHead)) {
        return MoveResult.selfCollision(newHead, currentHead, currentDirection);
    }
    
    // 3. Perform movement
    body.addFirst(newHead);
    if (!isGrowing) {
        body.removeLast(); // Remove tail
    }
    
    return MoveResult.success(newHead, currentHead, currentDirection);
}
```

### Collision Detection Details
```java
// Self Collision: Check if new head hits body (excluding tail if not growing)
private boolean checkSelfCollision(Point newHead) {
    int checkSize = isGrowing ? body.size() : body.size() - 1;
    Iterator<Point> iterator = body.iterator();
    for (int i = 0; i < checkSize && iterator.hasNext(); i++) {
        if (newHead.equals(iterator.next())) {
            return true;
        }
    }
    return false;
}

// Wall Collision: Simple boundary checking
public boolean isValidPosition(Point point) {
    return point.isWithinBounds(0, 0, width, height);
}
```

## ⚖️ Pros and Cons

### Pros
✅ **Clean State Management**: State pattern eliminates complex conditionals  
✅ **Thread Safety**: Concurrent data structures and proper locking  
✅ **Extensible Design**: Easy to add new game features and modes  
✅ **Immutable Objects**: Thread-safe Point and MoveResult classes  
✅ **Efficient Data Structures**: Deque for O(1) head/tail operations  
✅ **Input Validation**: Prevents invalid moves like immediate reversal  
✅ **Comprehensive Testing**: Full edge case coverage  
✅ **Real-time Performance**: Optimized for smooth gameplay  

### Cons
❌ **Memory Usage**: Stores entire snake body (could optimize for very long snakes)  
❌ **Collision Scaling**: Self-collision check is O(n) with snake length  
❌ **State Complexity**: Multiple patterns may be overkill for simple game  
❌ **GC Pressure**: Frequent Point object creation during movement  

## 🚀 Scalability & Extensibility

### Performance Optimizations
- **Spatial Partitioning**: Grid-based collision detection for large boards
- **Body Representation**: Circular buffer for memory efficiency
- **Object Pooling**: Reuse Point objects to reduce GC pressure
- **Predictive Input**: Queue multiple moves for smoother gameplay

### Feature Extensions
- **Multiple Snakes**: Multiplayer support with separate game states
- **Power-ups**: Special food items with temporary effects
- **Obstacles**: Static obstacles on the game board
- **AI Players**: Computer-controlled snakes with different strategies
- **Custom Game Modes**: Different rules, speeds, board layouts
- **Replay System**: Record and playback games using Command pattern
- **Tournament Mode**: Bracket-style competitions

### Technical Scalability
- **Network Play**: Client-server architecture for online multiplayer
- **Mobile Support**: Touch gesture command adapters
- **Database Integration**: High score persistence and player profiles
- **Analytics**: Gameplay metrics and user behavior tracking

## 🧪 Edge Cases Handled

1. **Rapid Direction Changes**: Queue system prevents lost inputs
2. **Reverse Direction**: Validation prevents snake from moving into itself
3. **Food on Snake**: Ensures food never spawns on snake body
4. **Boundary Wrapping**: Optional wraparound mode implementation
5. **Pause During Movement**: Clean state transitions
6. **Game Speed Changes**: Dynamic speed adjustment without breaking game state
7. **Memory Pressure**: Cleanup of old game states and objects
8. **Concurrent Access**: Thread-safe operations for potential multiplayer

## 🏭 Production Considerations

### Performance Metrics
- **Frame Rate**: Consistent 60 FPS gameplay
- **Input Latency**: < 50ms from input to screen update
- **Memory Usage**: < 100MB for typical gameplay session
- **CPU Usage**: < 5% on modern hardware

### Testing Strategy
- **Unit Tests**: Individual component testing (Snake, GameBoard, States)
- **Integration Tests**: Full gameplay scenarios and state transitions
- **Performance Tests**: Long gameplay sessions and memory leak detection
- **UI Tests**: Input handling and display rendering

### Monitoring
- **Game Analytics**: Score distribution, session length, common failure points
- **Performance Monitoring**: FPS drops, memory usage spikes
- **Error Tracking**: Crash reports and error scenarios
- **User Behavior**: Input patterns and gameplay preferences

## 🧰 How to Run

### Prerequisites
```bash
Java 11+
Maven 3.6+
```

### Run Demo
```bash
# Compile the project
mvn compile

# Run the demo (console version)
java -cp target/classes lld.snakegame.SnakeGameDemo

# Controls:
# W/↑ - Move Up
# S/↓ - Move Down  
# A/← - Move Left
# D/→ - Move Right
# P - Pause/Resume
# R - Restart (when game over)
# Q - Quit
```

### Sample Output
```
🐍 Welcome to Snake Game! 🐍
Use WASD or Arrow Keys to move, P to pause, Q to quit

████████████████████████
█                      █
█    🐍🐍🐍              █
█                      █
█          🍎           █
█                      █
████████████████████████

Score: 3 | Length: 4 | Speed: 200ms
State: RUNNING | Time: 00:45

🎵 Food eaten! Score: 4
⚡ Speed increased! New speed: 180ms
```

## 🔮 Future Enhancements

### Game Features
1. **Multiple Food Types**: Different foods with varying point values
2. **Special Powers**: Temporary abilities like passing through walls
3. **Level Progression**: Increasingly difficult levels with obstacles
4. **Boss Battles**: Special game modes with unique challenges
5. **Customizable Skins**: Different visual themes and snake appearances

### Technical Features
6. **Save/Load Game**: Persist game state across sessions
7. **Leaderboards**: Global and local high score tracking
8. **Achievements**: Unlock system for various accomplishments
9. **Statistics**: Detailed gameplay analytics and improvement tracking
10. **Mobile Port**: Touch controls and mobile-optimized UI

### Multiplayer Features
11. **Local Multiplayer**: Split-screen or shared board gameplay
12. **Online Multiplayer**: Real-time networked gameplay
13. **Tournaments**: Competitive gameplay with rankings
14. **Spectator Mode**: Watch other players' games
15. **Clan System**: Team-based competitions and challenges

## 📚 Alternative Implementations

### Different Data Structures
- **LinkedList**: Alternative to Deque for snake body
- **BitSet**: Memory-efficient board representation for large grids
- **RTree**: Spatial indexing for collision detection optimization

### Alternative Algorithms
- **A* Pathfinding**: For AI snake movement
- **Flood Fill**: For territory-based game modes
- **Genetic Algorithm**: For evolving AI strategies

### Performance Variants
- **Memory Pool**: Pre-allocated objects for zero-GC gameplay
- **Lock-Free**: Atomic operations for higher concurrency
- **SIMD**: Vector operations for batch collision detection