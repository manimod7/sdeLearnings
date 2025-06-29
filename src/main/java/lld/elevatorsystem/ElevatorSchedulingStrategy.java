package lld.elevatorsystem;

import java.util.List;

/**
 * Strategy interface for elevator scheduling algorithms
 * Implements Strategy pattern for different scheduling approaches
 */
public interface ElevatorSchedulingStrategy {
    /**
     * Select the best elevator for a given request
     * @param elevators List of available elevators
     * @param request The request to be scheduled
     * @return The selected elevator, or null if no elevator is available
     */
    Elevator selectElevator(List<Elevator> elevators, Request request);
    
    /**
     * Get strategy name for logging and debugging
     * @return Strategy name
     */
    String getStrategyName();
}