package lld.snakeladder;

/**
 * Represents a player in the Snake and Ladder game
 * Immutable design for thread safety
 */
public class Player {
    private final String playerId;
    private final String name;
    private int currentPosition;
    
    public Player(String playerId, String name) {
        this.playerId = playerId;
        this.name = name;
        this.currentPosition = 0; // Starting position (before board)
    }
    
    /**
     * Move player to new position
     */
    public void moveTo(int newPosition) {
        this.currentPosition = newPosition;
    }
    
    /**
     * Check if player has won (reached position 100)
     */
    public boolean hasWon() {
        return currentPosition == 100;
    }
    
    // Getters
    public String getPlayerId() { return playerId; }
    public String getName() { return name; }
    public int getCurrentPosition() { return currentPosition; }
    
    @Override
    public String toString() {
        return String.format("Player{id='%s', name='%s', position=%d}", 
                           playerId, name, currentPosition);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Player player = (Player) obj;
        return playerId.equals(player.playerId);
    }
    
    @Override
    public int hashCode() {
        return playerId.hashCode();
    }
}