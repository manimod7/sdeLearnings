package lld.parkinglot;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * Main parking lot management system.
 * 
 * Coordinates all parking operations including spot allocation,
 * vehicle management, pricing, and capacity tracking.
 * 
 * Design Patterns Used:
 * - Singleton: Single parking lot instance per facility
 * - Observer: Event notifications for parking operations
 * - Strategy: Flexible pricing strategies
 * - Factory: Vehicle and spot creation
 * 
 * Thread-safe implementation for concurrent operations.
 */
public class ParkingLot {
    private final String lotId;
    private final String name;
    private final String address;
    private final List<Level> levels;
    private final Map<String, ParkingSpot> allSpots; // spotId -> spot
    private final Map<String, ParkingTicket> activeTickets; // ticketId -> ticket
    private final Map<String, Vehicle> parkedVehicles; // licensePlate -> vehicle
    
    // Capacity tracking
    private final Map<SpotType, Integer> totalCapacity;
    private final Map<SpotType, Integer> availableCapacity;
    
    // System components
    private PricingStrategy pricingStrategy;
    private final List<ParkingEventListener> eventListeners;
    
    // Configuration
    private final int maxReservationHours;
    private final boolean allowOverbooking;
    private final ReentrantReadWriteLock lock;
    
    /**
     * Creates a new parking lot.
     * 
     * @param lotId Unique identifier for the parking lot
     * @param name Display name of the parking lot
     * @param address Physical address
     */
    public ParkingLot(String lotId, String name, String address) {
        if (lotId == null || lotId.trim().isEmpty()) {
            throw new IllegalArgumentException("Lot ID cannot be null or empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Lot name cannot be null or empty");
        }
        
        this.lotId = lotId.trim();
        this.name = name.trim();
        this.address = address != null ? address.trim() : "";
        this.levels = new ArrayList<>();
        this.allSpots = new ConcurrentHashMap<>();
        this.activeTickets = new ConcurrentHashMap<>();
        this.parkedVehicles = new ConcurrentHashMap<>();
        this.totalCapacity = new ConcurrentHashMap<>();
        this.availableCapacity = new ConcurrentHashMap<>();
        this.eventListeners = new ArrayList<>();
        
        // Default configuration
        this.maxReservationHours = 24;
        this.allowOverbooking = false;
        this.lock = new ReentrantReadWriteLock();
        
        // Default pricing strategy
        this.pricingStrategy = new HourlyPricingStrategy(new BigDecimal("5.00"));
        
        // Initialize capacity maps
        for (SpotType spotType : SpotType.values()) {
            totalCapacity.put(spotType, 0);
            availableCapacity.put(spotType, 0);
        }
    }
    
    /**
     * Adds a level to the parking lot.
     * 
     * @param level Level to add
     */
    public void addLevel(Level level) {
        if (level == null) {
            throw new IllegalArgumentException("Level cannot be null");
        }
        
        lock.writeLock().lock();
        try {
            levels.add(level);
            
            // Add all spots from this level
            for (ParkingSpot spot : level.getSpots()) {
                allSpots.put(spot.getSpotId(), spot);
                
                // Update capacity
                SpotType spotType = spot.getSpotType();
                totalCapacity.merge(spotType, 1, Integer::sum);
                if (spot.isAvailable()) {
                    availableCapacity.merge(spotType, 1, Integer::sum);
                }
            }
            
            notifyCapacityChanged();
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Attempts to park a vehicle in the parking lot.
     * 
     * @param vehicle Vehicle to park
     * @return ParkingTicket if successful, null if no space available
     */
    public ParkingTicket parkVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }
        
        // Check if vehicle is already parked
        if (parkedVehicles.containsKey(vehicle.getLicensePlate())) {
            throw new IllegalStateException("Vehicle " + vehicle.getLicensePlate() + " is already parked");
        }
        
        // Find available spot
        ParkingSpot spot = findAvailableSpot(vehicle.getType());
        if (spot == null) {
            notifyParkingFailed(vehicle, "No available spots");
            return null;
        }
        
        // Attempt to park
        if (!spot.parkVehicle(vehicle)) {
            notifyParkingFailed(vehicle, "Failed to allocate spot");
            return null;
        }
        
        lock.writeLock().lock();
        try {
            // Create parking ticket
            ParkingTicket ticket = new ParkingTicket(vehicle, spot, LocalDateTime.now());
            
            // Update tracking
            activeTickets.put(ticket.getTicketId(), ticket);
            parkedVehicles.put(vehicle.getLicensePlate(), vehicle);
            
            // Update capacity
            availableCapacity.merge(spot.getSpotType(), -1, Integer::sum);
            
            notifyVehicleParked(vehicle, spot, ticket);
            notifyCapacityChanged();
            
            return ticket;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Processes vehicle exit and calculates parking fee.
     * 
     * @param ticketId Parking ticket ID
     * @return Payment information if successful, null if ticket not found
     */
    public PaymentInfo processExit(String ticketId) {
        return processExit(ticketId, LocalDateTime.now());
    }
    
    /**
     * Processes vehicle exit with specified exit time.
     * 
     * @param ticketId Parking ticket ID
     * @param exitTime Time of exit
     * @return Payment information if successful, null if ticket not found
     */
    public PaymentInfo processExit(String ticketId, LocalDateTime exitTime) {
        if (ticketId == null || ticketId.trim().isEmpty()) {
            throw new IllegalArgumentException("Ticket ID cannot be null or empty");
        }
        
        lock.writeLock().lock();
        try {
            ParkingTicket ticket = activeTickets.get(ticketId);
            if (ticket == null) {
                return null; // Ticket not found
            }
            
            // Calculate fee
            BigDecimal fee = pricingStrategy.calculateFee(ticket, exitTime);
            ticket.setExitTime(exitTime);
            ticket.setFee(fee);
            
            // Remove vehicle from spot
            ParkingSpot spot = ticket.getSpot();
            Vehicle vehicle = spot.removeVehicle();
            
            if (vehicle != null) {
                // Update tracking
                activeTickets.remove(ticketId);
                parkedVehicles.remove(vehicle.getLicensePlate());
                
                // Update capacity
                availableCapacity.merge(spot.getSpotType(), 1, Integer::sum);
                
                // Create payment info
                PaymentInfo paymentInfo = new PaymentInfo(ticket, fee);
                
                notifyVehicleExited(vehicle, spot, ticket, fee);
                notifyCapacityChanged();
                
                return paymentInfo;
            }
            
            return null;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Finds an available parking spot for the specified vehicle type.
     * 
     * @param vehicleType Type of vehicle
     * @return Available spot or null if none found
     */
    public ParkingSpot findAvailableSpot(VehicleType vehicleType) {
        lock.readLock().lock();
        try {
            // Get compatible spot types in order of preference
            SpotType[] compatibleTypes = vehicleType.getCompatibleSpotTypes();
            
            for (SpotType spotType : compatibleTypes) {
                // Find available spot of this type
                for (Level level : levels) {
                    ParkingSpot spot = level.findAvailableSpotOfType(spotType);
                    if (spot != null && spot.canAccommodateVehicle(createVehicleInstance(vehicleType))) {
                        return spot;
                    }
                }
            }
            
            return null; // No available spots
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Creates a temporary vehicle instance for compatibility checking.
     * 
     * @param vehicleType Type of vehicle
     * @return Temporary vehicle instance
     */
    private Vehicle createVehicleInstance(VehicleType vehicleType) {
        // Factory method to create vehicle instances
        switch (vehicleType) {
            case MOTORCYCLE:
                return new Motorcycle("TEMP", "TEMP", "");
            case CAR:
                return new Car("TEMP", "TEMP", "");
            case TRUCK:
                return new Truck("TEMP", "TEMP", "");
            case ELECTRIC_CAR:
                return new ElectricCar("TEMP", "TEMP", "");
            default:
                return new Car("TEMP", "TEMP", ""); // Default to car
        }
    }
    
    /**
     * Reserves a parking spot for the specified duration.
     * 
     * @param vehicleType Type of vehicle
     * @param durationMinutes Reservation duration in minutes
     * @return Reservation details if successful, null if no spots available
     */
    public SpotReservation reserveSpot(VehicleType vehicleType, int durationMinutes) {
        if (durationMinutes <= 0 || durationMinutes > maxReservationHours * 60) {
            throw new IllegalArgumentException("Invalid reservation duration");
        }
        
        ParkingSpot spot = findAvailableSpot(vehicleType);
        if (spot == null) {
            return null; // No available spots
        }
        
        if (spot.reserveSpot(durationMinutes)) {
            SpotReservation reservation = new SpotReservation(spot, vehicleType, durationMinutes);
            notifySpotReserved(spot, vehicleType, durationMinutes);
            return reservation;
        }
        
        return null;
    }
    
    /**
     * Gets current capacity information.
     * 
     * @return Capacity information map
     */
    public Map<SpotType, CapacityInfo> getCapacityInfo() {
        lock.readLock().lock();
        try {
            Map<SpotType, CapacityInfo> capacityInfo = new HashMap<>();
            
            for (SpotType spotType : SpotType.values()) {
                int total = totalCapacity.getOrDefault(spotType, 0);
                int available = availableCapacity.getOrDefault(spotType, 0);
                int occupied = total - available;
                double occupancyRate = total > 0 ? (double) occupied / total : 0.0;
                
                capacityInfo.put(spotType, new CapacityInfo(total, available, occupied, occupancyRate));
            }
            
            return capacityInfo;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Gets overall parking lot statistics.
     * 
     * @return Parking lot statistics
     */
    public ParkingLotStats getStatistics() {
        lock.readLock().lock();
        try {
            int totalSpots = totalCapacity.values().stream().mapToInt(Integer::intValue).sum();
            int totalAvailable = availableCapacity.values().stream().mapToInt(Integer::intValue).sum();
            int totalOccupied = totalSpots - totalAvailable;
            double overallOccupancyRate = totalSpots > 0 ? (double) totalOccupied / totalSpots : 0.0;
            
            return new ParkingLotStats(
                totalSpots,
                totalAvailable,
                totalOccupied,
                overallOccupancyRate,
                activeTickets.size(),
                levels.size(),
                getCapacityInfo()
            );
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Gets all active parking tickets.
     * 
     * @return Collection of active tickets
     */
    public Collection<ParkingTicket> getActiveTickets() {
        return new ArrayList<>(activeTickets.values());
    }
    
    /**
     * Gets all currently parked vehicles.
     * 
     * @return Collection of parked vehicles
     */
    public Collection<Vehicle> getParkedVehicles() {
        return new ArrayList<>(parkedVehicles.values());
    }
    
    /**
     * Finds a vehicle by license plate.
     * 
     * @param licensePlate License plate to search for
     * @return Vehicle if found and parked, null otherwise
     */
    public Vehicle findVehicle(String licensePlate) {
        if (licensePlate == null) return null;
        return parkedVehicles.get(licensePlate.toUpperCase().trim());
    }
    
    /**
     * Adds an event listener.
     * 
     * @param listener Event listener to add
     */
    public void addEventListener(ParkingEventListener listener) {
        if (listener != null) {
            eventListeners.add(listener);
        }
    }
    
    /**
     * Removes an event listener.
     * 
     * @param listener Event listener to remove
     */
    public void removeEventListener(ParkingEventListener listener) {
        eventListeners.remove(listener);
    }
    
    // Event notification methods
    private void notifyVehicleParked(Vehicle vehicle, ParkingSpot spot, ParkingTicket ticket) {
        for (ParkingEventListener listener : eventListeners) {
            try {
                listener.onVehicleParked(vehicle, spot, ticket);
            } catch (Exception e) {
                // Log error but continue with other listeners
                System.err.println("Error notifying listener: " + e.getMessage());
            }
        }
    }
    
    private void notifyVehicleExited(Vehicle vehicle, ParkingSpot spot, ParkingTicket ticket, BigDecimal fee) {
        for (ParkingEventListener listener : eventListeners) {
            try {
                listener.onVehicleExited(vehicle, spot, ticket, fee);
            } catch (Exception e) {
                System.err.println("Error notifying listener: " + e.getMessage());
            }
        }
    }
    
    private void notifyParkingFailed(Vehicle vehicle, String reason) {
        for (ParkingEventListener listener : eventListeners) {
            try {
                listener.onParkingFailed(vehicle, reason);
            } catch (Exception e) {
                System.err.println("Error notifying listener: " + e.getMessage());
            }
        }
    }
    
    private void notifyCapacityChanged() {
        for (ParkingEventListener listener : eventListeners) {
            try {
                listener.onCapacityChanged(getCapacityInfo());
            } catch (Exception e) {
                System.err.println("Error notifying listener: " + e.getMessage());
            }
        }
    }
    
    private void notifySpotReserved(ParkingSpot spot, VehicleType vehicleType, int durationMinutes) {
        for (ParkingEventListener listener : eventListeners) {
            try {
                listener.onSpotReserved(spot, vehicleType, durationMinutes);
            } catch (Exception e) {
                System.err.println("Error notifying listener: " + e.getMessage());
            }
        }
    }
    
    // Getters and setters
    public String getLotId() { return lotId; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public List<Level> getLevels() { return new ArrayList<>(levels); }
    public PricingStrategy getPricingStrategy() { return pricingStrategy; }
    public void setPricingStrategy(PricingStrategy pricingStrategy) { 
        this.pricingStrategy = pricingStrategy != null ? pricingStrategy : this.pricingStrategy; 
    }
    
    @Override
    public String toString() {
        return String.format("ParkingLot{id='%s', name='%s', levels=%d, totalSpots=%d}", 
                           lotId, name, levels.size(), 
                           totalCapacity.values().stream().mapToInt(Integer::intValue).sum());
    }
}