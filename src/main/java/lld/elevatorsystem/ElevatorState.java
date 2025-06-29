package lld.elevatorsystem;

/**
 * Enum representing different states of an elevator
 * Used in the State pattern for elevator behavior management
 */
public enum ElevatorState {
    IDLE,           // Elevator is stationary and not serving requests
    MOVING_UP,      // Elevator is moving upward
    MOVING_DOWN,    // Elevator is moving downward
    DOORS_OPENING,  // Elevator doors are opening
    DOORS_CLOSING,  // Elevator doors are closing
    DOORS_OPEN,     // Elevator doors are open for passengers
    MAINTENANCE,    // Elevator is under maintenance
    EMERGENCY       // Elevator is in emergency mode
}