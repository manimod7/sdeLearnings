package lld.parkinglot;

/**
 * Enumeration representing different types of parking spots.
 * 
 * Each spot type has specific characteristics like size capacity,
 * pricing category, and accessibility features.
 * 
 * Design Pattern: Enum with behavior
 * Benefits:
 * - Type safety for parking spot classification
 * - Encapsulated spot characteristics
 * - Clear vehicle compatibility mapping
 * - Easy pricing and policy management
 */
public enum SpotType {
    MOTORCYCLE("Motorcycle Spot", VehicleSize.SMALL, 1.0, "M"),
    COMPACT("Compact Spot", VehicleSize.MEDIUM, 1.0, "C"),
    LARGE("Large Spot", VehicleSize.LARGE, 1.2, "L"),
    HANDICAPPED("Handicapped Spot", VehicleSize.MEDIUM, 1.0, "H"),
    ELECTRIC("Electric Vehicle Spot", VehicleSize.MEDIUM, 1.1, "E"),
    VIP("VIP Spot", VehicleSize.LARGE, 2.0, "V");
    
    private final String displayName;
    private final VehicleSize maxVehicleSize;
    private final double pricingMultiplier;
    private final String code;
    
    SpotType(String displayName, VehicleSize maxVehicleSize, double pricingMultiplier, String code) {
        this.displayName = displayName;
        this.maxVehicleSize = maxVehicleSize;
        this.pricingMultiplier = pricingMultiplier;
        this.code = code;
    }
    
    /**
     * Gets the human-readable display name for this spot type.
     * 
     * @return Display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Gets the maximum vehicle size that can fit in this spot type.
     * 
     * @return Maximum vehicle size
     */
    public VehicleSize getMaxVehicleSize() {
        return maxVehicleSize;
    }
    
    /**
     * Gets the pricing multiplier for this spot type.
     * Base rate is multiplied by this value.
     * 
     * @return Pricing multiplier
     */
    public double getPricingMultiplier() {
        return pricingMultiplier;
    }
    
    /**
     * Gets the short code for this spot type.
     * Used in spot ID generation.
     * 
     * @return Short code
     */
    public String getCode() {
        return code;
    }
    
    /**
     * Checks if a vehicle type can park in this spot type.
     * 
     * @param vehicleType Vehicle type to check
     * @return true if vehicle can park here
     */
    public boolean canAccommodate(VehicleType vehicleType) {
        // Special rules for specific spot types
        if (this == HANDICAPPED && !vehicleType.requiresHandicappedAccess()) {
            // Handicapped spots are reserved for handicapped vehicles only
            return false;
        }
        
        if (this == ELECTRIC && !vehicleType.isElectric()) {
            // Electric spots are reserved for electric vehicles only
            return false;
        }
        
        // General size compatibility check
        return vehicleType.getSize().canFitIn(this.maxVehicleSize);
    }
    
    /**
     * Checks if this spot type requires special authorization.
     * 
     * @return true if special authorization required
     */
    public boolean requiresSpecialAuthorization() {
        return this == HANDICAPPED || this == VIP;
    }
    
    /**
     * Checks if this spot type has charging capabilities.
     * 
     * @return true if charging available
     */
    public boolean hasChargingCapability() {
        return this == ELECTRIC;
    }
    
    /**
     * Checks if this is a premium spot type.
     * 
     * @return true if premium spot
     */
    public boolean isPremium() {
        return this == VIP || this == ELECTRIC;
    }
    
    /**
     * Gets the typical dimensions for this spot type in meters.
     * 
     * @return Spot dimensions as [width, length]
     */
    public double[] getDimensionsMeters() {
        switch (this) {
            case MOTORCYCLE:
                return new double[]{1.5, 3.0}; // 1.5m x 3.0m
            case COMPACT:
                return new double[]{2.4, 4.8}; // 2.4m x 4.8m
            case LARGE:
                return new double[]{2.7, 5.5}; // 2.7m x 5.5m
            case HANDICAPPED:
                return new double[]{3.0, 5.0}; // 3.0m x 5.0m (wider for accessibility)
            case ELECTRIC:
                return new double[]{2.4, 4.8}; // 2.4m x 4.8m (same as compact)
            case VIP:
                return new double[]{3.0, 6.0}; // 3.0m x 6.0m (extra spacious)
            default:
                return new double[]{2.4, 4.8};
        }
    }
    
    /**
     * Gets the area of this spot type in square meters.
     * 
     * @return Area in square meters
     */
    public double getAreaSquareMeters() {
        double[] dimensions = getDimensionsMeters();
        return dimensions[0] * dimensions[1];
    }
    
    /**
     * Gets spot type from string input (case-insensitive).
     * 
     * @param typeStr String representation of spot type
     * @return SpotType enum value
     * @throws IllegalArgumentException if string doesn't match any type
     */
    public static SpotType fromString(String typeStr) {
        if (typeStr == null) {
            throw new IllegalArgumentException("Spot type string cannot be null");
        }
        
        switch (typeStr.trim().toUpperCase()) {
            case "MOTORCYCLE":
            case "BIKE":
            case "M":
                return MOTORCYCLE;
            case "COMPACT":
            case "C":
            case "SMALL":
                return COMPACT;
            case "LARGE":
            case "L":
            case "BIG":
                return LARGE;
            case "HANDICAPPED":
            case "H":
            case "DISABLED":
            case "HANDICAP":
                return HANDICAPPED;
            case "ELECTRIC":
            case "E":
            case "EV":
                return ELECTRIC;
            case "VIP":
            case "V":
            case "PREMIUM":
                return VIP;
            default:
                throw new IllegalArgumentException("Invalid spot type: " + typeStr);
        }
    }
    
    /**
     * Gets all standard spot types (excluding special types).
     * 
     * @return Array of standard spot types
     */
    public static SpotType[] getStandardTypes() {
        return new SpotType[]{MOTORCYCLE, COMPACT, LARGE};
    }
    
    /**
     * Gets all special spot types that require authorization.
     * 
     * @return Array of special spot types
     */
    public static SpotType[] getSpecialTypes() {
        return new SpotType[]{HANDICAPPED, ELECTRIC, VIP};
    }
    
    /**
     * Gets recommended spot distribution percentages for a parking lot.
     * 
     * @return Array of percentages [motorcycle, compact, large, handicapped, electric, vip]
     */
    public static double[] getRecommendedDistribution() {
        return new double[]{0.15, 0.50, 0.25, 0.05, 0.03, 0.02}; // 15%, 50%, 25%, 5%, 3%, 2%
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}