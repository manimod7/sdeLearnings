package lld.parkinglot;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a parking ticket issued when a vehicle enters the parking lot.
 */
public class ParkingTicket {
    private final String ticketId;
    private final Vehicle vehicle;
    private final ParkingSpot spot;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private BigDecimal fee;
    
    public ParkingTicket(Vehicle vehicle, ParkingSpot spot, LocalDateTime entryTime) {
        this.ticketId = UUID.randomUUID().toString();
        this.vehicle = vehicle;
        this.spot = spot;
        this.entryTime = entryTime;
        this.fee = BigDecimal.ZERO;
    }
    
    public long getParkingDurationMinutes() {
        LocalDateTime end = exitTime != null ? exitTime : LocalDateTime.now();
        return java.time.Duration.between(entryTime, end).toMinutes();
    }
    
    // Getters and setters
    public String getTicketId() { return ticketId; }
    public Vehicle getVehicle() { return vehicle; }
    public ParkingSpot getSpot() { return spot; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public LocalDateTime getExitTime() { return exitTime; }
    public BigDecimal getFee() { return fee; }
    
    public void setExitTime(LocalDateTime exitTime) { this.exitTime = exitTime; }
    public void setFee(BigDecimal fee) { this.fee = fee; }
}