package lld.snakeladder;

import java.util.*;

/**
 * Game engine for Snake and Ladder game
 * Template Method pattern for game flow management
 */
public class GameEngine {
    private final GameBoard board;
    private final Dice dice;
    private final Queue<Player> playerQueue;
    private final List<Player> players;
    private GameState currentState;
    private Player currentPlayer;
    private Player winner;
    private final List<GameObserver> observers;
    private final GameStatistics statistics;
    
    public GameEngine(GameBoard board, List<Player> players, Dice dice) {
        validateGameSetup(board, players, dice);
        this.board = board;
        this.dice = dice;
        this.players = new ArrayList<>(players);
        this.playerQueue = new LinkedList<>(players);
        this.currentState = GameState.WAITING_TO_START;
        this.observers = new ArrayList<>();
        this.statistics = new GameStatistics();
    }
    
    /**
     * Start the game
     */
    public void startGame() {
        if (currentState != GameState.WAITING_TO_START) {
            throw new IllegalStateException("Game cannot be started in current state: " + currentState);
        }
        
        currentState = GameState.IN_PROGRESS;
        currentPlayer = playerQueue.peek();
        statistics.recordGameStart();
        notifyObservers(new GameEvent(GameEventType.GAME_STARTED, currentPlayer, "Game started with " + players.size() + " players"));
        
        System.out.println("🎮 Snake and Ladder Game Started!");
        System.out.println("Players: " + players.stream().map(Player::getName).toList());
        board.displayBoard();
    }
    
    /**
     * Execute a turn for the current player
     */
    public TurnResult executeTurn() {
        if (currentState != GameState.IN_PROGRESS) {
            throw new IllegalStateException("Cannot execute turn in current state: " + currentState);
        }
        
        if (currentPlayer == null) {
            throw new IllegalStateException("No current player available");
        }
        
        // Roll dice
        int diceValue = dice.roll();
        statistics.recordDiceRoll(diceValue);
        
        // Calculate new position
        int oldPosition = currentPlayer.getCurrentPosition();
        int newPosition = oldPosition + diceValue;
        
        // Check if player exceeds board
        if (newPosition > 100) {
            String message = String.format("%s rolled %d but would exceed position 100. Turn skipped.", 
                                         currentPlayer.getName(), diceValue);
            TurnResult result = new TurnResult(currentPlayer, diceValue, oldPosition, oldPosition, message, false);
            notifyObservers(new GameEvent(GameEventType.TURN_COMPLETED, currentPlayer, message));
            advanceToNextPlayer();
            return result;
        }
        
        // Apply board elements (snakes/ladders)
        int finalPosition = board.getFinalPosition(newPosition);
        currentPlayer.moveTo(finalPosition);
        
        // Check for win condition
        boolean hasWon = currentPlayer.hasWon();
        if (hasWon) {
            winner = currentPlayer;
            currentState = GameState.FINISHED;
            statistics.recordGameEnd(winner);
        }
        
        // Create turn result
        String message = buildTurnMessage(currentPlayer, diceValue, oldPosition, newPosition, finalPosition);
        TurnResult result = new TurnResult(currentPlayer, diceValue, oldPosition, finalPosition, message, hasWon);
        
        // Record statistics
        statistics.recordPlayerMove(currentPlayer, oldPosition, finalPosition);
        if (finalPosition != newPosition) {
            BoardElement element = board.getElementAt(newPosition);
            if (element instanceof Snake) {
                statistics.recordSnakeEncounter();
            } else if (element instanceof Ladder) {
                statistics.recordLadderEncounter();
            }
        }
        
        // Notify observers
        GameEventType eventType = hasWon ? GameEventType.GAME_WON : GameEventType.TURN_COMPLETED;
        notifyObservers(new GameEvent(eventType, currentPlayer, message));
        
        // Advance to next player if game continues
        if (!hasWon) {
            advanceToNextPlayer();
        }
        
        return result;
    }
    
    /**
     * Get current game status
     */
    public GameStatus getGameStatus() {
        return new GameStatus(currentState, currentPlayer, winner, new ArrayList<>(players), 
                            statistics.getTotalTurns(), statistics.getGameDuration());
    }
    
    /**
     * Add game observer
     */
    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }
    
    /**
     * Remove game observer
     */
    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }
    
    /**
     * Get game statistics
     */
    public GameStatistics getStatistics() {
        return statistics;
    }
    
    /**
     * Reset game to initial state
     */
    public void resetGame() {
        currentState = GameState.WAITING_TO_START;
        currentPlayer = null;
        winner = null;
        playerQueue.clear();
        playerQueue.addAll(players);
        
        // Reset all player positions
        players.forEach(player -> player.moveTo(0));
        
        statistics.reset();
        notifyObservers(new GameEvent(GameEventType.GAME_RESET, null, "Game has been reset"));
    }
    
    private void validateGameSetup(GameBoard board, List<Player> players, Dice dice) {
        if (board == null) {
            throw new IllegalArgumentException("Game board cannot be null");
        }
        if (players == null || players.isEmpty()) {
            throw new IllegalArgumentException("At least one player is required");
        }
        if (players.size() > 6) {
            throw new IllegalArgumentException("Maximum 6 players allowed");
        }
        if (dice == null) {
            throw new IllegalArgumentException("Dice cannot be null");
        }
        
        // Check for duplicate players
        Set<String> playerIds = new HashSet<>();
        for (Player player : players) {
            if (!playerIds.add(player.getPlayerId())) {
                throw new IllegalArgumentException("Duplicate player ID: " + player.getPlayerId());
            }
        }
    }
    
    private void advanceToNextPlayer() {
        playerQueue.offer(playerQueue.poll());
        currentPlayer = playerQueue.peek();
    }
    
    private String buildTurnMessage(Player player, int diceValue, int oldPosition, int newPosition, int finalPosition) {
        StringBuilder message = new StringBuilder();
        message.append(String.format("%s rolled %d and moved from %d to %d", 
                                   player.getName(), diceValue, oldPosition, newPosition));
        
        if (finalPosition != newPosition) {
            BoardElement element = board.getElementAt(newPosition);
            if (element != null) {
                message.append(" → ").append(element.getEffectDescription());
                message.append(" → Final position: ").append(finalPosition);
            }
        }
        
        if (player.hasWon()) {
            message.append(" 🎉 WINNER! 🎉");
        }
        
        return message.toString();
    }
    
    private void notifyObservers(GameEvent event) {
        observers.forEach(observer -> observer.onGameEvent(event));
    }
}

/**
 * Represents the result of a player's turn
 */
class TurnResult {
    private final Player player;
    private final int diceValue;
    private final int startPosition;
    private final int endPosition;
    private final String message;
    private final boolean isWinningMove;
    
    public TurnResult(Player player, int diceValue, int startPosition, int endPosition, 
                     String message, boolean isWinningMove) {
        this.player = player;
        this.diceValue = diceValue;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.message = message;
        this.isWinningMove = isWinningMove;
    }
    
    // Getters
    public Player getPlayer() { return player; }
    public int getDiceValue() { return diceValue; }
    public int getStartPosition() { return startPosition; }
    public int getEndPosition() { return endPosition; }
    public String getMessage() { return message; }
    public boolean isWinningMove() { return isWinningMove; }
    
    @Override
    public String toString() {
        return message;
    }
}

/**
 * Current game status information
 */
class GameStatus {
    private final GameState state;
    private final Player currentPlayer;
    private final Player winner;
    private final List<Player> players;
    private final int totalTurns;
    private final long gameDurationMs;
    
    public GameStatus(GameState state, Player currentPlayer, Player winner, List<Player> players, 
                     int totalTurns, long gameDurationMs) {
        this.state = state;
        this.currentPlayer = currentPlayer;
        this.winner = winner;
        this.players = players;
        this.totalTurns = totalTurns;
        this.gameDurationMs = gameDurationMs;
    }
    
    // Getters
    public GameState getState() { return state; }
    public Player getCurrentPlayer() { return currentPlayer; }
    public Player getWinner() { return winner; }
    public List<Player> getPlayers() { return new ArrayList<>(players); }
    public int getTotalTurns() { return totalTurns; }
    public long getGameDurationMs() { return gameDurationMs; }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Game Status: ").append(state);
        if (currentPlayer != null) {
            sb.append(", Current Player: ").append(currentPlayer.getName());
        }
        if (winner != null) {
            sb.append(", Winner: ").append(winner.getName());
        }
        sb.append(", Turns: ").append(totalTurns);
        sb.append(", Duration: ").append(gameDurationMs).append("ms");
        return sb.toString();
    }
}