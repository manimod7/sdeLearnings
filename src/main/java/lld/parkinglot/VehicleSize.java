package lld.parkinglot;

/**
 * Enumeration representing different vehicle size classifications.
 * 
 * Used to categorize vehicles by their physical dimensions
 * for appropriate parking spot allocation.
 */
public enum VehicleSize {
    SMALL(1, "Small Vehicle"),
    MEDIUM(2, "Medium Vehicle"), 
    LARGE(3, "Large Vehicle"),
    EXTRA_LARGE(4, "Extra Large Vehicle");
    
    private final int sizeValue;
    private final String description;
    
    VehicleSize(int sizeValue, String description) {
        this.sizeValue = sizeValue;
        this.description = description;
    }
    
    public int getSizeValue() {
        return sizeValue;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Checks if this size can fit in a spot designed for the target size.
     * 
     * @param targetSize Target size to fit in
     * @return true if this size can fit
     */
    public boolean canFitIn(VehicleSize targetSize) {
        return this.sizeValue <= targetSize.sizeValue;
    }
    
    @Override
    public String toString() {
        return description;
    }
}