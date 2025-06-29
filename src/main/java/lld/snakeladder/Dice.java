package lld.snakeladder;

import java.util.Random;

/**
 * Represents a dice in the game
 * Strategy pattern implementation for different dice types
 */
public interface Dice {
    /**
     * Roll the dice and return the result
     * @return Dice roll result
     */
    int roll();
    
    /**
     * Get minimum possible value
     * @return Minimum value
     */
    int getMinValue();
    
    /**
     * Get maximum possible value
     * @return Maximum value
     */
    int getMaxValue();
}

/**
 * Standard 6-sided dice implementation
 */
class StandardDice implements Dice {
    private final Random random;
    
    public StandardDice() {
        this.random = new Random();
    }
    
    public StandardDice(long seed) {
        this.random = new Random(seed);
    }
    
    @Override
    public int roll() {
        return random.nextInt(6) + 1; // 1 to 6
    }
    
    @Override
    public int getMinValue() {
        return 1;
    }
    
    @Override
    public int getMaxValue() {
        return 6;
    }
}

/**
 * Double dice implementation (roll two dice)
 */
class DoubleDice implements Dice {
    private final Dice dice1;
    private final Dice dice2;
    
    public DoubleDice() {
        this.dice1 = new StandardDice();
        this.dice2 = new StandardDice();
    }
    
    @Override
    public int roll() {
        return dice1.roll() + dice2.roll();
    }
    
    @Override
    public int getMinValue() {
        return dice1.getMinValue() + dice2.getMinValue();
    }
    
    @Override
    public int getMaxValue() {
        return dice1.getMaxValue() + dice2.getMaxValue();
    }
}

/**
 * Factory for creating different types of dice
 */
class DiceFactory {
    public static Dice createStandardDice() {
        return new StandardDice();
    }
    
    public static Dice createStandardDice(long seed) {
        return new StandardDice(seed);
    }
    
    public static Dice createDoubleDice() {
        return new DoubleDice();
    }
}