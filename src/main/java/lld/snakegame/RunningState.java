package lld.snakegame;

/**
 * State implementation for when the game is actively running.
 * 
 * In this state:
 * - Snake movement is allowed
 * - Game logic updates are processed
 * - All input commands are accepted
 * - Collision detection is active
 * 
 * This is the primary gameplay state where most game mechanics are active.
 */
public class RunningState implements GameState {
    
    private static final RunningState INSTANCE = new RunningState();
    
    /**
     * Gets the singleton instance of RunningState.
     * 
     * @return RunningState instance
     */
    public static RunningState getInstance() {
        return INSTANCE;
    }
    
    private RunningState() {
        // Private constructor for singleton
    }
    
    @Override
    public void update(SnakeGame game) {
        if (!canMove()) {
            return;
        }
        
        // Move the snake
        MoveResult moveResult = game.getSnake().move(game.getGameBoard());
        
        // Handle movement result
        switch (moveResult.getType()) {
            case NORMAL_MOVE:
                // Check for food collision
                if (game.getFood().getPosition().equals(game.getSnake().getHead())) {
                    handleFoodConsumption(game);
                }
                break;
                
            case SELF_COLLISION:
            case WALL_COLLISION:
                // Game over due to collision
                game.setState(GameOverState.getInstance());
                game.notifyGameOver(moveResult.getType());
                break;
                
            case INVALID_MOVE:
                // Ignore invalid moves (like trying to reverse)
                break;
        }
        
        // Notify observers of game update
        game.notifyGameUpdate();
    }
    
    @Override
    public void handleInput(SnakeGame game, Command command) {
        // In running state, all commands are processed
        if (command != null) {
            command.execute(game);
        }
    }
    
    @Override
    public boolean canMove() {
        return true;
    }
    
    @Override
    public boolean isGameOver() {
        return false;
    }
    
    @Override
    public boolean isPaused() {
        return false;
    }
    
    @Override
    public boolean isRunning() {
        return true;
    }
    
    @Override
    public String getStateName() {
        return "RUNNING";
    }
    
    @Override
    public void onEnter(SnakeGame game) {
        // Resume game timer if needed
        game.startGameTimer();
        game.notifyStateChanged(this);
    }
    
    @Override
    public void onExit(SnakeGame game) {
        // State-specific cleanup if needed
        // Timer management is handled by the new state
    }
    
    /**
     * Handles food consumption logic.
     * 
     * @param game Game instance
     */
    private void handleFoodConsumption(SnakeGame game) {
        // Grow the snake
        game.getSnake().grow();
        
        // Update score
        game.incrementScore();
        
        // Generate new food
        game.getFood().generateNewFood(game.getGameBoard(), game.getSnake());
        
        // Notify observers
        game.notifyFoodEaten();
        
        // Check for level completion or special conditions
        checkGameProgression(game);
    }
    
    /**
     * Checks for game progression conditions like level completion.
     * 
     * @param game Game instance
     */
    private void checkGameProgression(SnakeGame game) {
        int currentScore = game.getScore();
        
        // Example: Increase game speed every 10 points
        if (currentScore % 10 == 0 && currentScore > 0) {
            game.increaseSpeed();
            game.notifySpeedIncrease();
        }
        
        // Example: Check for win condition (if board is mostly filled)
        int totalCells = game.getGameBoard().getWidth() * game.getGameBoard().getHeight();
        int snakeLength = game.getSnake().getLength();
        double fillPercentage = (double) snakeLength / totalCells;
        
        if (fillPercentage >= 0.8) { // 80% of board filled
            game.setState(GameOverState.getInstance());
            game.notifyGameWon();
        }
    }
    
    @Override
    public String toString() {
        return getStateName();
    }
}