package lld.colorlist;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Represents a user's color list with access control and metadata
 * Thread-safe implementation using concurrent collections
 */
public class ColorList {
    private final String listId;
    private final String name;
    private final String ownerId;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String description;
    private AccessLevel accessLevel;
    
    // Thread-safe collections
    private final List<Color> colors;
    private final Set<String> sharedUsers;
    private final Map<String, Permission> userPermissions;
    
    public enum AccessLevel {
        PRIVATE,    // Only owner can access
        SHARED,     // Specific users can access
        PUBLIC      // Anyone can view
    }
    
    public enum Permission {
        VIEW,       // Can only view the list
        EDIT,       // Can add/remove colors
        ADMIN       // Can share and modify permissions
    }
    
    public ColorList(String listId, String name, String ownerId) {
        this.listId = listId;
        this.name = name;
        this.ownerId = ownerId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.description = "";
        this.accessLevel = AccessLevel.PRIVATE;
        
        this.colors = new CopyOnWriteArrayList<>();
        this.sharedUsers = new HashSet<>();
        this.userPermissions = new HashMap<>();
    }
    
    /**
     * Add color to the list
     */
    public synchronized boolean addColor(Color color, String userId) {
        if (!hasPermission(userId, Permission.EDIT)) {
            return false;
        }
        
        if (colors.contains(color)) {
            return false; // Color already exists
        }
        
        colors.add(color);
        updateTimestamp();
        return true;
    }
    
    /**
     * Remove color from the list
     */
    public synchronized boolean removeColor(Color color, String userId) {
        if (!hasPermission(userId, Permission.EDIT)) {
            return false;
        }
        
        boolean removed = colors.remove(color);
        if (removed) {
            updateTimestamp();
        }
        return removed;
    }
    
    /**
     * Remove color by index
     */
    public synchronized boolean removeColor(int index, String userId) {
        if (!hasPermission(userId, Permission.EDIT)) {
            return false;
        }
        
        if (index < 0 || index >= colors.size()) {
            return false;
        }
        
        colors.remove(index);
        updateTimestamp();
        return true;
    }
    
    /**
     * Get all colors (defensive copy)
     */
    public List<Color> getColors(String userId) {
        if (!hasPermission(userId, Permission.VIEW)) {
            return Collections.emptyList();
        }
        
        return new ArrayList<>(colors);
    }
    
    /**
     * Share list with user
     */
    public synchronized boolean shareWith(String userId, Permission permission, String requesterId) {
        if (!hasPermission(requesterId, Permission.ADMIN)) {
            return false;
        }
        
        sharedUsers.add(userId);
        userPermissions.put(userId, permission);
        
        if (accessLevel == AccessLevel.PRIVATE && !sharedUsers.isEmpty()) {
            accessLevel = AccessLevel.SHARED;
        }
        
        updateTimestamp();
        return true;
    }
    
    /**
     * Revoke access from user
     */
    public synchronized boolean revokeAccess(String userId, String requesterId) {
        if (!hasPermission(requesterId, Permission.ADMIN)) {
            return false;
        }
        
        if (userId.equals(ownerId)) {
            return false; // Cannot revoke owner access
        }
        
        sharedUsers.remove(userId);
        userPermissions.remove(userId);
        updateTimestamp();
        return true;
    }
    
    /**
     * Change access level
     */
    public synchronized boolean changeAccessLevel(AccessLevel newLevel, String userId) {
        if (!hasPermission(userId, Permission.ADMIN)) {
            return false;
        }
        
        this.accessLevel = newLevel;
        updateTimestamp();
        return true;
    }
    
    /**
     * Update description
     */
    public synchronized boolean updateDescription(String newDescription, String userId) {
        if (!hasPermission(userId, Permission.EDIT)) {
            return false;
        }
        
        this.description = newDescription;
        updateTimestamp();
        return true;
    }
    
    /**
     * Check if user has specific permission
     */
    public boolean hasPermission(String userId, Permission requiredPermission) {
        // Owner has all permissions
        if (userId.equals(ownerId)) {
            return true;
        }
        
        // Public lists allow view access to everyone
        if (accessLevel == AccessLevel.PUBLIC && requiredPermission == Permission.VIEW) {
            return true;
        }
        
        // Check shared permissions
        Permission userPermission = userPermissions.get(userId);
        if (userPermission == null) {
            return false;
        }
        
        // Permission hierarchy: ADMIN > EDIT > VIEW
        switch (requiredPermission) {
            case VIEW:
                return true; // If user has any permission, they can view
            case EDIT:
                return userPermission == Permission.EDIT || userPermission == Permission.ADMIN;
            case ADMIN:
                return userPermission == Permission.ADMIN;
            default:
                return false;
        }
    }
    
    /**
     * Get color statistics
     */
    public ColorStatistics getStatistics(String userId) {
        if (!hasPermission(userId, Permission.VIEW)) {
            return null;
        }
        
        return new ColorStatistics(colors);
    }
    
    /**
     * Generate color palette variations
     */
    public List<Color> generatePalette(String userId, PaletteType type) {
        if (!hasPermission(userId, Permission.VIEW) || colors.isEmpty()) {
            return Collections.emptyList();
        }
        
        Color baseColor = colors.get(0); // Use first color as base
        return PaletteGenerator.generatePalette(baseColor, type);
    }
    
    /**
     * Find similar colors
     */
    public List<Color> findSimilarColors(Color targetColor, String userId, double threshold) {
        if (!hasPermission(userId, Permission.VIEW)) {
            return Collections.emptyList();
        }
        
        return colors.stream()
                    .filter(color -> ColorUtils.calculateColorDistance(color, targetColor) <= threshold)
                    .sorted((c1, c2) -> Double.compare(
                        ColorUtils.calculateColorDistance(c1, targetColor),
                        ColorUtils.calculateColorDistance(c2, targetColor)
                    ))
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    /**
     * Export color list to different formats
     */
    public String exportToFormat(ExportFormat format, String userId) {
        if (!hasPermission(userId, Permission.VIEW)) {
            return null;
        }
        
        return ColorExporter.export(colors, format);
    }
    
    private void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters
    public String getListId() { return listId; }
    public String getName() { return name; }
    public String getOwnerId() { return ownerId; }
    public String getDescription() { return description; }
    public AccessLevel getAccessLevel() { return accessLevel; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public int getColorCount() { return colors.size(); }
    
    public Set<String> getSharedUsers(String requesterId) {
        if (!hasPermission(requesterId, Permission.ADMIN)) {
            return Collections.emptySet();
        }
        return new HashSet<>(sharedUsers);
    }
    
    public Map<String, Permission> getUserPermissions(String requesterId) {
        if (!hasPermission(requesterId, Permission.ADMIN)) {
            return Collections.emptyMap();
        }
        return new HashMap<>(userPermissions);
    }
    
    @Override
    public String toString() {
        return String.format("ColorList{id='%s', name='%s', owner='%s', colors=%d, access=%s}", 
                           listId, name, ownerId, colors.size(), accessLevel);
    }
    
    // Inner class for color statistics
    public static class ColorStatistics {
        private final int totalColors;
        private final Color dominantColor;
        private final double averageLuminance;
        private final Map<String, Integer> colorFamilies;
        
        public ColorStatistics(List<Color> colors) {
            this.totalColors = colors.size();
            this.dominantColor = findDominantColor(colors);
            this.averageLuminance = calculateAverageLuminance(colors);
            this.colorFamilies = categorizeColors(colors);
        }
        
        private Color findDominantColor(List<Color> colors) {
            return colors.isEmpty() ? null : colors.get(0); // Simplified implementation
        }
        
        private double calculateAverageLuminance(List<Color> colors) {
            return colors.stream()
                        .mapToDouble(Color::getLuminance)
                        .average()
                        .orElse(0.0);
        }
        
        private Map<String, Integer> categorizeColors(List<Color> colors) {
            Map<String, Integer> families = new HashMap<>();
            for (Color color : colors) {
                String family = ColorUtils.getColorFamily(color);
                families.put(family, families.getOrDefault(family, 0) + 1);
            }
            return families;
        }
        
        // Getters
        public int getTotalColors() { return totalColors; }
        public Color getDominantColor() { return dominantColor; }
        public double getAverageLuminance() { return averageLuminance; }
        public Map<String, Integer> getColorFamilies() { return colorFamilies; }
    }
}