package systemdesign.parkinglot;

/**
 * Represents a vehicle entering the parking lot.
 */
public class Vehicle {
    private final VehicleType type;

    public Vehicle(VehicleType type) {
        this.type = type;
    }

    public VehicleType getType() {
        return type;
    }
}
