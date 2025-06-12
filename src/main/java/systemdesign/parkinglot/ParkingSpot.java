package systemdesign.parkinglot;

/**
 * Represents an individual parking spot on a floor.
 */
public class ParkingSpot {
    private final String id;
    private final VehicleType type;
    private Vehicle parkedVehicle;

    public ParkingSpot(String id, VehicleType type) {
        this.id = id;
        this.type = type;
    }

    public boolean isFree() {
        return parkedVehicle == null;
    }

    public void assignVehicle(Vehicle vehicle) {
        this.parkedVehicle = vehicle;
    }

    public void removeVehicle() {
        this.parkedVehicle = null;
    }

    public VehicleType getType() {
        return type;
    }

    public String getId() {
        return id;
    }
}
