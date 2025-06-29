package lld.snakegame;

/**
 * Enumeration representing different types of collisions in the snake game.
 * 
 * Used to categorize collision events and determine appropriate game responses.
 * Each collision type has different implications for game state and scoring.
 * 
 * Design Pattern: Enum with behavior
 * Benefits:
 * - Type safety for collision handling
 * - Clear collision categorization
 * - Extensible for new collision types
 * - Built-in severity assessment
 */
public enum CollisionType {
    
    /**
     * No collision occurred - normal movement.
     */
    NONE(\"No collision\", false, 0),
    
    /**
     * Snake collided with its own body.
     * This is a game-ending collision.
     */
    SELF_COLLISION(\"Self collision\", true, 3),
    
    /**
     * Snake collided with a wall or boundary.
     * This is a game-ending collision.
     */
    WALL_COLLISION(\"Wall collision\", true, 2),
    
    /**
     * Snake collided with food (positive collision).
     * This results in growth and score increase.
     */
    FOOD_COLLISION(\"Food eaten\", false, 1),
    
    /**
     * Snake collided with an obstacle.
     * This is a game-ending collision.
     */
    OBSTACLE_COLLISION(\"Obstacle collision\", true, 2),
    
    /**
     * Snake collided with a power-up item.
     * This results in special effects.
     */
    POWERUP_COLLISION(\"Power-up collected\", false, 1);
    
    private final String description;
    private final boolean isGameEnding;
    private final int severity; // 0=none, 1=positive, 2=negative, 3=critical
    
    CollisionType(String description, boolean isGameEnding, int severity) {
        this.description = description;
        this.isGameEnding = isGameEnding;
        this.severity = severity;
    }
    
    /**
     * Gets the human-readable description of this collision type.
     * 
     * @return Collision description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Checks if this collision type ends the game.
     * 
     * @return true if collision ends the game
     */
    public boolean isGameEnding() {
        return isGameEnding;
    }
    
    /**
     * Gets the severity level of this collision.
     * 
     * @return Severity (0=none, 1=positive, 2=negative, 3=critical)
     */
    public int getSeverity() {
        return severity;
    }
    
    /**
     * Checks if this is a positive collision (like eating food).
     * 
     * @return true if collision is beneficial
     */
    public boolean isPositive() {
        return severity == 1 && !isGameEnding;
    }
    
    /**
     * Checks if this is a negative collision (like hitting walls).
     * 
     * @return true if collision is harmful
     */
    public boolean isNegative() {
        return severity >= 2;
    }
    
    /**
     * Checks if this collision requires immediate game state change.
     * 
     * @return true if collision requires immediate handling
     */
    public boolean requiresImmediateAction() {
        return isGameEnding || isPositive();
    }
    
    /**
     * Gets the emoji representation of this collision type.
     * 
     * @return Emoji string
     */
    public String getEmoji() {
        switch (this) {
            case NONE: return \"✅\";
            case SELF_COLLISION: return \"🐍💀\";
            case WALL_COLLISION: return \"🧱💥\";
            case FOOD_COLLISION: return \"🍎😋\";
            case OBSTACLE_COLLISION: return \"🚧💥\";
            case POWERUP_COLLISION: return \"⭐✨\";
            default: return \"❓\";
        }
    }
    
    /**
     * Gets a colored console representation for logging.
     * 
     * @return Colored string for console output
     */
    public String getColoredString() {
        switch (this) {
            case NONE: return \"\\u001B[32m\" + description + \"\\u001B[0m\"; // Green
            case FOOD_COLLISION:
            case POWERUP_COLLISION: return \"\\u001B[36m\" + description + \"\\u001B[0m\"; // Cyan
            case WALL_COLLISION:
            case OBSTACLE_COLLISION: return \"\\u001B[31m\" + description + \"\\u001B[0m\"; // Red
            case SELF_COLLISION: return \"\\u001B[35m\" + description + \"\\u001B[0m\"; // Magenta
            default: return description;
        }
    }
    
    /**
     * Gets collision type from string (case-insensitive).
     * 
     * @param collisionStr String representation
     * @return CollisionType enum value
     * @throws IllegalArgumentException if string doesn't match any type
     */
    public static CollisionType fromString(String collisionStr) {
        if (collisionStr == null) {
            return NONE;
        }
        
        switch (collisionStr.trim().toUpperCase()) {
            case \"NONE\":
            case \"NO_COLLISION\":
                return NONE;
            case \"SELF\":
            case \"SELF_COLLISION\":
            case \"BODY\":
                return SELF_COLLISION;
            case \"WALL\":
            case \"WALL_COLLISION\":
            case \"BOUNDARY\":
                return WALL_COLLISION;
            case \"FOOD\":
            case \"FOOD_COLLISION\":
            case \"EAT\":
                return FOOD_COLLISION;
            case \"OBSTACLE\":
            case \"OBSTACLE_COLLISION\":
                return OBSTACLE_COLLISION;
            case \"POWERUP\":
            case \"POWERUP_COLLISION\":
            case \"POWER_UP\":
                return POWERUP_COLLISION;
            default:
                throw new IllegalArgumentException(\"Invalid collision type: \" + collisionStr);
        }
    }
    
    /**
     * Gets all game-ending collision types.
     * 
     * @return Array of game-ending collision types
     */
    public static CollisionType[] getGameEndingTypes() {
        return new CollisionType[]{SELF_COLLISION, WALL_COLLISION, OBSTACLE_COLLISION};
    }
    
    /**
     * Gets all positive collision types.
     * 
     * @return Array of positive collision types
     */
    public static CollisionType[] getPositiveTypes() {
        return new CollisionType[]{FOOD_COLLISION, POWERUP_COLLISION};
    }
    
    @Override
    public String toString() {
        return getEmoji() + \" \" + description;
    }
}