package lld.snakegame;

/**
 * State interface for managing different game states.
 * 
 * Implements the State Pattern to handle different game behaviors
 * based on the current state (Running, Paused, Game Over).
 * 
 * Design Pattern: State Pattern
 * Benefits:
 * - Clean state management
 * - State-specific behavior encapsulation
 * - Easy state transitions
 * - Eliminates complex conditional logic
 */
public interface GameState {
    
    /**
     * Handles game logic updates for this state.
     * Different states may have different update behaviors.
     * 
     * @param game Game instance to update
     */
    void update(SnakeGame game);
    
    /**
     * Processes player input for this state.
     * Some states may ignore certain inputs.
     * 
     * @param game Game instance
     * @param command Command to process
     */
    void handleInput(SnakeGame game, Command command);
    
    /**
     * Checks if the snake can move in this state.
     * 
     * @return true if movement is allowed
     */
    boolean canMove();
    
    /**
     * Checks if the game is over in this state.
     * 
     * @return true if game is over
     */
    boolean isGameOver();
    
    /**
     * Checks if the game is paused in this state.
     * 
     * @return true if game is paused
     */
    boolean isPaused();
    
    /**
     * Checks if the game is running in this state.
     * 
     * @return true if game is running
     */
    boolean isRunning();
    
    /**
     * Gets the name of this state for display purposes.
     * 
     * @return State name
     */
    String getStateName();
    
    /**
     * Called when entering this state.
     * Allows state-specific initialization.
     * 
     * @param game Game instance
     */
    void onEnter(SnakeGame game);
    
    /**
     * Called when exiting this state.
     * Allows state-specific cleanup.
     * 
     * @param game Game instance
     */
    void onExit(SnakeGame game);
}