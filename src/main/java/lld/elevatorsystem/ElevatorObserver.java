package lld.elevatorsystem;

/**
 * Observer interface for elevator events
 * Implements Observer pattern for real-time notifications
 */
public interface ElevatorObserver {
    /**
     * Called when an elevator event occurs
     * @param elevatorId ID of the elevator
     * @param message Event message
     */
    void onElevatorEvent(int elevatorId, String message);
}