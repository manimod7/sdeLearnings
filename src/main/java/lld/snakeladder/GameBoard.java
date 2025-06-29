package lld.snakeladder;

import java.util.*;

/**
 * Represents the game board with snakes and ladders
 * Singleton pattern for board configuration
 */
public class GameBoard {
    private static final int BOARD_SIZE = 100;
    private final Map<Integer, BoardElement> boardElements;
    private final Set<Integer> occupiedPositions;
    
    public GameBoard() {
        this.boardElements = new HashMap<>();
        this.occupiedPositions = new HashSet<>();
    }
    
    /**
     * Add a snake to the board
     */
    public void addSnake(int head, int tail) {
        validateElementPlacement(head);
        Snake snake = new Snake(head, tail);
        boardElements.put(head, snake);
        occupiedPositions.add(head);
        occupiedPositions.add(tail);
    }
    
    /**
     * Add a ladder to the board
     */
    public void addLadder(int bottom, int top) {
        validateElementPlacement(bottom);
        Ladder ladder = new Ladder(bottom, top);
        boardElements.put(bottom, ladder);
        occupiedPositions.add(bottom);
        occupiedPositions.add(top);
    }
    
    /**
     * Get the final position after considering board elements
     */
    public int getFinalPosition(int position) {
        if (position > BOARD_SIZE) {
            return position; // Invalid position, no change
        }
        
        BoardElement element = boardElements.get(position);
        if (element != null) {
            return element.getEndPosition();
        }
        return position;
    }
    
    /**
     * Get board element at position (if any)
     */
    public BoardElement getElementAt(int position) {
        return boardElements.get(position);
    }
    
    /**
     * Check if position has a board element
     */
    public boolean hasElementAt(int position) {
        return boardElements.containsKey(position);
    }
    
    /**
     * Get all snakes on the board
     */
    public List<Snake> getSnakes() {
        return boardElements.values().stream()
                          .filter(element -> element instanceof Snake)
                          .map(element -> (Snake) element)
                          .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    /**
     * Get all ladders on the board
     */
    public List<Ladder> getLadders() {
        return boardElements.values().stream()
                           .filter(element -> element instanceof Ladder)
                           .map(element -> (Ladder) element)
                           .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    /**
     * Get board statistics
     */
    public BoardStats getStats() {
        long snakeCount = boardElements.values().stream()
                                     .filter(element -> element instanceof Snake)
                                     .count();
        long ladderCount = boardElements.values().stream()
                                      .filter(element -> element instanceof Ladder)
                                      .count();
        
        return new BoardStats((int) snakeCount, (int) ladderCount, occupiedPositions.size());
    }
    
    /**
     * Create a standard board configuration
     */
    public static GameBoard createStandardBoard() {
        GameBoard board = new GameBoard();
        
        // Add standard snakes
        board.addSnake(99, 78);
        board.addSnake(95, 75);
        board.addSnake(92, 88);
        board.addSnake(89, 68);
        board.addSnake(74, 53);
        board.addSnake(64, 60);
        board.addSnake(62, 19);
        board.addSnake(46, 25);
        board.addSnake(37, 3);
        board.addSnake(27, 1);
        
        // Add standard ladders
        board.addLadder(2, 38);
        board.addLadder(7, 14);
        board.addLadder(8, 31);
        board.addLadder(15, 26);
        board.addLadder(21, 42);
        board.addLadder(28, 84);
        board.addLadder(36, 44);
        board.addLadder(51, 67);
        board.addLadder(71, 91);
        board.addLadder(78, 98);
        
        return board;
    }
    
    /**
     * Create a custom board with specified elements
     */
    public static GameBoard createCustomBoard(List<Snake> snakes, List<Ladder> ladders) {
        GameBoard board = new GameBoard();
        
        for (Snake snake : snakes) {
            board.addSnake(snake.getStartPosition(), snake.getEndPosition());
        }
        
        for (Ladder ladder : ladders) {
            board.addLadder(ladder.getStartPosition(), ladder.getEndPosition());
        }
        
        return board;
    }
    
    private void validateElementPlacement(int position) {
        if (position < 1 || position > BOARD_SIZE) {
            throw new IllegalArgumentException("Position must be between 1 and " + BOARD_SIZE);
        }
        
        if (boardElements.containsKey(position)) {
            throw new IllegalArgumentException("Position " + position + " already has an element");
        }
        
        if (position == BOARD_SIZE) {
            throw new IllegalArgumentException("Cannot place element at winning position");
        }
    }
    
    /**
     * Display board in text format
     */
    public void displayBoard() {
        System.out.println("🎮 Snake and Ladder Board Configuration:");
        System.out.println("=========================================");
        
        System.out.println("🐍 Snakes:");
        getSnakes().forEach(snake -> 
            System.out.println("   " + snake.getStartPosition() + " → " + snake.getEndPosition()));
        
        System.out.println("\n🪜 Ladders:");
        getLadders().forEach(ladder -> 
            System.out.println("   " + ladder.getStartPosition() + " → " + ladder.getEndPosition()));
        
        BoardStats stats = getStats();
        System.out.println("\n📊 Board Stats:");
        System.out.println("   Snakes: " + stats.getSnakeCount());
        System.out.println("   Ladders: " + stats.getLadderCount());
        System.out.println("   Total Elements: " + (stats.getSnakeCount() + stats.getLadderCount()));
    }
    
    // Inner class for board statistics
    public static class BoardStats {
        private final int snakeCount;
        private final int ladderCount;
        private final int occupiedPositions;
        
        public BoardStats(int snakeCount, int ladderCount, int occupiedPositions) {
            this.snakeCount = snakeCount;
            this.ladderCount = ladderCount;
            this.occupiedPositions = occupiedPositions;
        }
        
        public int getSnakeCount() { return snakeCount; }
        public int getLadderCount() { return ladderCount; }
        public int getOccupiedPositions() { return occupiedPositions; }
    }
}