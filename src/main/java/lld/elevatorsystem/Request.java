package lld.elevatorsystem;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents an elevator request made by a passenger
 * Includes both external requests (hall calls) and internal requests (car calls)
 */
public class Request {
    private final int sourceFloor;
    private final int destinationFloor;
    private final Direction direction;
    private final LocalDateTime timestamp;
    private final RequestType type;
    
    public enum RequestType {
        EXTERNAL_UP,    // Hall call going up
        EXTERNAL_DOWN,  // Hall call going down
        INTERNAL        // Car call (destination request)
    }
    
    public Request(int sourceFloor, int destinationFloor, Direction direction, RequestType type) {
        this.sourceFloor = sourceFloor;
        this.destinationFloor = destinationFloor;
        this.direction = direction;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }
    
    // Factory methods for creating different types of requests
    public static Request createExternalRequest(int floor, Direction direction) {
        RequestType type = (direction == Direction.UP) ? RequestType.EXTERNAL_UP : RequestType.EXTERNAL_DOWN;
        return new Request(floor, -1, direction, type);
    }
    
    public static Request createInternalRequest(int sourceFloor, int destinationFloor) {
        Direction direction = sourceFloor < destinationFloor ? Direction.UP : Direction.DOWN;
        return new Request(sourceFloor, destinationFloor, direction, RequestType.INTERNAL);
    }
    
    // Getters
    public int getSourceFloor() { return sourceFloor; }
    public int getDestinationFloor() { return destinationFloor; }
    public Direction getDirection() { return direction; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public RequestType getType() { return type; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Request request = (Request) obj;
        return sourceFloor == request.sourceFloor &&
               destinationFloor == request.destinationFloor &&
               direction == request.direction &&
               type == request.type;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(sourceFloor, destinationFloor, direction, type);
    }
    
    @Override
    public String toString() {
        return String.format("Request{source=%d, dest=%d, dir=%s, type=%s, time=%s}", 
                           sourceFloor, destinationFloor, direction, type, timestamp);
    }
}