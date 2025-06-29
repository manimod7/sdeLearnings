package lld.snakegame;

/**
 * State implementation for when the game has ended.
 * 
 * In this state:
 * - Snake movement is disabled
 * - Game logic updates are suspended
 * - Only restart/quit commands are accepted
 * - Final score and statistics are available
 * 
 * This state handles game termination and provides options for restarting.
 */
public class GameOverState implements GameState {
    
    private static final GameOverState INSTANCE = new GameOverState();
    
    private CollisionType gameOverReason;
    private long gameOverTime;
    private boolean isWin;
    
    /**
     * Gets the singleton instance of GameOverState.
     * 
     * @return GameOverState instance
     */
    public static GameOverState getInstance() {
        return INSTANCE;
    }
    
    private GameOverState() {
        // Private constructor for singleton
        this.gameOverReason = CollisionType.NONE;
        this.isWin = false;
    }
    
    @Override
    public void update(SnakeGame game) {
        // No game logic updates in game over state
        // Could handle animations or cleanup here
    }
    
    @Override
    public void handleInput(SnakeGame game, Command command) {
        // Only handle specific commands in game over state
        if (command instanceof RestartCommand) {
            // Restart the game
            command.execute(game);
        } else if (command instanceof QuitCommand) {
            // Allow quitting from game over state
            command.execute(game);
        }
        // Ignore movement and pause commands
    }
    
    @Override
    public boolean canMove() {
        return false;
    }
    
    @Override
    public boolean isGameOver() {
        return true;
    }
    
    @Override
    public boolean isPaused() {
        return false;
    }
    
    @Override
    public boolean isRunning() {
        return false;
    }
    
    @Override
    public String getStateName() {
        return "GAME_OVER";
    }
    
    @Override
    public void onEnter(SnakeGame game) {
        // Stop the game timer
        game.stopGameTimer();
        
        // Record game over time
        this.gameOverTime = System.currentTimeMillis();
        
        // Calculate and display final statistics
        displayGameOverStatistics(game);
        
        // Notify observers
        game.notifyStateChanged(this);
        
        // Check for high score
        checkHighScore(game);
    }
    
    @Override
    public void onExit(SnakeGame game) {
        // Reset game over state for next game
        this.gameOverReason = CollisionType.NONE;
        this.isWin = false;
        
        // Could save game statistics here
        saveGameStatistics(game);
    }
    
    /**
     * Sets the reason for game over.
     * 
     * @param reason Collision type that caused game over
     */
    public void setGameOverReason(CollisionType reason) {
        this.gameOverReason = reason;
        this.isWin = false;
    }
    
    /**
     * Sets the game as won (special win condition met).
     */
    public void setGameWon() {
        this.gameOverReason = CollisionType.NONE;
        this.isWin = true;
    }
    
    /**
     * Gets the reason for game over.
     * 
     * @return Collision type that caused game over
     */
    public CollisionType getGameOverReason() {
        return gameOverReason;
    }
    
    /**
     * Checks if the game was won rather than lost.
     * 
     * @return true if game was won
     */
    public boolean isWin() {
        return isWin;
    }
    
    /**
     * Gets the time when game over occurred.
     * 
     * @return Game over timestamp
     */
    public long getGameOverTime() {
        return gameOverTime;
    }
    
    /**
     * Displays comprehensive game over statistics.
     * 
     * @param game Game instance
     */
    private void displayGameOverStatistics(SnakeGame game) {
        System.out.println(\"\\n\" + \"=\".repeat(50));
        
        if (isWin) {
            System.out.println(\"🎉 CONGRATULATIONS! YOU WON! 🎉\");
        } else {
            System.out.println(\"💀 GAME OVER 💀\");
            System.out.println(\"Reason: \" + getGameOverReasonText());
        }
        
        System.out.println(\"=\".repeat(50));
        System.out.println(\"📊 FINAL STATISTICS:\");
        System.out.println(\"Score: \" + game.getScore());
        System.out.println(\"Snake Length: \" + game.getSnake().getLength());
        System.out.println(\"Game Time: \" + formatGameTime(game.getGameTimeMillis()));
        System.out.println(\"Food Eaten: \" + game.getScore());
        
        // Calculate additional statistics
        long gameTimeSeconds = game.getGameTimeMillis() / 1000;
        if (gameTimeSeconds > 0) {
            double scorePerSecond = (double) game.getScore() / gameTimeSeconds;
            System.out.println(String.format(\"Score per Second: %.2f\", scorePerSecond));
        }
        
        double boardFillPercentage = calculateBoardFillPercentage(game);
        System.out.println(String.format(\"Board Fill: %.1f%%\", boardFillPercentage));
        
        System.out.println(\"=\".repeat(50));
        System.out.println(\"Press R to restart or Q to quit\");
    }
    
    /**
     * Gets a human-readable description of the game over reason.
     * 
     * @return Game over reason text
     */
    private String getGameOverReasonText() {
        switch (gameOverReason) {
            case SELF_COLLISION:
                return \"Snake collided with itself\";
            case WALL_COLLISION:
                return \"Snake hit the wall\";
            case NONE:
                return \"Unknown reason\";
            default:
                return \"Game ended\";
        }
    }
    
    /**
     * Formats game time in a readable format.
     * 
     * @param gameTimeMillis Game time in milliseconds
     * @return Formatted time string
     */
    private String formatGameTime(long gameTimeMillis) {
        long seconds = gameTimeMillis / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        
        if (minutes > 0) {
            return String.format(\"%d:%02d minutes\", minutes, seconds);
        } else {
            return String.format(\"%d seconds\", seconds);
        }
    }
    
    /**
     * Calculates what percentage of the board is filled by the snake.
     * 
     * @param game Game instance
     * @return Fill percentage (0-100)
     */
    private double calculateBoardFillPercentage(SnakeGame game) {
        int totalCells = game.getGameBoard().getWidth() * game.getGameBoard().getHeight();
        int snakeLength = game.getSnake().getLength();
        return (double) snakeLength / totalCells * 100;
    }
    
    /**
     * Checks if current score is a new high score.
     * 
     * @param game Game instance
     */
    private void checkHighScore(SnakeGame game) {
        // This would typically check against saved high scores
        // For now, just note if it's a good score
        int score = game.getScore();
        
        if (score >= 50) {
            System.out.println(\"🏆 EXCELLENT SCORE! 🏆\");
        } else if (score >= 25) {
            System.out.println(\"🥉 Good job!\");
        } else if (score >= 10) {
            System.out.println(\"👍 Not bad!\");
        }
    }
    
    /**
     * Saves game statistics for analytics or high score tracking.
     * 
     * @param game Game instance
     */
    private void saveGameStatistics(SnakeGame game) {
        // In a real implementation, this would save to a database or file
        // For now, just log the statistics
        System.out.println(String.format(
            \"Game statistics: Score=%d, Length=%d, Time=%dms, Reason=%s\",
            game.getScore(),
            game.getSnake().getLength(),
            game.getGameTimeMillis(),
            gameOverReason
        ));
    }
    
    @Override
    public String toString() {
        return getStateName() + (isWin ? \" (WIN)\" : \" (\" + gameOverReason + \")\");
    }
}