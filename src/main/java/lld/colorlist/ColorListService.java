package lld.colorlist;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Main service class for managing color lists
 * Implements business logic and provides API endpoints
 * Thread-safe implementation for concurrent operations
 */
public class ColorListService {
    private final Map<String, ColorList> colorLists;
    private final Map<String, Set<String>> userLists; // userId -> listIds
    private final Map<String, User> users;
    
    public ColorListService() {
        this.colorLists = new ConcurrentHashMap<>();
        this.userLists = new ConcurrentHashMap<>();
        this.users = new ConcurrentHashMap<>();
    }
    
    /**
     * Register a new user
     */
    public boolean registerUser(String userId, String name, String email) {
        if (users.containsKey(userId)) {
            return false;
        }
        
        User user = new User(userId, name, email);
        users.put(userId, user);
        userLists.put(userId, ConcurrentHashMap.newKeySet());
        
        return true;
    }
    
    /**
     * Create a new color list
     */
    public String createColorList(String name, String ownerId, String description) {
        if (!users.containsKey(ownerId)) {
            throw new IllegalArgumentException("User not found: " + ownerId);
        }
        
        String listId = generateListId();
        ColorList colorList = new ColorList(listId, name, ownerId);
        
        if (description != null && !description.trim().isEmpty()) {
            colorList.updateDescription(description, ownerId);
        }
        
        colorLists.put(listId, colorList);
        userLists.get(ownerId).add(listId);
        
        return listId;
    }
    
    /**
     * Add color to a list
     */
    public boolean addColorToList(String listId, Color color, String userId) {
        ColorList colorList = colorLists.get(listId);
        if (colorList == null) {
            return false;
        }
        
        return colorList.addColor(color, userId);
    }
    
    /**
     * Remove color from a list
     */
    public boolean removeColorFromList(String listId, Color color, String userId) {
        ColorList colorList = colorLists.get(listId);
        if (colorList == null) {
            return false;
        }
        
        return colorList.removeColor(color, userId);
    }
    
    /**
     * Share color list with another user
     */
    public boolean shareColorList(String listId, String targetUserId, 
                                 ColorList.Permission permission, String requesterId) {
        ColorList colorList = colorLists.get(listId);
        if (colorList == null || !users.containsKey(targetUserId)) {
            return false;
        }
        
        boolean shared = colorList.shareWith(targetUserId, permission, requesterId);
        if (shared) {
            userLists.get(targetUserId).add(listId);
        }
        
        return shared;
    }
    
    /**
     * Get color lists accessible by user
     */
    public List<ColorListSummary> getUserColorLists(String userId) {
        if (!users.containsKey(userId)) {
            return Collections.emptyList();
        }
        
        Set<String> userListIds = userLists.get(userId);
        List<ColorListSummary> summaries = new ArrayList<>();
        
        for (String listId : userListIds) {
            ColorList colorList = colorLists.get(listId);
            if (colorList != null && colorList.hasPermission(userId, ColorList.Permission.VIEW)) {
                summaries.add(new ColorListSummary(colorList, userId));
            }
        }
        
        return summaries;
    }
    
    /**
     * Get color list details
     */
    public ColorList getColorList(String listId, String userId) {
        ColorList colorList = colorLists.get(listId);
        if (colorList == null || !colorList.hasPermission(userId, ColorList.Permission.VIEW)) {
            return null;
        }
        
        return colorList;
    }
    
    /**
     * Search colors across accessible lists
     */
    public List<ColorSearchResult> searchColors(String userId, String query, SearchCriteria criteria) {
        List<ColorSearchResult> results = new ArrayList<>();
        Set<String> userListIds = userLists.get(userId);
        
        if (userListIds == null) {
            return results;
        }
        
        for (String listId : userListIds) {
            ColorList colorList = colorLists.get(listId);
            if (colorList == null || !colorList.hasPermission(userId, ColorList.Permission.VIEW)) {
                continue;
            }
            
            List<Color> colors = colorList.getColors(userId);
            List<Color> matchingColors = filterColors(colors, query, criteria);
            
            for (Color color : matchingColors) {
                results.add(new ColorSearchResult(color, colorList.getListId(), colorList.getName()));
            }
        }
        
        return results;
    }
    
    /**
     * Get popular colors across all public lists
     */
    public List<Color> getPopularColors(int limit) {
        Map<Color, Integer> colorCounts = new HashMap<>();
        
        for (ColorList colorList : colorLists.values()) {
            if (colorList.getAccessLevel() == ColorList.AccessLevel.PUBLIC) {
                List<Color> colors = colorList.getColors(colorList.getOwnerId());
                for (Color color : colors) {
                    colorCounts.put(color, colorCounts.getOrDefault(color, 0) + 1);
                }
            }
        }
        
        return colorCounts.entrySet().stream()
                         .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                         .limit(limit)
                         .map(Map.Entry::getKey)
                         .collect(Collectors.toList());
    }
    
    /**
     * Generate color recommendations based on user's lists
     */
    public List<Color> getRecommendations(String userId, int limit) {
        Set<String> userListIds = userLists.get(userId);
        if (userListIds == null || userListIds.isEmpty()) {
            return getPopularColors(limit);
        }
        
        // Analyze user's color preferences
        Map<String, Integer> colorFamilyPreferences = new HashMap<>();
        Set<Color> userColors = new HashSet<>();
        
        for (String listId : userListIds) {
            ColorList colorList = colorLists.get(listId);
            if (colorList != null) {
                List<Color> colors = colorList.getColors(userId);
                for (Color color : colors) {
                    userColors.add(color);
                    String family = ColorUtils.getColorFamily(color);
                    colorFamilyPreferences.put(family, colorFamilyPreferences.getOrDefault(family, 0) + 1);
                }
            }
        }
        
        // Find most preferred color family
        String preferredFamily = colorFamilyPreferences.entrySet().stream()
                                                      .max(Map.Entry.comparingByValue())
                                                      .map(Map.Entry::getKey)
                                                      .orElse("Blue");
        
        // Generate recommendations based on preferred family
        List<Color> recommendations = new ArrayList<>();
        
        // Add complementary colors to user's existing colors
        for (Color userColor : userColors) {
            if (recommendations.size() >= limit) break;
            
            Color complementary = userColor.getComplementaryColor();
            if (!userColors.contains(complementary)) {
                recommendations.add(complementary);
            }
        }
        
        // Fill remaining with popular colors from preferred family
        List<Color> popularColors = getPopularColors(50);
        for (Color color : popularColors) {
            if (recommendations.size() >= limit) break;
            
            if (ColorUtils.getColorFamily(color).equals(preferredFamily) && 
                !userColors.contains(color) && 
                !recommendations.contains(color)) {
                recommendations.add(color);
            }
        }
        
        return recommendations.subList(0, Math.min(recommendations.size(), limit));
    }
    
    /**
     * Delete color list
     */
    public boolean deleteColorList(String listId, String userId) {
        ColorList colorList = colorLists.get(listId);
        if (colorList == null || !colorList.getOwnerId().equals(userId)) {
            return false;
        }
        
        // Remove from all users' lists
        for (Set<String> userListSet : userLists.values()) {
            userListSet.remove(listId);
        }
        
        colorLists.remove(listId);
        return true;
    }
    
    /**
     * Filter colors based on search criteria
     */
    private List<Color> filterColors(List<Color> colors, String query, SearchCriteria criteria) {
        return colors.stream()
                    .filter(color -> matchesQuery(color, query, criteria))
                    .collect(Collectors.toList());
    }
    
    /**
     * Check if color matches search query
     */
    private boolean matchesQuery(Color color, String query, SearchCriteria criteria) {
        if (query == null || query.trim().isEmpty()) {
            return true;
        }
        
        String lowerQuery = query.toLowerCase();
        
        switch (criteria) {
            case NAME:
                return color.getName().toLowerCase().contains(lowerQuery);
            case HEX:
                return color.getHexCode().toLowerCase().contains(lowerQuery);
            case FAMILY:
                return ColorUtils.getColorFamily(color).toLowerCase().contains(lowerQuery);
            case ALL:
            default:
                return color.getName().toLowerCase().contains(lowerQuery) ||
                       color.getHexCode().toLowerCase().contains(lowerQuery) ||
                       ColorUtils.getColorFamily(color).toLowerCase().contains(lowerQuery);
        }
    }
    
    /**
     * Generate unique list ID
     */
    private String generateListId() {
        return "list_" + System.currentTimeMillis() + "_" + Math.random();
    }
    
    // Inner classes
    public static class ColorListSummary {
        public final String listId;
        public final String name;
        public final String ownerId;
        public final int colorCount;
        public final ColorList.AccessLevel accessLevel;
        public final ColorList.Permission userPermission;
        
        public ColorListSummary(ColorList colorList, String userId) {
            this.listId = colorList.getListId();
            this.name = colorList.getName();
            this.ownerId = colorList.getOwnerId();
            this.colorCount = colorList.getColorCount();
            this.accessLevel = colorList.getAccessLevel();
            
            // Determine user's permission level
            if (userId.equals(colorList.getOwnerId())) {
                this.userPermission = ColorList.Permission.ADMIN;
            } else {
                this.userPermission = colorList.getUserPermissions(colorList.getOwnerId())
                                               .getOrDefault(userId, ColorList.Permission.VIEW);
            }
        }
    }
    
    public static class ColorSearchResult {
        public final Color color;
        public final String listId;
        public final String listName;
        
        public ColorSearchResult(Color color, String listId, String listName) {
            this.color = color;
            this.listId = listId;
            this.listName = listName;
        }
    }
    
    public enum SearchCriteria {
        NAME, HEX, FAMILY, ALL
    }
    
    // Simple User class
    public static class User {
        private final String userId;
        private final String name;
        private final String email;
        
        public User(String userId, String name, String email) {
            this.userId = userId;
            this.name = name;
            this.email = email;
        }
        
        public String getUserId() { return userId; }
        public String getName() { return name; }
        public String getEmail() { return email; }
    }
}