package systemdesign.parkinglot;

import java.util.ArrayList;
import java.util.List;

/**
 * The parking lot containing multiple floors.
 */
public class ParkingLot {
    private final List<ParkingFloor> floors = new ArrayList<>();

    public void addFloor(ParkingFloor floor) {
        floors.add(floor);
    }

    public ParkingSpot findSpot(VehicleType type) {
        for (ParkingFloor floor : floors) {
            ParkingSpot spot = floor.findSpot(type);
            if (spot != null) {
                return spot;
            }
        }
        return null;
    }
}
