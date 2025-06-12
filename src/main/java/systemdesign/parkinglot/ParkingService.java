package systemdesign.parkinglot;

/**
 * Service to allocate and release parking spots.
 */
public class ParkingService {
    private final ParkingLot lot;

    public ParkingService(ParkingLot lot) {
        this.lot = lot;
    }

    public ParkingSpot park(VehicleType type) {
        ParkingSpot spot = lot.findSpot(type);
        if (spot != null) {
            spot.occupy();
        }
        return spot;
    }

    public void unpark(ParkingSpot spot) {
        spot.vacate();
    }
}
