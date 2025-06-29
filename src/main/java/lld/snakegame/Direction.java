package lld.snakegame;

/**
 * Enumeration representing the four possible movement directions for the snake.
 * 
 * Each direction has an associated coordinate delta for movement calculation.
 * Includes utility methods for direction validation and opposite direction detection.
 * 
 * Design Pattern: Enum with behavior
 * Benefits:
 * - Type safety for directions
 * - Encapsulated movement logic
 * - Immutable direction state
 * - Built-in validation methods
 */
public enum Direction {
    UP(0, -1, "↑"),
    DOWN(0, 1, "↓"),
    LEFT(-1, 0, "←"),
    RIGHT(1, 0, "→");
    
    private final int deltaX;
    private final int deltaY;
    private final String symbol;
    
    Direction(int deltaX, int deltaY, String symbol) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.symbol = symbol;
    }
    
    /**
     * Gets the X-axis movement delta for this direction.
     * 
     * @return Delta X (-1, 0, or 1)
     */
    public int getDeltaX() {
        return deltaX;
    }
    
    /**
     * Gets the Y-axis movement delta for this direction.
     * 
     * @return Delta Y (-1, 0, or 1)
     */
    public int getDeltaY() {
        return deltaY;
    }
    
    /**
     * Gets the visual symbol for this direction.
     * 
     * @return Unicode arrow symbol
     */
    public String getSymbol() {
        return symbol;
    }
    
    /**
     * Gets the opposite direction.
     * Used to prevent the snake from immediately reversing into itself.
     * 
     * @return Opposite direction
     */
    public Direction getOpposite() {
        switch (this) {
            case UP: return DOWN;
            case DOWN: return UP;
            case LEFT: return RIGHT;
            case RIGHT: return LEFT;
            default: throw new IllegalStateException("Unknown direction: " + this);
        }
    }
    
    /**
     * Checks if this direction is opposite to the given direction.
     * 
     * @param other Direction to compare with
     * @return true if directions are opposite
     */
    public boolean isOpposite(Direction other) {
        return this.getOpposite() == other;
    }
    
    /**
     * Checks if this direction is horizontal (LEFT or RIGHT).
     * 
     * @return true if horizontal direction
     */
    public boolean isHorizontal() {
        return this == LEFT || this == RIGHT;
    }
    
    /**
     * Checks if this direction is vertical (UP or DOWN).
     * 
     * @return true if vertical direction
     */
    public boolean isVertical() {
        return this == UP || this == DOWN;
    }
    
    /**
     * Gets a direction from string input (case-insensitive).
     * Useful for parsing user input or configuration.
     * 
     * @param directionStr String representation of direction
     * @return Direction enum value
     * @throws IllegalArgumentException if string doesn't match any direction
     */
    public static Direction fromString(String directionStr) {
        if (directionStr == null) {
            throw new IllegalArgumentException("Direction string cannot be null");
        }
        
        switch (directionStr.trim().toUpperCase()) {
            case "UP":
            case "U":
            case "NORTH":
            case "N":
                return UP;
            case "DOWN":
            case "D":
            case "SOUTH":
            case "S":
                return DOWN;
            case "LEFT":
            case "L":
            case "WEST":
            case "W":
                return LEFT;
            case "RIGHT":
            case "R":
            case "EAST":
            case "E":
                return RIGHT;
            default:
                throw new IllegalArgumentException("Invalid direction: " + directionStr);
        }
    }
    
    /**
     * Gets all valid directions as an array.
     * 
     * @return Array of all directions
     */
    public static Direction[] getAllDirections() {
        return values();
    }
    
    /**
     * Gets a random direction.
     * Useful for AI or random movement testing.
     * 
     * @return Random direction
     */
    public static Direction getRandomDirection() {
        Direction[] directions = values();
        return directions[(int) (Math.random() * directions.length)];
    }
    
    @Override
    public String toString() {
        return symbol + " " + name();
    }
}