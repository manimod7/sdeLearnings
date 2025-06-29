package lld.snakeladder;

/**
 * Abstract base class for board elements (Snake and Ladder)
 * Template Method pattern for common element behavior
 */
public abstract class BoardElement {
    protected final int startPosition;
    protected final int endPosition;
    
    public BoardElement(int startPosition, int endPosition) {
        validatePositions(startPosition, endPosition);
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }
    
    /**
     * Template method for position validation
     */
    protected abstract void validatePositions(int start, int end);
    
    /**
     * Get the destination position for a player
     * @param currentPosition Player's current position
     * @return New position after applying board element effect
     */
    public int getDestination(int currentPosition) {
        if (currentPosition == startPosition) {
            return endPosition;
        }
        return currentPosition;
    }
    
    /**
     * Check if this element affects the given position
     */
    public boolean affects(int position) {
        return position == startPosition;
    }
    
    /**
     * Get the type of board element
     */
    public abstract BoardElementType getType();
    
    /**
     * Get effect description
     */
    public abstract String getEffectDescription();
    
    // Getters
    public int getStartPosition() { return startPosition; }
    public int getEndPosition() { return endPosition; }
    
    @Override
    public String toString() {
        return String.format("%s{start=%d, end=%d}", 
                           getType(), startPosition, endPosition);
    }
}

/**
 * Snake implementation - moves player down
 */
class Snake extends BoardElement {
    public Snake(int head, int tail) {
        super(head, tail);
    }
    
    @Override
    protected void validatePositions(int start, int end) {
        if (start <= end) {
            throw new IllegalArgumentException("Snake head must be higher than tail");
        }
        if (start < 2 || start > 99) {
            throw new IllegalArgumentException("Snake head must be between 2 and 99");
        }
        if (end < 1 || end >= start) {
            throw new IllegalArgumentException("Snake tail must be between 1 and head-1");
        }
    }
    
    @Override
    public BoardElementType getType() {
        return BoardElementType.SNAKE;
    }
    
    @Override
    public String getEffectDescription() {
        return String.format("Snake bite! Slide down from %d to %d", startPosition, endPosition);
    }
}

/**
 * Ladder implementation - moves player up
 */
class Ladder extends BoardElement {
    public Ladder(int bottom, int top) {
        super(bottom, top);
    }
    
    @Override
    protected void validatePositions(int start, int end) {
        if (start >= end) {
            throw new IllegalArgumentException("Ladder bottom must be lower than top");
        }
        if (start < 1 || start > 98) {
            throw new IllegalArgumentException("Ladder bottom must be between 1 and 98");
        }
        if (end <= start || end > 100) {
            throw new IllegalArgumentException("Ladder top must be between bottom+1 and 100");
        }
    }
    
    @Override
    public BoardElementType getType() {
        return BoardElementType.LADDER;
    }
    
    @Override
    public String getEffectDescription() {
        return String.format("Ladder climb! Move up from %d to %d", startPosition, endPosition);
    }
}

/**
 * Enum for board element types
 */
enum BoardElementType {
    SNAKE, LADDER
}