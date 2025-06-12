package systemdesign.parkinglot;

/**
 * Handles vehicle entries into the parking lot.
 */
public class EntryGate {
    private final ParkingLot parkingLot;

    public EntryGate(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    public ParkingTicket parkVehicle(Vehicle vehicle) {
        ParkingSpot spot = parkingLot.findSpot(vehicle.getType());
        if (spot == null) {
            return null;
        }
        spot.assignVehicle(vehicle);
        return new ParkingTicket(spot);
    }
}
