package lld.parkinglot;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Observer interface for parking lot events.
 */
public interface ParkingEventListener {
    void onVehicleParked(Vehicle vehicle, ParkingSpot spot, ParkingTicket ticket);
    void onVehicleExited(Vehicle vehicle, ParkingSpot spot, ParkingTicket ticket, BigDecimal fee);
    void onParkingFailed(Vehicle vehicle, String reason);
    void onCapacityChanged(Map<SpotType, CapacityInfo> capacityInfo);
    void onSpotReserved(ParkingSpot spot, VehicleType vehicleType, int durationMinutes);
}

/**
 * Data classes for supporting information.
 */
class CapacityInfo {
    private final int total;
    private final int available;
    private final int occupied;
    private final double occupancyRate;
    
    public CapacityInfo(int total, int available, int occupied, double occupancyRate) {
        this.total = total;
        this.available = available;
        this.occupied = occupied;
        this.occupancyRate = occupancyRate;
    }
    
    // Getters
    public int getTotal() { return total; }
    public int getAvailable() { return available; }
    public int getOccupied() { return occupied; }
    public double getOccupancyRate() { return occupancyRate; }
}

class PaymentInfo {
    private final ParkingTicket ticket;
    private final BigDecimal fee;
    
    public PaymentInfo(ParkingTicket ticket, BigDecimal fee) {
        this.ticket = ticket;
        this.fee = fee;
    }
    
    // Getters
    public ParkingTicket getTicket() { return ticket; }
    public BigDecimal getFee() { return fee; }
}

class SpotReservation {
    private final ParkingSpot spot;
    private final VehicleType vehicleType;
    private final int durationMinutes;
    
    public SpotReservation(ParkingSpot spot, VehicleType vehicleType, int durationMinutes) {
        this.spot = spot;
        this.vehicleType = vehicleType;
        this.durationMinutes = durationMinutes;
    }
    
    // Getters
    public ParkingSpot getSpot() { return spot; }
    public VehicleType getVehicleType() { return vehicleType; }
    public int getDurationMinutes() { return durationMinutes; }
}

class ParkingLotStats {
    private final int totalSpots;
    private final int availableSpots;
    private final int occupiedSpots;
    private final double occupancyRate;
    private final int activeTickets;
    private final int levels;
    private final Map<SpotType, CapacityInfo> capacityByType;
    
    public ParkingLotStats(int totalSpots, int availableSpots, int occupiedSpots, 
                          double occupancyRate, int activeTickets, int levels,
                          Map<SpotType, CapacityInfo> capacityByType) {
        this.totalSpots = totalSpots;
        this.availableSpots = availableSpots;
        this.occupiedSpots = occupiedSpots;
        this.occupancyRate = occupancyRate;
        this.activeTickets = activeTickets;
        this.levels = levels;
        this.capacityByType = capacityByType;
    }
    
    // Getters
    public int getTotalSpots() { return totalSpots; }
    public int getAvailableSpots() { return availableSpots; }
    public int getOccupiedSpots() { return occupiedSpots; }
    public double getOccupancyRate() { return occupancyRate; }
    public int getActiveTickets() { return activeTickets; }
    public int getLevels() { return levels; }
    public Map<SpotType, CapacityInfo> getCapacityByType() { return capacityByType; }
}