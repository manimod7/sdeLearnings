package lld.snakeladder;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Enum representing different game states
 */
enum GameState {
    WAITING_TO_START("Waiting to start"),
    IN_PROGRESS("Game in progress"),
    PAUSED("Game paused"),
    FINISHED("Game finished"),
    ABORTED("Game aborted");
    
    private final String description;
    
    GameState(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return description;
    }
}

/**
 * Game event types for observer pattern
 */
enum GameEventType {
    GAME_STARTED,
    TURN_COMPLETED,
    SNAKE_ENCOUNTERED,
    LADDER_CLIMBED,
    GAME_WON,
    GAME_PAUSED,
    GAME_RESUMED,
    GAME_RESET,
    PLAYER_ADDED,
    PLAYER_REMOVED
}

/**
 * Represents a game event for observer pattern
 */
class GameEvent {
    private final GameEventType type;
    private final Player player;
    private final String message;
    private final LocalDateTime timestamp;
    private final Map<String, Object> additionalData;
    
    public GameEvent(GameEventType type, Player player, String message) {
        this.type = type;
        this.player = player;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.additionalData = new HashMap<>();
    }
    
    public GameEvent(GameEventType type, Player player, String message, Map<String, Object> additionalData) {
        this.type = type;
        this.player = player;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.additionalData = new HashMap<>(additionalData);
    }
    
    // Getters
    public GameEventType getType() { return type; }
    public Player getPlayer() { return player; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public Map<String, Object> getAdditionalData() { return new HashMap<>(additionalData); }
    
    public void addData(String key, Object value) {
        additionalData.put(key, value);
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s: %s", timestamp, type, message);
    }
}

/**
 * Observer interface for game events
 */
interface GameObserver {
    void onGameEvent(GameEvent event);
}

/**
 * Console observer for game events
 */
class ConsoleGameObserver implements GameObserver {
    private final boolean verboseMode;
    
    public ConsoleGameObserver() {
        this.verboseMode = false;
    }
    
    public ConsoleGameObserver(boolean verboseMode) {
        this.verboseMode = verboseMode;
    }
    
    @Override
    public void onGameEvent(GameEvent event) {
        switch (event.getType()) {
            case GAME_STARTED:
                System.out.println("🎮 " + event.getMessage());
                break;
            case TURN_COMPLETED:
                System.out.println("🎲 " + event.getMessage());
                break;
            case SNAKE_ENCOUNTERED:
                System.out.println("🐍 " + event.getMessage());
                break;
            case LADDER_CLIMBED:
                System.out.println("🪜 " + event.getMessage());
                break;
            case GAME_WON:
                System.out.println("🏆 " + event.getMessage());
                break;
            case GAME_PAUSED:
                System.out.println("⏸️ " + event.getMessage());
                break;
            case GAME_RESUMED:
                System.out.println("▶️ " + event.getMessage());
                break;
            case GAME_RESET:
                System.out.println("🔄 " + event.getMessage());
                break;
            default:
                if (verboseMode) {
                    System.out.println("ℹ️ " + event.getMessage());
                }
                break;
        }
    }
}

/**
 * Statistics tracking for the game
 */
class GameStatistics {
    private LocalDateTime gameStartTime;
    private LocalDateTime gameEndTime;
    private int totalTurns;
    private final Map<String, Integer> playerTurns;
    private final Map<Integer, Integer> diceRollFrequency;
    private int snakeEncounters;
    private int ladderEncounters;
    private final List<PlayerMoveRecord> moveHistory;
    
    public GameStatistics() {
        this.playerTurns = new HashMap<>();
        this.diceRollFrequency = new HashMap<>();
        this.moveHistory = new ArrayList<>();
        this.totalTurns = 0;
        this.snakeEncounters = 0;
        this.ladderEncounters = 0;
    }
    
    public void recordGameStart() {
        this.gameStartTime = LocalDateTime.now();
        this.gameEndTime = null;
    }
    
    public void recordGameEnd(Player winner) {
        this.gameEndTime = LocalDateTime.now();
    }
    
    public void recordDiceRoll(int value) {
        diceRollFrequency.merge(value, 1, Integer::sum);
    }
    
    public void recordPlayerMove(Player player, int fromPosition, int toPosition) {
        totalTurns++;
        playerTurns.merge(player.getPlayerId(), 1, Integer::sum);
        moveHistory.add(new PlayerMoveRecord(player, fromPosition, toPosition, LocalDateTime.now()));
    }
    
    public void recordSnakeEncounter() {
        snakeEncounters++;
    }
    
    public void recordLadderEncounter() {
        ladderEncounters++;
    }
    
    public void reset() {
        gameStartTime = null;
        gameEndTime = null;
        totalTurns = 0;
        playerTurns.clear();
        diceRollFrequency.clear();
        snakeEncounters = 0;
        ladderEncounters = 0;
        moveHistory.clear();
    }
    
    // Getters and analysis methods
    public int getTotalTurns() { return totalTurns; }
    public int getSnakeEncounters() { return snakeEncounters; }
    public int getLadderEncounters() { return ladderEncounters; }
    public Map<String, Integer> getPlayerTurns() { return new HashMap<>(playerTurns); }
    public Map<Integer, Integer> getDiceRollFrequency() { return new HashMap<>(diceRollFrequency); }
    public List<PlayerMoveRecord> getMoveHistory() { return new ArrayList<>(moveHistory); }
    
    public long getGameDuration() {
        if (gameStartTime == null) return 0;
        LocalDateTime endTime = gameEndTime != null ? gameEndTime : LocalDateTime.now();
        return java.time.Duration.between(gameStartTime, endTime).toMillis();
    }
    
    public double getAverageDiceRoll() {
        return diceRollFrequency.entrySet().stream()
                .mapToDouble(entry -> entry.getKey() * entry.getValue())
                .sum() / diceRollFrequency.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    public int getMostFrequentDiceRoll() {
        return diceRollFrequency.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(0);
    }
    
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("📊 Game Statistics Report\n");
        report.append("========================\n");
        report.append("Total Turns: ").append(totalTurns).append("\n");
        report.append("Game Duration: ").append(getGameDuration()).append(" ms\n");
        report.append("Snake Encounters: ").append(snakeEncounters).append("\n");
        report.append("Ladder Encounters: ").append(ladderEncounters).append("\n");
        
        if (!diceRollFrequency.isEmpty()) {
            report.append("Average Dice Roll: ").append(String.format("%.2f", getAverageDiceRoll())).append("\n");
            report.append("Most Frequent Roll: ").append(getMostFrequentDiceRoll()).append("\n");
        }
        
        if (!playerTurns.isEmpty()) {
            report.append("\nPlayer Turn Distribution:\n");
            playerTurns.forEach((playerId, turns) -> 
                report.append("  ").append(playerId).append(": ").append(turns).append(" turns\n"));
        }
        
        return report.toString();
    }
}

/**
 * Records a player's move for statistics
 */
class PlayerMoveRecord {
    private final Player player;
    private final int fromPosition;
    private final int toPosition;
    private final LocalDateTime timestamp;
    
    public PlayerMoveRecord(Player player, int fromPosition, int toPosition, LocalDateTime timestamp) {
        this.player = player;
        this.fromPosition = fromPosition;
        this.toPosition = toPosition;
        this.timestamp = timestamp;
    }
    
    // Getters
    public Player getPlayer() { return player; }
    public int getFromPosition() { return fromPosition; }
    public int getToPosition() { return toPosition; }
    public LocalDateTime getTimestamp() { return timestamp; }
    
    public int getPositionChange() {
        return toPosition - fromPosition;
    }
    
    @Override
    public String toString() {
        return String.format("%s: %d → %d (change: %+d) at %s", 
                           player.getName(), fromPosition, toPosition, getPositionChange(), timestamp);
    }
}