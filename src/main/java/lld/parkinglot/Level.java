package lld.parkinglot;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a level in the parking lot.
 */
public class Level {
    private final int levelNumber;
    private final Map<SpotType, List<ParkingSpot>> spotsByType;
    private final List<ParkingSpot> allSpots;
    
    public Level(int levelNumber) {
        this.levelNumber = levelNumber;
        this.spotsByType = new ConcurrentHashMap<>();
        this.allSpots = new ArrayList<>();
        
        // Initialize spot type lists
        for (SpotType spotType : SpotType.values()) {
            spotsByType.put(spotType, new ArrayList<>());
        }
    }
    
    public void addSpot(ParkingSpot spot) {
        allSpots.add(spot);
        spotsByType.get(spot.getSpotType()).add(spot);
    }
    
    public ParkingSpot findAvailableSpotOfType(SpotType spotType) {
        return spotsByType.get(spotType).stream()
                .filter(ParkingSpot::isAvailable)
                .findFirst()
                .orElse(null);
    }
    
    public List<ParkingSpot> getSpots() { return new ArrayList<>(allSpots); }
    public int getLevelNumber() { return levelNumber; }
}