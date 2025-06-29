package lld.youtubetrending;

/**
 * Enum representing different video categories for trending analysis
 */
public enum VideoCategory {
    ENTERTAINMENT("Entertainment", 1),
    MUSIC("Music", 2),
    GAMING("Gaming", 3),
    NEWS("News & Politics", 4),
    EDUCATION("Education", 5),
    SPORTS("Sports", 6),
    TECHNOLOGY("Technology", 7),
    LIFESTYLE("Lifestyle", 8),
    COMEDY("Comedy", 9),
    SCIENCE("Science", 10),
    TRAVEL("Travel", 11),
    FOOD("Food", 12);
    
    private final String displayName;
    private final int categoryId;
    
    VideoCategory(String displayName, int categoryId) {
        this.displayName = displayName;
        this.categoryId = categoryId;
    }
    
    public String getDisplayName() { return displayName; }
    public int getCategoryId() { return categoryId; }
    
    public static VideoCategory fromId(int categoryId) {
        for (VideoCategory category : values()) {
            if (category.categoryId == categoryId) {
                return category;
            }
        }
        return ENTERTAINMENT; // Default
    }
}