package lld.elevatorsystem;

import java.util.List;

/**
 * SCAN (Elevator) scheduling strategy implementation
 * Selects elevator based on distance and direction efficiency
 * Most commonly used algorithm in real elevator systems
 */
public class SCANSchedulingStrategy implements ElevatorSchedulingStrategy {
    
    @Override
    public Elevator selectElevator(List<Elevator> elevators, Request request) {
        if (elevators == null || elevators.isEmpty()) {
            return null;
        }
        
        Elevator bestElevator = null;
        int bestScore = Integer.MAX_VALUE;
        
        for (Elevator elevator : elevators) {
            // Skip elevators in maintenance or emergency mode
            if (elevator.getState() == ElevatorState.MAINTENANCE || 
                elevator.getState() == ElevatorState.EMERGENCY) {
                continue;
            }
            
            int score = calculateScore(elevator, request);
            if (score < bestScore) {
                bestScore = score;
                bestElevator = elevator;
            }
        }
        
        return bestElevator;
    }
    
    /**
     * Calculate efficiency score for elevator-request pairing
     * Lower score indicates better match
     */
    private int calculateScore(Elevator elevator, Request request) {
        int sourceFloor = request.getSourceFloor();
        int currentFloor = elevator.getCurrentFloor();
        Direction currentDirection = elevator.getCurrentDirection();
        Direction requestDirection = request.getDirection();
        
        // Base distance score
        int distance = Math.abs(currentFloor - sourceFloor);
        
        // Direction compatibility bonus/penalty
        int directionScore = calculateDirectionScore(
            currentFloor, sourceFloor, currentDirection, requestDirection
        );
        
        // Load penalty (prefer less loaded elevators)
        int loadPenalty = elevator.getLoadCount() * 2;
        
        // Idle elevator bonus
        int idleBonus = elevator.isIdle() ? -5 : 0;
        
        return distance + directionScore + loadPenalty + idleBonus;
    }
    
    /**
     * Calculate direction compatibility score
     */
    private int calculateDirectionScore(int currentFloor, int sourceFloor, 
                                      Direction currentDirection, Direction requestDirection) {
        
        // If elevator is idle, no direction penalty
        if (currentDirection == Direction.IDLE) {
            return 0;
        }
        
        boolean elevatorMovingTowardsRequest = 
            (currentDirection == Direction.UP && sourceFloor > currentFloor) ||
            (currentDirection == Direction.DOWN && sourceFloor < currentFloor);
        
        boolean sameDirection = currentDirection == requestDirection;
        
        if (elevatorMovingTowardsRequest && sameDirection) {
            // Perfect match: moving towards request in same direction
            return -10;
        } else if (elevatorMovingTowardsRequest) {
            // Good match: moving towards request
            return -5;
        } else if (sameDirection) {
            // Okay match: same direction but moving away
            return 5;
        } else {
            // Poor match: opposite direction
            return 15;
        }
    }
    
    @Override
    public String getStrategyName() {
        return "SCAN Scheduling Strategy";
    }
}