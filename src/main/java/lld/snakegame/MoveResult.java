package lld.snakegame;

/**
 * Immutable result object for snake movement operations.
 * 
 * Contains information about the movement outcome, including collision type,
 * new position, and any additional metadata about the movement.
 * 
 * Design Pattern: Value Object Pattern
 * Benefits:
 * - Immutable state for thread safety
 * - Comprehensive movement information
 * - Clear separation of concerns
 * - Easy testing and debugging
 */
public class MoveResult {
    private final CollisionType type;
    private final Point newPosition;
    private final Point previousPosition;
    private final Direction movementDirection;
    private final boolean successful;
    private final String message;
    private final long timestamp;
    
    /**
     * Creates a new MoveResult.
     * 
     * @param type Type of collision that occurred
     * @param newPosition New position after movement
     * @param previousPosition Position before movement
     * @param movementDirection Direction of movement
     * @param successful Whether the movement was successful
     * @param message Additional information about the movement
     */
    public MoveResult(CollisionType type, Point newPosition, Point previousPosition,
                     Direction movementDirection, boolean successful, String message) {
        this.type = type != null ? type : CollisionType.NONE;
        this.newPosition = newPosition;
        this.previousPosition = previousPosition;
        this.movementDirection = movementDirection;
        this.successful = successful;
        this.message = message != null ? message : \"\";
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * Factory method for successful normal movement.
     * 
     * @param newPosition New position after movement
     * @param previousPosition Position before movement
     * @param direction Direction of movement
     * @return MoveResult for successful movement
     */
    public static MoveResult success(Point newPosition, Point previousPosition, Direction direction) {
        return new MoveResult(CollisionType.NONE, newPosition, previousPosition, 
                            direction, true, \"Normal movement\");
    }
    
    /**
     * Factory method for successful food collision.
     * 
     * @param newPosition New position after movement
     * @param previousPosition Position before movement
     * @param direction Direction of movement
     * @return MoveResult for food collision
     */
    public static MoveResult foodCollision(Point newPosition, Point previousPosition, Direction direction) {
        return new MoveResult(CollisionType.FOOD_COLLISION, newPosition, previousPosition,
                            direction, true, \"Food eaten - snake will grow\");
    }
    
    /**
     * Factory method for self collision.
     * 
     * @param attemptedPosition Position that would have caused collision
     * @param currentPosition Current snake head position
     * @param direction Direction of attempted movement
     * @return MoveResult for self collision
     */
    public static MoveResult selfCollision(Point attemptedPosition, Point currentPosition, Direction direction) {
        return new MoveResult(CollisionType.SELF_COLLISION, attemptedPosition, currentPosition,
                            direction, false, \"Snake collided with its own body\");
    }
    
    /**
     * Factory method for wall collision.
     * 
     * @param attemptedPosition Position that would be out of bounds
     * @param currentPosition Current snake head position
     * @param direction Direction of attempted movement
     * @return MoveResult for wall collision
     */
    public static MoveResult wallCollision(Point attemptedPosition, Point currentPosition, Direction direction) {
        return new MoveResult(CollisionType.WALL_COLLISION, attemptedPosition, currentPosition,
                            direction, false, \"Snake hit the boundary wall\");
    }
    
    /**
     * Factory method for invalid movement (like trying to reverse).
     * 
     * @param currentPosition Current snake head position
     * @param attemptedDirection Direction that was attempted
     * @return MoveResult for invalid movement
     */
    public static MoveResult invalidMove(Point currentPosition, Direction attemptedDirection) {
        return new MoveResult(CollisionType.NONE, currentPosition, currentPosition,
                            attemptedDirection, false, \"Invalid movement - cannot reverse direction\");
    }
    
    /**
     * Factory method for obstacle collision.
     * 
     * @param attemptedPosition Position with obstacle
     * @param currentPosition Current snake head position
     * @param direction Direction of attempted movement
     * @return MoveResult for obstacle collision
     */
    public static MoveResult obstacleCollision(Point attemptedPosition, Point currentPosition, Direction direction) {
        return new MoveResult(CollisionType.OBSTACLE_COLLISION, attemptedPosition, currentPosition,
                            direction, false, \"Snake hit an obstacle\");
    }
    
    // Getters
    public CollisionType getType() { return type; }
    public Point getNewPosition() { return newPosition; }
    public Point getPreviousPosition() { return previousPosition; }
    public Direction getMovementDirection() { return movementDirection; }
    public boolean isSuccessful() { return successful; }
    public String getMessage() { return message; }
    public long getTimestamp() { return timestamp; }
    
    /**
     * Checks if this movement result indicates game should end.
     * 
     * @return true if game should end
     */
    public boolean isGameEnding() {
        return type.isGameEnding();
    }
    
    /**
     * Checks if this movement result is a positive outcome.
     * 
     * @return true if result is positive (like eating food)
     */
    public boolean isPositive() {
        return type.isPositive();
    }
    
    /**
     * Checks if this movement result requires immediate action.
     * 
     * @return true if immediate action is required
     */
    public boolean requiresImmediateAction() {
        return type.requiresImmediateAction();
    }
    
    /**
     * Gets the distance moved (should be 1 for normal moves, 0 for failed moves).
     * 
     * @return Manhattan distance between old and new positions
     */
    public int getDistanceMoved() {
        if (newPosition == null || previousPosition == null) {
            return 0;
        }
        return successful ? previousPosition.manhattanDistance(newPosition) : 0;
    }
    
    /**
     * Gets a detailed description of the movement result.
     * 
     * @return Detailed description string
     */
    public String getDetailedDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(\"Movement: %s -> %s\", previousPosition, newPosition));
        sb.append(String.format(\" | Direction: %s\", movementDirection));
        sb.append(String.format(\" | Result: %s\", type.getDescription()));
        sb.append(String.format(\" | Success: %s\", successful));
        if (!message.isEmpty()) {
            sb.append(String.format(\" | %s\", message));
        }
        return sb.toString();
    }
    
    /**
     * Gets a summary suitable for logging.
     * 
     * @return Log-friendly summary
     */
    public String getLogSummary() {
        return String.format(\"%s: %s [%s]\", 
                           type.getEmoji(), 
                           type.getDescription(), 
                           successful ? \"SUCCESS\" : \"FAILED\");
    }
    
    @Override
    public String toString() {
        return String.format(\"MoveResult{type=%s, successful=%s, %s -> %s}\",
                           type, successful, previousPosition, newPosition);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        MoveResult that = (MoveResult) obj;
        return successful == that.successful &&
               type == that.type &&
               java.util.Objects.equals(newPosition, that.newPosition) &&
               java.util.Objects.equals(previousPosition, that.previousPosition) &&
               movementDirection == that.movementDirection &&
               message.equals(that.message);
    }
    
    @Override
    public int hashCode() {
        return java.util.Objects.hash(type, newPosition, previousPosition, 
                                    movementDirection, successful, message);
    }
}