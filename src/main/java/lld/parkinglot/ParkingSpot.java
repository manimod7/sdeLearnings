package lld.parkinglot;

import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Represents a single parking spot in the parking lot.
 * 
 * Encapsulates spot state, occupancy tracking, and vehicle management.
 * Thread-safe implementation for concurrent access.
 * 
 * Design Pattern: State Pattern (for spot status)
 * Benefits:
 * - Clear state management
 * - Thread-safe operations
 * - Comprehensive spot information
 * - Easy status tracking and validation
 */
public class ParkingSpot {
    private final String spotId;
    private final SpotType spotType;
    private final int levelNumber;
    private final String section;
    private final int spotNumber;
    
    // Mutable state
    private volatile boolean isOccupied;
    private volatile boolean isReserved;
    private volatile boolean isOutOfOrder;
    private Vehicle currentVehicle;
    private LocalDateTime occupiedSince;
    private LocalDateTime reservedUntil;
    private String outOfOrderReason;
    
    // Thread safety
    private final ReentrantReadWriteLock lock;
    
    // Spot characteristics
    private final double[] dimensions; // [width, length] in meters
    private boolean hasCover;
    private boolean hasChargingStation;
    private int securityLevel; // 1-5, higher is more secure
    
    /**
     * Creates a new parking spot.
     * 
     * @param spotId Unique identifier for the spot
     * @param spotType Type of parking spot
     * @param levelNumber Level where this spot is located
     * @param section Section identifier within the level
     * @param spotNumber Spot number within the section
     */
    public ParkingSpot(String spotId, SpotType spotType, int levelNumber, String section, int spotNumber) {
        if (spotId == null || spotId.trim().isEmpty()) {
            throw new IllegalArgumentException("Spot ID cannot be null or empty");
        }
        if (spotType == null) {
            throw new IllegalArgumentException("Spot type cannot be null");
        }
        
        this.spotId = spotId.trim();
        this.spotType = spotType;
        this.levelNumber = levelNumber;
        this.section = section != null ? section.trim() : "";
        this.spotNumber = spotNumber;
        
        // Initialize state
        this.isOccupied = false;
        this.isReserved = false;
        this.isOutOfOrder = false;
        this.currentVehicle = null;
        this.occupiedSince = null;
        this.reservedUntil = null;
        this.outOfOrderReason = null;
        
        // Initialize characteristics
        this.dimensions = spotType.getDimensionsMeters();
        this.hasCover = false; // Default
        this.hasChargingStation = spotType.hasChargingCapability();
        this.securityLevel = 3; // Default medium security
        
        this.lock = new ReentrantReadWriteLock();
    }
    
    /**
     * Attempts to park a vehicle in this spot.
     * 
     * @param vehicle Vehicle to park
     * @return true if parking was successful
     */
    public boolean parkVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }
        
        lock.writeLock().lock();
        try {
            // Check if spot is available
            if (!isAvailable()) {
                return false;
            }
            
            // Check if vehicle can fit
            if (!canAccommodateVehicle(vehicle)) {
                return false;
            }
            
            // Park the vehicle
            this.currentVehicle = vehicle;
            this.isOccupied = true;
            this.occupiedSince = LocalDateTime.now();
            
            // Clear reservation if any
            this.isReserved = false;
            this.reservedUntil = null;
            
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Removes the vehicle from this spot.
     * 
     * @return The vehicle that was removed, or null if spot was empty
     */
    public Vehicle removeVehicle() {
        lock.writeLock().lock();
        try {
            if (!isOccupied || currentVehicle == null) {
                return null;
            }
            
            Vehicle removedVehicle = currentVehicle;
            
            // Clear spot state
            this.currentVehicle = null;
            this.isOccupied = false;
            this.occupiedSince = null;
            
            return removedVehicle;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Reserves this spot for a specified duration.
     * 
     * @param durationMinutes How long to reserve the spot
     * @return true if reservation was successful
     */
    public boolean reserveSpot(int durationMinutes) {
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Duration must be positive");
        }
        
        lock.writeLock().lock();
        try {
            if (!isAvailable()) {
                return false;
            }
            
            this.isReserved = true;
            this.reservedUntil = LocalDateTime.now().plusMinutes(durationMinutes);
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Cancels the reservation for this spot.
     * 
     * @return true if reservation was cancelled
     */
    public boolean cancelReservation() {
        lock.writeLock().lock();
        try {
            if (!isReserved) {
                return false;
            }
            
            this.isReserved = false;
            this.reservedUntil = null;
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Marks this spot as out of order.
     * 
     * @param reason Reason for being out of order
     * @return true if successfully marked as out of order
     */
    public boolean markOutOfOrder(String reason) {
        lock.writeLock().lock();
        try {
            if (isOccupied) {
                return false; // Cannot mark occupied spot as out of order
            }
            
            this.isOutOfOrder = true;
            this.outOfOrderReason = reason != null ? reason : "Maintenance required";
            
            // Cancel any reservation
            this.isReserved = false;
            this.reservedUntil = null;
            
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Marks this spot as back in service.
     * 
     * @return true if successfully restored to service
     */
    public boolean restoreToService() {
        lock.writeLock().lock();
        try {
            if (!isOutOfOrder) {
                return false;
            }
            
            this.isOutOfOrder = false;
            this.outOfOrderReason = null;
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Checks if this spot is available for parking.
     * 
     * @return true if available
     */
    public boolean isAvailable() {
        lock.readLock().lock();
        try {
            if (isOccupied || isOutOfOrder) {
                return false;
            }
            
            // Check if reservation has expired
            if (isReserved && reservedUntil != null) {
                if (LocalDateTime.now().isAfter(reservedUntil)) {
                    // Reservation expired, make available
                    lock.readLock().unlock();
                    lock.writeLock().lock();
                    try {
                        this.isReserved = false;
                        this.reservedUntil = null;
                        return true;
                    } finally {
                        lock.readLock().lock();
                        lock.writeLock().unlock();
                    }
                }
                return false; // Still reserved
            }
            
            return true;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Checks if this spot can accommodate the given vehicle.
     * 
     * @param vehicle Vehicle to check
     * @return true if vehicle can be accommodated
     */
    public boolean canAccommodateVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            return false;
        }
        
        lock.readLock().lock();
        try {
            // Check spot type compatibility
            if (!spotType.canAccommodate(vehicle.getType())) {
                return false;
            }
            
            // Check if vehicle can fit in spot
            return vehicle.canFitInSpot(spotType);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Gets the duration for which this spot has been occupied.
     * 
     * @return Duration in minutes, or 0 if not occupied
     */
    public long getOccupiedDurationMinutes() {
        lock.readLock().lock();
        try {
            if (!isOccupied || occupiedSince == null) {
                return 0;
            }
            
            return java.time.Duration.between(occupiedSince, LocalDateTime.now()).toMinutes();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Gets remaining reservation time in minutes.
     * 
     * @return Remaining minutes, or 0 if not reserved
     */
    public long getRemainingReservationMinutes() {
        lock.readLock().lock();
        try {
            if (!isReserved || reservedUntil == null) {
                return 0;
            }
            
            LocalDateTime now = LocalDateTime.now();
            if (now.isAfter(reservedUntil)) {
                return 0;
            }
            
            return java.time.Duration.between(now, reservedUntil).toMinutes();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Gets spot status information.
     * 
     * @return Status string
     */
    public String getStatusInfo() {
        lock.readLock().lock();
        try {
            if (isOutOfOrder) {
                return "OUT OF ORDER: " + outOfOrderReason;
            } else if (isOccupied) {
                return "OCCUPIED by " + (currentVehicle != null ? currentVehicle.getLicensePlate() : "Unknown");
            } else if (isReserved) {
                return "RESERVED until " + reservedUntil;
            } else {
                return "AVAILABLE";
            }
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Gets detailed spot information.
     * 
     * @return Detailed information string
     */
    public String getDetailedInfo() {
        lock.readLock().lock();
        try {
            StringBuilder info = new StringBuilder();
            info.append(String.format("Spot %s [%s] - Level %d, Section %s, Spot %d%n",
                                    spotId, spotType.getDisplayName(), levelNumber, section, spotNumber));
            info.append(String.format("Dimensions: %.1fm x %.1fm%n", dimensions[0], dimensions[1]));
            info.append(String.format("Features: %s%s%s%n",
                                    hasCover ? "Covered " : "",
                                    hasChargingStation ? "Charging " : "",
                                    securityLevel > 3 ? "High Security" : ""));
            info.append("Status: ").append(getStatusInfo());
            
            if (isOccupied && currentVehicle != null) {
                info.append(String.format("%nVehicle: %s%nOccupied since: %s%nDuration: %d minutes",
                                        currentVehicle.getDisplayInfo(),
                                        occupiedSince,
                                        getOccupiedDurationMinutes()));
            }
            
            return info.toString();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    // Getters
    public String getSpotId() { return spotId; }
    public SpotType getSpotType() { return spotType; }
    public int getLevelNumber() { return levelNumber; }
    public String getSection() { return section; }
    public int getSpotNumber() { return spotNumber; }
    
    public boolean isOccupied() { 
        lock.readLock().lock();
        try {
            return isOccupied;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public boolean isReserved() { 
        lock.readLock().lock();
        try {
            return isReserved && (reservedUntil == null || LocalDateTime.now().isBefore(reservedUntil));
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public boolean isOutOfOrder() { 
        lock.readLock().lock();
        try {
            return isOutOfOrder;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public Vehicle getCurrentVehicle() { 
        lock.readLock().lock();
        try {
            return currentVehicle;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public LocalDateTime getOccupiedSince() { 
        lock.readLock().lock();
        try {
            return occupiedSince;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public LocalDateTime getReservedUntil() { 
        lock.readLock().lock();
        try {
            return reservedUntil;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public String getOutOfOrderReason() { 
        lock.readLock().lock();
        try {
            return outOfOrderReason;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public double[] getDimensions() { return dimensions.clone(); }
    public boolean hasCover() { return hasCover; }
    public boolean hasChargingStation() { return hasChargingStation; }
    public int getSecurityLevel() { return securityLevel; }
    
    // Setters for characteristics
    public void setHasCover(boolean hasCover) { this.hasCover = hasCover; }
    public void setHasChargingStation(boolean hasChargingStation) { this.hasChargingStation = hasChargingStation; }
    public void setSecurityLevel(int securityLevel) { 
        this.securityLevel = Math.max(1, Math.min(5, securityLevel)); 
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        ParkingSpot that = (ParkingSpot) obj;
        return spotId.equals(that.spotId);
    }
    
    @Override
    public int hashCode() {
        return spotId.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("ParkingSpot{id='%s', type=%s, level=%d, available=%s}", 
                           spotId, spotType, levelNumber, isAvailable());
    }
}