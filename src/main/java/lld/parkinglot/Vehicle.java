package lld.parkinglot;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Abstract base class representing a vehicle that can park in the parking lot.
 * 
 * Encapsulates common vehicle properties and behavior while allowing
 * specific vehicle types to override behavior as needed.
 * 
 * Design Pattern: Template Method Pattern
 * Benefits:
 * - Common behavior in base class
 * - Specific behavior in subclasses
 * - Type safety and polymorphism
 * - Easy to extend for new vehicle types
 */
public abstract class Vehicle {
    protected final String licensePlate;
    protected final VehicleType type;
    protected final String ownerName;
    protected final String ownerPhone;
    protected final LocalDateTime registrationTime;
    protected String color;
    protected String model;
    protected int year;
    
    /**
     * Creates a new Vehicle with required information.
     * 
     * @param licensePlate Vehicle license plate (must be unique)
     * @param type Type of vehicle
     * @param ownerName Name of vehicle owner
     * @param ownerPhone Phone number of owner
     */
    public Vehicle(String licensePlate, VehicleType type, String ownerName, String ownerPhone) {
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            throw new IllegalArgumentException("License plate cannot be null or empty");
        }
        if (type == null) {
            throw new IllegalArgumentException("Vehicle type cannot be null");
        }
        if (ownerName == null || ownerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Owner name cannot be null or empty");
        }
        
        this.licensePlate = licensePlate.toUpperCase().trim();
        this.type = type;
        this.ownerName = ownerName.trim();
        this.ownerPhone = ownerPhone != null ? ownerPhone.trim() : "";
        this.registrationTime = LocalDateTime.now();
    }
    
    /**
     * Checks if this vehicle can fit in the specified spot type.
     * Default implementation delegates to vehicle type.
     * 
     * @param spotType Parking spot type to check
     * @return true if vehicle can fit
     */
    public boolean canFitInSpot(SpotType spotType) {
        return type.canFitInSpotType(spotType);
    }
    
    /**
     * Gets the number of parking spots required for this vehicle.
     * Most vehicles require 1 spot, but some large vehicles might need more.
     * 
     * @return Number of spots required
     */
    public int getSpotsRequired() {
        return type.getSpotsRequired();
    }
    
    /**
     * Gets the parking fee multiplier for this vehicle.
     * Used in pricing calculations.
     * 
     * @return Fee multiplier
     */
    public double getFeeMultiplier() {
        return type.getFeeMultiplier();
    }
    
    /**
     * Validates the license plate format.
     * Can be overridden by subclasses for specific validation rules.
     * 
     * @return true if license plate is valid
     */
    public boolean isValidLicensePlate() {
        // Basic validation: 3-10 alphanumeric characters
        return licensePlate.matches("^[A-Z0-9]{3,10}$");
    }
    
    /**
     * Gets vehicle priority for parking allocation.
     * Higher priority vehicles get preference in spot allocation.
     * 
     * @return Priority level (higher = more priority)
     */
    public int getParkingPriority() {
        if (type.requiresHandicappedAccess()) {
            return 100; // Highest priority for handicapped vehicles
        } else if (type.isElectric()) {
            return 50; // Medium priority for electric vehicles
        } else if (type.isCommercial()) {
            return 25; // Lower priority for commercial vehicles
        } else {
            return 10; // Standard priority for regular vehicles
        }
    }
    
    /**
     * Gets estimated parking duration for this vehicle type.
     * Used for capacity planning and analytics.
     * 
     * @return Estimated duration in minutes
     */
    public int getEstimatedParkingDuration() {
        return type.getAverageParkingDurationMinutes();
    }
    
    /**
     * Checks if this vehicle requires special accommodations.
     * 
     * @return true if special accommodations needed
     */
    public boolean requiresSpecialAccommodations() {
        return type.requiresHandicappedAccess() || type.isElectric();
    }
    
    /**
     * Gets display information for this vehicle.
     * 
     * @return Formatted display string
     */
    public String getDisplayInfo() {
        StringBuilder info = new StringBuilder();
        info.append(type.getDisplayName()).append(" - ").append(licensePlate);
        if (color != null && !color.isEmpty()) {
            info.append(" (").append(color).append(")");
        }
        if (model != null && !model.isEmpty()) {
            info.append(" ").append(model);
        }
        if (year > 0) {
            info.append(" ").append(year);
        }
        return info.toString();
    }
    
    /**
     * Gets a detailed description of the vehicle.
     * 
     * @return Detailed description
     */
    public String getDetailedDescription() {
        return String.format("%s %s %s (%d) - Owner: %s, Phone: %s, Registered: %s",
                           color != null ? color : "Unknown",
                           model != null ? model : "Unknown Model",
                           licensePlate,
                           year > 0 ? year : 0,
                           ownerName,
                           ownerPhone.isEmpty() ? "Not provided" : ownerPhone,
                           registrationTime);
    }
    
    // Getters and setters
    public String getLicensePlate() { return licensePlate; }
    public VehicleType getType() { return type; }
    public String getOwnerName() { return ownerName; }
    public String getOwnerPhone() { return ownerPhone; }
    public LocalDateTime getRegistrationTime() { return registrationTime; }
    public String getColor() { return color; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    
    public void setColor(String color) { this.color = color; }
    public void setModel(String model) { this.model = model; }
    public void setYear(int year) { 
        if (year < 1900 || year > LocalDateTime.now().getYear() + 1) {
            throw new IllegalArgumentException("Invalid year: " + year);
        }
        this.year = year; 
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Vehicle vehicle = (Vehicle) obj;
        return licensePlate.equals(vehicle.licensePlate);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(licensePlate);
    }
    
    @Override
    public String toString() {
        return String.format("%s{licensePlate='%s', type=%s, owner='%s'}", 
                           getClass().getSimpleName(), licensePlate, type, ownerName);
    }
}

/**
 * Concrete implementation for Motorcycle vehicles.
 */
class Motorcycle extends Vehicle {
    private int engineCapacity; // in CC
    private boolean hasHelmet;
    
    public Motorcycle(String licensePlate, String ownerName, String ownerPhone) {
        super(licensePlate, VehicleType.MOTORCYCLE, ownerName, ownerPhone);
    }
    
    public Motorcycle(String licensePlate, String ownerName, String ownerPhone, 
                     int engineCapacity, boolean hasHelmet) {
        this(licensePlate, ownerName, ownerPhone);
        this.engineCapacity = engineCapacity;
        this.hasHelmet = hasHelmet;
    }
    
    @Override
    public boolean isValidLicensePlate() {
        // Motorcycle license plates might have different format
        return licensePlate.matches("^[A-Z0-9]{2,8}$");
    }
    
    @Override
    public int getParkingPriority() {
        return super.getParkingPriority() + (hasHelmet ? 5 : 0); // Bonus for safety
    }
    
    public int getEngineCapacity() { return engineCapacity; }
    public boolean hasHelmet() { return hasHelmet; }
    
    public void setEngineCapacity(int engineCapacity) { this.engineCapacity = engineCapacity; }
    public void setHasHelmet(boolean hasHelmet) { this.hasHelmet = hasHelmet; }
}

/**
 * Concrete implementation for Car vehicles.
 */
class Car extends Vehicle {
    private int numberOfDoors;
    private String fuelType;
    private boolean isConvertible;
    
    public Car(String licensePlate, String ownerName, String ownerPhone) {
        super(licensePlate, VehicleType.CAR, ownerName, ownerPhone);
        this.numberOfDoors = 4; // Default
        this.fuelType = "Gasoline"; // Default
    }
    
    public Car(String licensePlate, String ownerName, String ownerPhone,
               int numberOfDoors, String fuelType, boolean isConvertible) {
        this(licensePlate, ownerName, ownerPhone);
        this.numberOfDoors = numberOfDoors;
        this.fuelType = fuelType;
        this.isConvertible = isConvertible;
    }
    
    @Override
    public int getParkingPriority() {
        int priority = super.getParkingPriority();
        if (isConvertible) {
            priority += 5; // Convertibles might need covered parking
        }
        return priority;
    }
    
    public int getNumberOfDoors() { return numberOfDoors; }
    public String getFuelType() { return fuelType; }
    public boolean isConvertible() { return isConvertible; }
    
    public void setNumberOfDoors(int numberOfDoors) { this.numberOfDoors = numberOfDoors; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }
    public void setConvertible(boolean convertible) { this.isConvertible = convertible; }
}

/**
 * Concrete implementation for Truck vehicles.
 */
class Truck extends Vehicle {
    private double cargoCapacity; // in tons
    private boolean hasTrailer;
    private String truckType;
    
    public Truck(String licensePlate, String ownerName, String ownerPhone) {
        super(licensePlate, VehicleType.TRUCK, ownerName, ownerPhone);
        this.truckType = "Standard";
    }
    
    public Truck(String licensePlate, String ownerName, String ownerPhone,
                 double cargoCapacity, boolean hasTrailer, String truckType) {
        this(licensePlate, ownerName, ownerPhone);
        this.cargoCapacity = cargoCapacity;
        this.hasTrailer = hasTrailer;
        this.truckType = truckType;
    }
    
    @Override
    public int getSpotsRequired() {
        return hasTrailer ? 2 : 1; // Trucks with trailers need 2 spots
    }
    
    @Override
    public double getFeeMultiplier() {
        return super.getFeeMultiplier() * (hasTrailer ? 1.5 : 1.0);
    }
    
    @Override
    public int getEstimatedParkingDuration() {
        // Commercial vehicles typically park longer
        return super.getEstimatedParkingDuration() + (hasTrailer ? 60 : 30);
    }
    
    public double getCargoCapacity() { return cargoCapacity; }
    public boolean hasTrailer() { return hasTrailer; }
    public String getTruckType() { return truckType; }
    
    public void setCargoCapacity(double cargoCapacity) { this.cargoCapacity = cargoCapacity; }
    public void setHasTrailer(boolean hasTrailer) { this.hasTrailer = hasTrailer; }
    public void setTruckType(String truckType) { this.truckType = truckType; }
}

/**
 * Concrete implementation for Electric Car vehicles.
 */
class ElectricCar extends Car {
    private int batteryCapacity; // in kWh
    private int currentChargePercent;
    private boolean needsCharging;
    
    public ElectricCar(String licensePlate, String ownerName, String ownerPhone) {
        super(licensePlate, ownerName, ownerPhone);
        // Override the type to be electric
        // Note: In a real implementation, we might need to redesign the inheritance hierarchy
        this.currentChargePercent = 50; // Default 50% charge
    }
    
    public ElectricCar(String licensePlate, String ownerName, String ownerPhone,
                      int batteryCapacity, int currentChargePercent) {
        this(licensePlate, ownerName, ownerPhone);
        this.batteryCapacity = batteryCapacity;
        this.currentChargePercent = Math.max(0, Math.min(100, currentChargePercent));
        this.needsCharging = currentChargePercent < 20; // Need charging if below 20%
    }
    
    @Override
    public boolean canFitInSpot(SpotType spotType) {
        // Electric cars prefer electric spots but can use regular spots
        return spotType == SpotType.ELECTRIC || super.canFitInSpot(spotType);
    }
    
    @Override
    public int getParkingPriority() {
        int priority = super.getParkingPriority() + 25; // Higher priority for electric vehicles
        if (needsCharging) {
            priority += 50; // Much higher priority if needs charging
        }
        return priority;
    }
    
    @Override
    public boolean requiresSpecialAccommodations() {
        return true; // Electric vehicles need charging stations
    }
    
    public int getBatteryCapacity() { return batteryCapacity; }
    public int getCurrentChargePercent() { return currentChargePercent; }
    public boolean needsCharging() { return needsCharging; }
    
    public void setBatteryCapacity(int batteryCapacity) { this.batteryCapacity = batteryCapacity; }
    public void setCurrentChargePercent(int currentChargePercent) { 
        this.currentChargePercent = Math.max(0, Math.min(100, currentChargePercent));
        this.needsCharging = this.currentChargePercent < 20;
    }
    public void setNeedsCharging(boolean needsCharging) { this.needsCharging = needsCharging; }
}