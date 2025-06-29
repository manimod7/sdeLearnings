package lld.snakeladder;

import java.util.*;

/**
 * Main game controller for Snake and Ladder Game
 * Facade pattern providing simplified interface to complex game system
 */
public class SnakeLadderGame {
    private final GameEngine gameEngine;
    private final GameBoard gameBoard;
    private final List<Player> players;
    private final Dice dice;
    
    /**
     * Create a new Snake and Ladder game with default configuration
     */
    public SnakeLadderGame(List<String> playerNames) {
        this(playerNames, GameBoard.createStandardBoard(), DiceFactory.createStandardDice());
    }
    
    /**
     * Create a new Snake and Ladder game with custom configuration
     */
    public SnakeLadderGame(List<String> playerNames, GameBoard customBoard, Dice customDice) {
        validatePlayerNames(playerNames);
        
        this.gameBoard = customBoard;
        this.dice = customDice;
        this.players = createPlayers(playerNames);
        this.gameEngine = new GameEngine(gameBoard, players, dice);
        
        // Add console observer for game events
        gameEngine.addObserver(new ConsoleGameObserver(true));
    }
    
    /**
     * Start the game
     */
    public void startGame() {
        gameEngine.startGame();
        displayCurrentPositions();
    }
    
    /**
     * Play a single turn
     */
    public TurnResult playTurn() {
        TurnResult result = gameEngine.executeTurn();
        displayCurrentPositions();
        
        if (result.isWinningMove()) {
            displayGameSummary();
        }
        
        return result;
    }
    
    /**
     * Play the entire game automatically until someone wins
     */
    public Player playFullGame() {
        startGame();
        
        while (!isGameFinished()) {
            playTurn();
            
            // Add a small delay for better visualization (optional)
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        return getWinner();
    }
    
    /**
     * Play game with interactive mode (manual turn advancement)
     */
    public void playInteractiveGame() {
        Scanner scanner = new Scanner(System.in);
        startGame();
        
        while (!isGameFinished()) {
            System.out.println("\nPress Enter to roll dice for " + getCurrentPlayer().getName() + " (or 'quit' to exit):");
            String input = scanner.nextLine().trim();
            
            if ("quit".equalsIgnoreCase(input)) {
                System.out.println("Game aborted by user.");
                break;
            }
            
            playTurn();
        }
        
        if (isGameFinished()) {
            System.out.println("\n🎉 Game completed! Check the summary above.");
        }
    }
    
    /**
     * Get current game status
     */
    public GameStatus getGameStatus() {
        return gameEngine.getGameStatus();
    }
    
    /**
     * Check if game is finished
     */
    public boolean isGameFinished() {
        return getGameStatus().getState() == GameState.FINISHED;
    }
    
    /**
     * Get the winner (null if game not finished)
     */
    public Player getWinner() {
        return getGameStatus().getWinner();
    }
    
    /**
     * Get current player whose turn it is
     */
    public Player getCurrentPlayer() {
        return getGameStatus().getCurrentPlayer();
    }
    
    /**
     * Get all players
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }
    
    /**
     * Get game statistics
     */
    public GameStatistics getStatistics() {
        return gameEngine.getStatistics();
    }
    
    /**
     * Reset game to initial state
     */
    public void resetGame() {
        gameEngine.resetGame();
        System.out.println("🔄 Game has been reset. Call startGame() to begin again.");
    }
    
    /**
     * Add a game observer
     */
    public void addObserver(GameObserver observer) {
        gameEngine.addObserver(observer);
    }
    
    /**
     * Remove a game observer
     */
    public void removeObserver(GameObserver observer) {
        gameEngine.removeObserver(observer);
    }
    
    /**
     * Get the game board
     */
    public GameBoard getGameBoard() {
        return gameBoard;
    }
    
    private void validatePlayerNames(List<String> playerNames) {
        if (playerNames == null || playerNames.isEmpty()) {
            throw new IllegalArgumentException("At least one player name is required");
        }
        
        if (playerNames.size() > 6) {
            throw new IllegalArgumentException("Maximum 6 players allowed");
        }
        
        // Check for duplicate names
        Set<String> uniqueNames = new HashSet<>(playerNames);
        if (uniqueNames.size() != playerNames.size()) {
            throw new IllegalArgumentException("Duplicate player names are not allowed");
        }
        
        // Check for empty or null names
        for (String name : playerNames) {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Player names cannot be empty");
            }
        }
    }
    
    private List<Player> createPlayers(List<String> playerNames) {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < playerNames.size(); i++) {
            String name = playerNames.get(i).trim();
            String playerId = "player_" + (i + 1);
            players.add(new Player(playerId, name));
        }
        return players;
    }
    
    private void displayCurrentPositions() {
        System.out.println("\n📍 Current Positions:");
        System.out.println("==================");
        for (Player player : players) {
            String status = player.hasWon() ? " 🏆 WINNER!" : "";
            System.out.println(String.format("%-15s: Position %2d%s", 
                                            player.getName(), player.getCurrentPosition(), status));
        }
    }
    
    private void displayGameSummary() {
        GameStatus status = getGameStatus();
        GameStatistics stats = getStatistics();
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("🎮 GAME COMPLETED!");
        System.out.println("=".repeat(50));
        System.out.println("🏆 Winner: " + status.getWinner().getName());
        System.out.println("🎲 Total Turns: " + status.getTotalTurns());
        System.out.println("⏱️ Game Duration: " + (status.getGameDurationMs() / 1000.0) + " seconds");
        System.out.println("🐍 Snake Encounters: " + stats.getSnakeEncounters());
        System.out.println("🪜 Ladder Climbs: " + stats.getLadderEncounters());
        
        if (stats.getTotalTurns() > 0) {
            System.out.println("📊 Average Dice Roll: " + String.format("%.2f", stats.getAverageDiceRoll()));
        }
        
        System.out.println("\n📈 Player Turn Distribution:");
        Map<String, Integer> playerTurns = stats.getPlayerTurns();
        for (Player player : players) {
            int turns = playerTurns.getOrDefault(player.getPlayerId(), 0);
            System.out.println(String.format("   %-15s: %d turns", player.getName(), turns));
        }
        
        System.out.println("=".repeat(50));
    }
    
    /**
     * Create a quick demo game
     */
    public static SnakeLadderGame createDemoGame() {
        List<String> playerNames = Arrays.asList("Alice", "Bob", "Charlie");
        return new SnakeLadderGame(playerNames);
    }
    
    /**
     * Create a custom game with specified snakes and ladders
     */
    public static SnakeLadderGame createCustomGame(List<String> playerNames, 
                                                  List<Snake> snakes, 
                                                  List<Ladder> ladders) {
        GameBoard customBoard = GameBoard.createCustomBoard(snakes, ladders);
        return new SnakeLadderGame(playerNames, customBoard, DiceFactory.createStandardDice());
    }
    
    /**
     * Main method for running a quick demo
     */
    public static void main(String[] args) {
        System.out.println("🎮 Snake and Ladder Game Demo");
        System.out.println("============================");
        
        // Create a demo game
        SnakeLadderGame game = createDemoGame();
        
        // Display game board
        game.getGameBoard().displayBoard();
        
        // Play interactive game
        game.playInteractiveGame();
        
        // Display final statistics
        System.out.println("\n" + game.getStatistics().generateReport());
    }
}