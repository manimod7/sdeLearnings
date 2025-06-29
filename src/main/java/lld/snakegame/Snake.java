package lld.snakegame;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Snake class representing the player-controlled snake entity.
 * 
 * Manages snake body, movement, growth, and collision detection.
 * Thread-safe implementation for potential multiplayer scenarios.
 * 
 * Key Features:
 * - Dynamic body management with efficient add/remove operations
 * - Direction validation to prevent immediate reversal
 * - Collision detection with self and boundaries
 * - Growth mechanism when consuming food
 * - Performance optimized for smooth gameplay
 */
public class Snake {
    private final Deque<Point> body;
    private Direction currentDirection;
    private Direction nextDirection;
    private boolean isGrowing;
    private final ReentrantReadWriteLock lock;
    private final int initialLength;
    private int growthPending;
    
    // Performance tracking
    private long lastMoveTime;
    private int totalMoves;
    
    /**
     * Creates a new Snake with default starting position and direction.
     * Snake starts at center of a 20x20 board moving RIGHT.
     */
    public Snake() {
        this(new Point(10, 10), Direction.RIGHT, 3);
    }
    
    /**
     * Creates a new Snake with specified starting position, direction, and length.
     * 
     * @param startPosition Starting position for snake head
     * @param startDirection Initial movement direction
     * @param initialLength Initial length of the snake
     */
    public Snake(Point startPosition, Direction startDirection, int initialLength) {
        if (startPosition == null) {
            throw new IllegalArgumentException(\"Start position cannot be null\");
        }
        if (startDirection == null) {
            throw new IllegalArgumentException(\"Start direction cannot be null\");
        }
        if (initialLength < 1) {
            throw new IllegalArgumentException(\"Initial length must be at least 1\");
        }
        
        this.body = new ArrayDeque<>();
        this.currentDirection = startDirection;
        this.nextDirection = startDirection;
        this.isGrowing = false;
        this.lock = new ReentrantReadWriteLock();
        this.initialLength = initialLength;
        this.growthPending = 0;
        this.lastMoveTime = System.currentTimeMillis();
        this.totalMoves = 0;
        
        // Initialize snake body
        initializeBody(startPosition, startDirection, initialLength);
    }
    
    /**
     * Initializes the snake body with the specified parameters.
     * 
     * @param startPosition Starting position for snake head
     * @param direction Direction snake is facing
     * @param length Initial length
     */
    private void initializeBody(Point startPosition, Direction direction, int length) {
        // Create snake body from head to tail
        Point currentPos = startPosition;
        
        for (int i = 0; i < length; i++) {
            body.addLast(currentPos);
            // Move in opposite direction to create body
            currentPos = currentPos.move(direction.getOpposite());
        }
    }
    
    /**
     * Moves the snake in the current direction.
     * 
     * @param gameBoard Game board for boundary checking
     * @return MoveResult indicating outcome of the movement
     */
    public MoveResult move(GameBoard gameBoard) {
        lock.writeLock().lock();
        try {
            // Update direction if a new direction was queued
            if (nextDirection != currentDirection && isValidDirectionChange(nextDirection)) {
                currentDirection = nextDirection;
            }
            
            Point currentHead = getHead();
            Point newHead = currentHead.move(currentDirection);
            
            // Check for boundary collision
            if (!gameBoard.isValidPosition(newHead)) {
                return MoveResult.wallCollision(newHead, currentHead, currentDirection);
            }
            
            // Check for self collision (exclude tail unless growing)
            if (checkSelfCollision(newHead)) {
                return MoveResult.selfCollision(newHead, currentHead, currentDirection);
            }
            
            // Perform the movement
            body.addFirst(newHead);
            
            // Handle growth
            if (isGrowing || growthPending > 0) {
                if (growthPending > 0) {
                    growthPending--;
                }
                isGrowing = false;
            } else {
                // Remove tail if not growing
                body.removeLast();
            }
            
            // Update movement tracking
            lastMoveTime = System.currentTimeMillis();
            totalMoves++;
            
            return MoveResult.success(newHead, currentHead, currentDirection);
            
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Checks if the new head position collides with the snake's body.
     * 
     * @param newHead New head position to check
     * @return true if collision detected
     */
    private boolean checkSelfCollision(Point newHead) {
        // Check collision with body (excluding tail which will be removed)
        int bodySize = body.size();
        int checkSize = isGrowing || growthPending > 0 ? bodySize : bodySize - 1;
        
        Iterator<Point> iterator = body.iterator();
        for (int i = 0; i < checkSize && iterator.hasNext(); i++) {
            if (newHead.equals(iterator.next())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Queues the snake to grow on the next movement.
     * Can be called multiple times to queue multiple growth segments.
     */
    public void grow() {
        lock.writeLock().lock();
        try {
            growthPending++;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Immediately grows the snake by the specified amount.
     * 
     * @param segments Number of segments to grow
     */
    public void grow(int segments) {
        if (segments <= 0) return;
        
        lock.writeLock().lock();
        try {
            growthPending += segments;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Changes the snake's direction.
     * Validates that the new direction is not opposite to current direction.
     * 
     * @param newDirection New direction to move
     * @return true if direction change was accepted
     */
    public boolean changeDirection(Direction newDirection) {
        if (newDirection == null) {
            return false;
        }
        
        lock.readLock().lock();
        try {
            if (isValidDirectionChange(newDirection)) {
                nextDirection = newDirection;
                return true;
            }
            return false;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Validates if a direction change is allowed.
     * Prevents immediate reversal into the snake's body.
     * 
     * @param newDirection Direction to validate
     * @return true if direction change is valid
     */
    private boolean isValidDirectionChange(Direction newDirection) {
        // Allow any direction if snake has only one segment
        if (body.size() <= 1) {
            return true;
        }
        
        // Prevent immediate reversal
        return !newDirection.isOpposite(currentDirection);
    }
    
    /**
     * Gets the snake's head position.
     * 
     * @return Head position
     */
    public Point getHead() {
        lock.readLock().lock();
        try {
            return body.isEmpty() ? null : body.peekFirst();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Gets the snake's tail position.
     * 
     * @return Tail position
     */
    public Point getTail() {
        lock.readLock().lock();
        try {
            return body.isEmpty() ? null : body.peekLast();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Gets a copy of the snake's body.
     * 
     * @return List of body segments from head to tail
     */
    public List<Point> getBody() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(body);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Gets the current length of the snake.
     * 
     * @return Snake length
     */
    public int getLength() {
        lock.readLock().lock();
        try {
            return body.size();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Gets the current direction of the snake.
     * 
     * @return Current direction
     */
    public Direction getCurrentDirection() {
        lock.readLock().lock();
        try {
            return currentDirection;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Gets the next queued direction.
     * 
     * @return Next direction
     */
    public Direction getNextDirection() {
        lock.readLock().lock();
        try {
            return nextDirection;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Checks if the snake is currently growing.
     * 
     * @return true if snake will grow on next move
     */
    public boolean isGrowing() {
        lock.readLock().lock();
        try {
            return isGrowing || growthPending > 0;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Gets the number of pending growth segments.
     * 
     * @return Number of segments that will be added
     */
    public int getGrowthPending() {
        lock.readLock().lock();
        try {
            return growthPending;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Checks if a given point is part of the snake's body.
     * 
     * @param point Point to check
     * @return true if point is part of snake
     */
    public boolean contains(Point point) {
        if (point == null) return false;
        
        lock.readLock().lock();
        try {
            return body.contains(point);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Resets the snake to its initial state.
     * 
     * @param startPosition New starting position
     * @param startDirection New starting direction
     */
    public void reset(Point startPosition, Direction startDirection) {
        lock.writeLock().lock();
        try {
            body.clear();
            this.currentDirection = startDirection;
            this.nextDirection = startDirection;
            this.isGrowing = false;
            this.growthPending = 0;
            this.totalMoves = 0;
            this.lastMoveTime = System.currentTimeMillis();
            
            initializeBody(startPosition, startDirection, initialLength);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Gets movement statistics.
     * 
     * @return Snake movement statistics
     */
    public SnakeStats getStats() {
        lock.readLock().lock();
        try {
            return new SnakeStats(
                getLength(),
                totalMoves,
                getLength() - initialLength, // Food eaten
                currentDirection,
                lastMoveTime
            );
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Gets the snake's initial length.
     * 
     * @return Initial length
     */
    public int getInitialLength() {
        return initialLength;
    }
    
    /**
     * Calculates the snake's growth rate.
     * 
     * @return Segments grown per move (average)
     */
    public double getGrowthRate() {
        lock.readLock().lock();
        try {
            if (totalMoves == 0) return 0.0;
            return (double) (getLength() - initialLength) / totalMoves;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public String toString() {
        lock.readLock().lock();
        try {
            return String.format(\"Snake{length=%d, head=%s, direction=%s, growing=%s}\",
                               getLength(), getHead(), currentDirection, isGrowing());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Inner class for snake statistics.
     */
    public static class SnakeStats {
        private final int length;
        private final int totalMoves;
        private final int foodEaten;
        private final Direction currentDirection;
        private final long lastMoveTime;
        
        public SnakeStats(int length, int totalMoves, int foodEaten, 
                         Direction currentDirection, long lastMoveTime) {
            this.length = length;
            this.totalMoves = totalMoves;
            this.foodEaten = foodEaten;
            this.currentDirection = currentDirection;
            this.lastMoveTime = lastMoveTime;
        }
        
        // Getters
        public int getLength() { return length; }
        public int getTotalMoves() { return totalMoves; }
        public int getFoodEaten() { return foodEaten; }
        public Direction getCurrentDirection() { return currentDirection; }
        public long getLastMoveTime() { return lastMoveTime; }
        
        @Override
        public String toString() {
            return String.format(\"SnakeStats{length=%d, moves=%d, food=%d, direction=%s}\",
                               length, totalMoves, foodEaten, currentDirection);
        }
    }
}