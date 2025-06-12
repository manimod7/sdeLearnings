package systemdesign.parkinglot;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic parking lot holding a list of spots.
 */
public class ParkingLot {
    private final List<ParkingSpot> spots = new ArrayList<>();

    public void addSpot(ParkingSpot spot) {
        spots.add(spot);
    }

    public ParkingSpot findSpot(VehicleType type) {
        for (ParkingSpot spot : spots) {
            if (spot.getType() == type && spot.isAvailable()) {
                return spot;
            }
        }
        return null;
    }
}
