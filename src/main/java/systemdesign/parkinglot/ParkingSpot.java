package systemdesign.parkinglot;

/**
 * Represents a parking spot in the lot.
 */
public class ParkingSpot {
    private final int number;
    private final VehicleType type;
    private boolean occupied;

    public ParkingSpot(int number, VehicleType type) {
        this.number = number;
        this.type = type;
    }

    public boolean isAvailable() {
        return !occupied;
    }

    public void occupy() {
        occupied = true;
    }

    public void vacate() {
        occupied = false;
    }

    public VehicleType getType() {
        return type;
    }
}
