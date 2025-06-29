package lld.snakegame;

import java.util.Objects;

/**
 * Immutable Point class representing a coordinate on the game board.
 * 
 * Used to represent snake body segments, food positions, and any other
 * coordinate-based elements in the game.
 * 
 * Design Pattern: Value Object
 * Benefits:
 * - Immutable state prevents accidental modification
 * - Proper equals/hashCode for collections
 * - Thread-safe by design
 * - Clear coordinate representation
 */
public class Point {
    private final int x;
    private final int y;
    
    /**
     * Creates a new Point with the specified coordinates.
     * 
     * @param x X-coordinate
     * @param y Y-coordinate
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Gets the X-coordinate.
     * 
     * @return X-coordinate
     */
    public int getX() {
        return x;
    }
    
    /**
     * Gets the Y-coordinate.
     * 
     * @return Y-coordinate
     */
    public int getY() {
        return y;
    }
    
    /**
     * Creates a new Point by moving this point in the specified direction.
     * 
     * @param direction Direction to move
     * @return New Point after movement
     */
    public Point move(Direction direction) {
        return new Point(x + direction.getDeltaX(), y + direction.getDeltaY());
    }
    
    /**
     * Creates a new Point by adding the specified deltas.
     * 
     * @param deltaX X-axis movement
     * @param deltaY Y-axis movement
     * @return New Point after movement
     */
    public Point move(int deltaX, int deltaY) {
        return new Point(x + deltaX, y + deltaY);
    }
    
    /**
     * Calculates the Manhattan distance to another point.
     * 
     * @param other Other point
     * @return Manhattan distance
     */
    public int manhattanDistance(Point other) {
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
    }
    
    /**
     * Calculates the Euclidean distance to another point.
     * 
     * @param other Other point
     * @return Euclidean distance
     */
    public double euclideanDistance(Point other) {
        int dx = this.x - other.x;
        int dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    /**
     * Checks if this point is adjacent to another point.
     * Adjacent means exactly one unit away in cardinal directions.
     * 
     * @param other Other point
     * @return true if points are adjacent
     */
    public boolean isAdjacent(Point other) {
        return manhattanDistance(other) == 1;
    }
    
    /**
     * Checks if this point is within the specified rectangular bounds.
     * 
     * @param minX Minimum X coordinate (inclusive)
     * @param minY Minimum Y coordinate (inclusive)
     * @param maxX Maximum X coordinate (exclusive)
     * @param maxY Maximum Y coordinate (exclusive)
     * @return true if point is within bounds
     */
    public boolean isWithinBounds(int minX, int minY, int maxX, int maxY) {
        return x >= minX && x < maxX && y >= minY && y < maxY;
    }
    
    /**
     * Checks if this point is within the specified rectangular bounds.
     * 
     * @param width Width of the rectangular area
     * @param height Height of the rectangular area
     * @return true if point is within bounds (0,0) to (width-1, height-1)
     */
    public boolean isWithinBounds(int width, int height) {
        return isWithinBounds(0, 0, width, height);
    }
    
    /**
     * Gets the direction from this point to another point.
     * Only works for points that are exactly one unit apart in cardinal directions.
     * 
     * @param other Target point
     * @return Direction to the other point
     * @throws IllegalArgumentException if points are not adjacent in cardinal directions
     */
    public Direction getDirectionTo(Point other) {
        int dx = other.x - this.x;
        int dy = other.y - this.y;
        
        if (dx == 0 && dy == -1) return Direction.UP;
        if (dx == 0 && dy == 1) return Direction.DOWN;
        if (dx == -1 && dy == 0) return Direction.LEFT;
        if (dx == 1 && dy == 0) return Direction.RIGHT;
        
        throw new IllegalArgumentException(
            String.format("Points %s and %s are not adjacent in cardinal directions", this, other));
    }
    
    /**
     * Creates a copy of this point (since it's immutable, returns this).
     * 
     * @return This point (immutable)
     */
    public Point copy() {
        return this;
    }
    
    /**
     * Factory method to create origin point (0, 0).
     * 
     * @return Point at origin
     */
    public static Point origin() {
        return new Point(0, 0);
    }
    
    /**
     * Factory method to create a random point within specified bounds.
     * 
     * @param width Maximum X coordinate (exclusive)
     * @param height Maximum Y coordinate (exclusive)
     * @return Random point within bounds
     */
    public static Point random(int width, int height) {
        return new Point(
            (int) (Math.random() * width),
            (int) (Math.random() * height)
        );
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Point point = (Point) obj;
        return x == point.x && y == point.y;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
    
    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }
    
    /**
     * Returns a string representation suitable for debugging.
     * 
     * @return Detailed string representation
     */
    public String toDetailedString() {
        return String.format("Point{x=%d, y=%d}", x, y);
    }
}