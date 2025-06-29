package lld.parkinglot;

/**
 * Enumeration representing different types of vehicles that can park.
 * 
 * Each vehicle type has specific characteristics like size requirements
 * and compatible parking spot types.
 * 
 * Design Pattern: Enum with behavior
 * Benefits:
 * - Type safety for vehicle classification
 * - Encapsulated size and compatibility logic
 * - Easy to extend for new vehicle types
 * - Clear mapping to parking spot requirements
 */
public enum VehicleType {
    MOTORCYCLE("Motorcycle", VehicleSize.SMALL, 1, SpotType.MOTORCYCLE),
    CAR("Car", VehicleSize.MEDIUM, 1, SpotType.COMPACT),
    TRUCK("Truck", VehicleSize.LARGE, 1, SpotType.LARGE),
    BUS("Bus", VehicleSize.EXTRA_LARGE, 2, SpotType.LARGE),
    ELECTRIC_CAR("Electric Car", VehicleSize.MEDIUM, 1, SpotType.COMPACT),
    HANDICAPPED("Handicapped Vehicle", VehicleSize.MEDIUM, 1, SpotType.HANDICAPPED);
    
    private final String displayName;
    private final VehicleSize size;
    private final int spotsRequired;
    private final SpotType preferredSpotType;
    
    VehicleType(String displayName, VehicleSize size, int spotsRequired, SpotType preferredSpotType) {
        this.displayName = displayName;
        this.size = size;
        this.spotsRequired = spotsRequired;
        this.preferredSpotType = preferredSpotType;
    }
    
    /**
     * Gets the human-readable display name for this vehicle type.
     * 
     * @return Display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Gets the size classification of this vehicle type.
     * 
     * @return Vehicle size
     */
    public VehicleSize getSize() {
        return size;
    }
    
    /**
     * Gets the number of parking spots required for this vehicle type.
     * Most vehicles require 1 spot, but buses might require 2.
     * 
     * @return Number of spots required
     */
    public int getSpotsRequired() {
        return spotsRequired;
    }
    
    /**
     * Gets the preferred parking spot type for this vehicle.
     * 
     * @return Preferred spot type
     */
    public SpotType getPreferredSpotType() {
        return preferredSpotType;
    }
    
    /**
     * Gets compatible parking spot types for this vehicle.
     * Vehicles can typically park in larger spots than their preferred size.
     * 
     * @return Array of compatible spot types (ordered by preference)
     */
    public SpotType[] getCompatibleSpotTypes() {
        switch (this) {
            case MOTORCYCLE:
                return new SpotType[]{SpotType.MOTORCYCLE, SpotType.COMPACT, SpotType.LARGE};
            case CAR:
            case ELECTRIC_CAR:
                return new SpotType[]{SpotType.COMPACT, SpotType.LARGE};
            case TRUCK:
                return new SpotType[]{SpotType.LARGE};
            case BUS:
                return new SpotType[]{SpotType.LARGE}; // Buses need special handling for multiple spots
            case HANDICAPPED:
                return new SpotType[]{SpotType.HANDICAPPED, SpotType.COMPACT, SpotType.LARGE};
            default:
                return new SpotType[]{preferredSpotType};
        }
    }
    
    /**
     * Checks if this vehicle type can fit in the specified spot type.
     * 
     * @param spotType Spot type to check
     * @return true if vehicle can fit
     */
    public boolean canFitInSpotType(SpotType spotType) {
        SpotType[] compatibleTypes = getCompatibleSpotTypes();
        for (SpotType compatibleType : compatibleTypes) {
            if (compatibleType == spotType) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks if this is an electric vehicle that requires charging.
     * 
     * @return true if electric vehicle
     */
    public boolean isElectric() {
        return this == ELECTRIC_CAR;
    }
    
    /**
     * Checks if this vehicle requires handicapped-accessible parking.
     * 
     * @return true if handicapped vehicle
     */
    public boolean requiresHandicappedAccess() {
        return this == HANDICAPPED;
    }
    
    /**
     * Checks if this is a commercial vehicle.
     * 
     * @return true if commercial vehicle
     */
    public boolean isCommercial() {
        return this == TRUCK || this == BUS;
    }
    
    /**
     * Gets the parking fee multiplier for this vehicle type.
     * Larger vehicles typically pay more.
     * 
     * @return Fee multiplier (1.0 = standard rate)
     */
    public double getFeeMultiplier() {
        switch (this) {
            case MOTORCYCLE:
                return 0.5; // Half price for motorcycles
            case CAR:
            case ELECTRIC_CAR:
            case HANDICAPPED:
                return 1.0; // Standard rate
            case TRUCK:
                return 1.5; // 50% more for trucks
            case BUS:
                return 2.0; // Double rate for buses
            default:
                return 1.0;
        }
    }
    
    /**
     * Gets the estimated parking duration for this vehicle type.
     * Used for analytics and capacity planning.
     * 
     * @return Average parking duration in minutes
     */
    public int getAverageParkingDurationMinutes() {
        switch (this) {
            case MOTORCYCLE:
                return 60; // 1 hour average
            case CAR:
            case ELECTRIC_CAR:
                return 120; // 2 hours average
            case HANDICAPPED:
                return 90; // 1.5 hours average
            case TRUCK:
                return 180; // 3 hours average
            case BUS:
                return 240; // 4 hours average
            default:
                return 120;
        }
    }
    
    /**
     * Gets vehicle type from string input (case-insensitive).
     * 
     * @param typeStr String representation of vehicle type
     * @return VehicleType enum value
     * @throws IllegalArgumentException if string doesn't match any type
     */
    public static VehicleType fromString(String typeStr) {
        if (typeStr == null) {
            throw new IllegalArgumentException("Vehicle type string cannot be null");
        }
        
        switch (typeStr.trim().toUpperCase()) {
            case "MOTORCYCLE":
            case "BIKE":
            case "MOTORBIKE":
                return MOTORCYCLE;
            case "CAR":
            case "AUTOMOBILE":
            case "SEDAN":
            case "SUV":
                return CAR;
            case "TRUCK":
            case "PICKUP":
            case "LORRY":
                return TRUCK;
            case "BUS":
            case "COACH":
                return BUS;
            case "ELECTRIC_CAR":
            case "ELECTRIC":
            case "EV":
                return ELECTRIC_CAR;
            case "HANDICAPPED":
            case "DISABLED":
            case "HANDICAP":
                return HANDICAPPED;
            default:
                throw new IllegalArgumentException("Invalid vehicle type: " + typeStr);
        }
    }
    
    /**
     * Gets all available vehicle types as an array.
     * 
     * @return Array of all vehicle types
     */
    public static VehicleType[] getAllTypes() {
        return values();
    }
    
    /**
     * Gets commercial vehicle types only.
     * 
     * @return Array of commercial vehicle types
     */
    public static VehicleType[] getCommercialTypes() {
        return new VehicleType[]{TRUCK, BUS};
    }
    
    /**
     * Gets personal vehicle types only.
     * 
     * @return Array of personal vehicle types
     */
    public static VehicleType[] getPersonalTypes() {
        return new VehicleType[]{MOTORCYCLE, CAR, ELECTRIC_CAR, HANDICAPPED};
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}