package lld.snakegame;

/**
 * State implementation for when the game is paused.
 * 
 * In this state:
 * - Snake movement is disabled
 * - Game logic updates are suspended
 * - Only resume/quit commands are accepted
 * - Game state is preserved for resuming
 * 
 * This state allows players to take breaks without losing progress.
 */
public class PausedState implements GameState {
    
    private static final PausedState INSTANCE = new PausedState();
    
    /**
     * Gets the singleton instance of PausedState.
     * 
     * @return PausedState instance
     */
    public static PausedState getInstance() {
        return INSTANCE;
    }
    
    private PausedState() {
        // Private constructor for singleton
    }
    
    @Override
    public void update(SnakeGame game) {
        // No game logic updates while paused
        // Game state is frozen
    }
    
    @Override
    public void handleInput(SnakeGame game, Command command) {
        // Only handle specific commands while paused
        if (command instanceof PauseCommand) {
            // Resume the game
            game.setState(RunningState.getInstance());
        } else if (command instanceof QuitCommand) {
            // Allow quitting from paused state
            command.execute(game);
        }
        // Ignore movement commands while paused
    }
    
    @Override
    public boolean canMove() {
        return false;
    }
    
    @Override
    public boolean isGameOver() {
        return false;
    }
    
    @Override
    public boolean isPaused() {
        return true;
    }
    
    @Override
    public boolean isRunning() {
        return false;
    }
    
    @Override
    public String getStateName() {
        return "PAUSED";
    }
    
    @Override
    public void onEnter(SnakeGame game) {
        // Pause the game timer
        game.pauseGameTimer();
        
        // Notify observers that game is paused
        game.notifyGamePaused();
        game.notifyStateChanged(this);
        
        // Log pause event
        logPauseEvent(game);
    }
    
    @Override
    public void onExit(SnakeGame game) {
        // Notify observers that game is resuming
        game.notifyGameResumed();
        
        // Log resume event
        logResumeEvent(game);
    }
    
    /**
     * Logs the pause event for analytics or debugging.
     * 
     * @param game Game instance
     */
    private void logPauseEvent(SnakeGame game) {
        System.out.println(String.format(
            "⏸️ Game paused at score: %d, snake length: %d, time: %d ms",
            game.getScore(),
            game.getSnake().getLength(),
            game.getGameTimeMillis()
        ));
    }
    
    /**
     * Logs the resume event for analytics or debugging.
     * 
     * @param game Game instance
     */
    private void logResumeEvent(SnakeGame game) {
        System.out.println(String.format(
            "▶️ Game resumed at score: %d, snake length: %d",
            game.getScore(),
            game.getSnake().getLength()
        ));
    }
    
    /**
     * Gets pause duration information.
     * Could be used for achievements or analytics.
     * 
     * @param pauseStartTime When pause began
     * @return Pause duration in milliseconds
     */
    public long getPauseDuration(long pauseStartTime) {
        return System.currentTimeMillis() - pauseStartTime;
    }
    
    @Override
    public String toString() {
        return getStateName();
    }
}