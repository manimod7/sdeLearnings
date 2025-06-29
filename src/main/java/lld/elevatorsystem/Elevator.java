package lld.elevatorsystem;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Core Elevator class implementing the elevator car functionality
 * Uses State pattern for elevator state management
 * Thread-safe implementation for concurrent access
 */
public class Elevator {
    private final int elevatorId;
    private final int capacity;
    private int currentFloor;
    private Direction currentDirection;
    private ElevatorState state;
    
    // Thread-safe collections for handling concurrent requests
    private final Set<Integer> destinationFloors;
    private final Set<Request> pendingRequests;
    private final Map<Integer, Set<Request>> floorRequests;
    
    // Observer pattern for notifications
    private final Set<ElevatorObserver> observers;
    
    public Elevator(int elevatorId, int capacity, int initialFloor) {
        this.elevatorId = elevatorId;
        this.capacity = capacity;
        this.currentFloor = initialFloor;
        this.currentDirection = Direction.IDLE;
        this.state = ElevatorState.IDLE;
        
        this.destinationFloors = new CopyOnWriteArraySet<>();
        this.pendingRequests = new CopyOnWriteArraySet<>();
        this.floorRequests = new ConcurrentHashMap<>();
        this.observers = new CopyOnWriteArraySet<>();
    }
    
    /**
     * Add request to elevator's pending requests
     * @param request The request to be added
     * @return true if request was successfully added
     */
    public synchronized boolean addRequest(Request request) {
        if (state == ElevatorState.MAINTENANCE || state == ElevatorState.EMERGENCY) {
            return false;
        }
        
        pendingRequests.add(request);
        destinationFloors.add(request.getDestinationFloor());
        
        // Group requests by floor for efficient processing
        floorRequests.computeIfAbsent(request.getSourceFloor(), k -> new CopyOnWriteArraySet<>())
                    .add(request);
        
        notifyObservers("Request added: " + request);
        return true;
    }
    
    /**
     * Move elevator one floor in the current direction
     * Implements SCAN (elevator) algorithm for efficient request handling
     */
    public synchronized void move() {
        if (state == ElevatorState.MAINTENANCE || state == ElevatorState.EMERGENCY) {
            return;
        }
        
        if (destinationFloors.isEmpty()) {
            state = ElevatorState.IDLE;
            currentDirection = Direction.IDLE;
            return;
        }
        
        // Determine next floor based on SCAN algorithm
        int nextFloor = getNextFloor();
        if (nextFloor == currentFloor) {
            // Arrived at destination floor
            handleFloorArrival();
            return;
        }
        
        // Update direction and state
        if (nextFloor > currentFloor) {
            currentDirection = Direction.UP;
            state = ElevatorState.MOVING_UP;
            currentFloor++;
        } else {
            currentDirection = Direction.DOWN;
            state = ElevatorState.MOVING_DOWN;
            currentFloor--;
        }
        
        notifyObservers(String.format("Elevator %d moved to floor %d", elevatorId, currentFloor));
        
        // Check if we need to stop at current floor
        if (shouldStopAtFloor(currentFloor)) {
            handleFloorArrival();
        }
    }
    
    /**
     * Handle elevator arrival at a floor
     * Open doors, process requests, close doors
     */
    private void handleFloorArrival() {
        state = ElevatorState.DOORS_OPENING;
        notifyObservers(String.format("Elevator %d opening doors at floor %d", elevatorId, currentFloor));
        
        // Simulate door opening time
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        state = ElevatorState.DOORS_OPEN;
        
        // Process requests for current floor
        processFloorRequests(currentFloor);
        
        // Simulate door open time
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        state = ElevatorState.DOORS_CLOSING;
        notifyObservers(String.format("Elevator %d closing doors at floor %d", elevatorId, currentFloor));
        
        // Simulate door closing time
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Remove current floor from destinations
        destinationFloors.remove(currentFloor);
        
        // Determine next state
        if (destinationFloors.isEmpty()) {
            state = ElevatorState.IDLE;
            currentDirection = Direction.IDLE;
        } else {
            // Continue in same direction or determine new direction
            updateDirection();
        }
    }
    
    /**
     * Process all requests for the given floor
     */
    private void processFloorRequests(int floor) {
        Set<Request> floorReqs = floorRequests.get(floor);
        if (floorReqs != null) {
            Iterator<Request> iterator = floorReqs.iterator();
            while (iterator.hasNext()) {
                Request request = iterator.next();
                if (request.getSourceFloor() == floor || request.getDestinationFloor() == floor) {
                    pendingRequests.remove(request);
                    iterator.remove();
                    notifyObservers("Processed request: " + request);
                }
            }
            
            if (floorReqs.isEmpty()) {
                floorRequests.remove(floor);
            }
        }
    }
    
    /**
     * Determine if elevator should stop at given floor
     */
    private boolean shouldStopAtFloor(int floor) {
        // Stop if there are requests for this floor in the current direction
        Set<Request> floorReqs = floorRequests.get(floor);
        if (floorReqs == null) return false;
        
        return floorReqs.stream().anyMatch(req -> 
            req.getSourceFloor() == floor || 
            req.getDestinationFloor() == floor ||
            req.getDirection() == currentDirection
        );
    }
    
    /**
     * Get next floor using SCAN algorithm
     */
    private int getNextFloor() {
        if (destinationFloors.isEmpty()) {
            return currentFloor;
        }
        
        // Get floors in current direction
        List<Integer> floors = new ArrayList<>(destinationFloors);
        floors.sort(Integer::compareTo);
        
        if (currentDirection == Direction.UP || currentDirection == Direction.IDLE) {
            // Find next floor above current
            for (int floor : floors) {
                if (floor >= currentFloor) {
                    return floor;
                }
            }
            // If no floors above, go to highest floor below
            for (int i = floors.size() - 1; i >= 0; i--) {
                if (floors.get(i) <= currentFloor) {
                    return floors.get(i);
                }
            }
        } else {
            // Find next floor below current
            for (int i = floors.size() - 1; i >= 0; i--) {
                if (floors.get(i) <= currentFloor) {
                    return floors.get(i);
                }
            }
            // If no floors below, go to lowest floor above
            for (int floor : floors) {
                if (floor >= currentFloor) {
                    return floor;
                }
            }
        }
        
        return currentFloor;
    }
    
    /**
     * Update elevator direction based on pending requests
     */
    private void updateDirection() {
        if (destinationFloors.isEmpty()) {
            currentDirection = Direction.IDLE;
            return;
        }
        
        boolean hasUpRequests = destinationFloors.stream().anyMatch(floor -> floor > currentFloor);
        boolean hasDownRequests = destinationFloors.stream().anyMatch(floor -> floor < currentFloor);
        
        if (currentDirection == Direction.UP && hasUpRequests) {
            // Continue going up
            return;
        } else if (currentDirection == Direction.DOWN && hasDownRequests) {
            // Continue going down
            return;
        } else if (hasUpRequests) {
            currentDirection = Direction.UP;
        } else if (hasDownRequests) {
            currentDirection = Direction.DOWN;
        } else {
            currentDirection = Direction.IDLE;
        }
    }
    
    /**
     * Calculate efficiency score for request assignment
     * Lower score means more efficient
     */
    public int calculateEfficiencyScore(Request request) {
        if (state == ElevatorState.MAINTENANCE || state == ElevatorState.EMERGENCY) {
            return Integer.MAX_VALUE;
        }
        
        int distance = Math.abs(currentFloor - request.getSourceFloor());
        int directionPenalty = 0;
        
        // Penalty for going in opposite direction
        if (currentDirection != Direction.IDLE && 
            currentDirection != request.getDirection()) {
            directionPenalty = 10;
        }
        
        // Penalty for load
        int loadPenalty = destinationFloors.size();
        
        return distance + directionPenalty + loadPenalty;
    }
    
    // Observer pattern implementation
    public void addObserver(ElevatorObserver observer) {
        observers.add(observer);
    }
    
    public void removeObserver(ElevatorObserver observer) {
        observers.remove(observer);
    }
    
    private void notifyObservers(String message) {
        for (ElevatorObserver observer : observers) {
            observer.onElevatorEvent(elevatorId, message);
        }
    }
    
    // Getters
    public int getElevatorId() { return elevatorId; }
    public int getCurrentFloor() { return currentFloor; }
    public Direction getCurrentDirection() { return currentDirection; }
    public ElevatorState getState() { return state; }
    public int getCapacity() { return capacity; }
    public int getLoadCount() { return destinationFloors.size(); }
    public boolean isIdle() { return state == ElevatorState.IDLE; }
    
    // Emergency and maintenance methods
    public void setMaintenanceMode() {
        state = ElevatorState.MAINTENANCE;
        notifyObservers("Elevator " + elevatorId + " entering maintenance mode");
    }
    
    public void setEmergencyMode() {
        state = ElevatorState.EMERGENCY;
        notifyObservers("Elevator " + elevatorId + " entering emergency mode");
    }
    
    public void resumeNormalOperation() {
        if (state == ElevatorState.MAINTENANCE || state == ElevatorState.EMERGENCY) {
            state = ElevatorState.IDLE;
            currentDirection = Direction.IDLE;
            notifyObservers("Elevator " + elevatorId + " resuming normal operation");
        }
    }
    
    @Override
    public String toString() {
        return String.format("Elevator{id=%d, floor=%d, direction=%s, state=%s, load=%d/%d}", 
                           elevatorId, currentFloor, currentDirection, state, 
                           destinationFloors.size(), capacity);
    }
}