package systemdesign.parkinglot;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a floor in the parking lot.
 */
public class ParkingFloor {
    private final int number;
    private final List<ParkingSpot> spots = new ArrayList<>();

    public ParkingFloor(int number) {
        this.number = number;
    }

    public void addSpot(ParkingSpot spot) {
        spots.add(spot);
    }

    public ParkingSpot findSpot(VehicleType type) {
        for (ParkingSpot spot : spots) {
            if (spot.getType() == type && spot.isFree()) {
                return spot;
            }
        }
        return null;
    }

    public int getNumber() {
        return number;
    }
}
