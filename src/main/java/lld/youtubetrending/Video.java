package lld.youtubetrending;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Represents a video with all metadata and engagement metrics
 * Thread-safe implementation for concurrent metric updates
 */
public class Video {
    private final String videoId;
    private final String title;
    private final String channelId;
    private final String channelName;
    private final VideoCategory category;
    private final LocalDateTime uploadTime;
    private final String region;
    private final int durationSeconds;
    
    // Engagement metrics - thread-safe atomic counters
    private final AtomicLong viewCount;
    private final AtomicLong likeCount;
    private final AtomicLong commentCount;
    private final AtomicLong shareCount;
    private final AtomicLong dislikeCount;
    
    // Trending metrics
    private volatile double trendingScore;
    private volatile int percentileRank;
    private volatile LocalDateTime lastScoreUpdate;
    
    // Metadata
    private final String description;
    private final String[] tags;
    private final String thumbnailUrl;
    
    public Video(String videoId, String title, String channelId, String channelName,
                VideoCategory category, String region, int durationSeconds, 
                String description, String[] tags, String thumbnailUrl) {
        this.videoId = videoId;
        this.title = title;
        this.channelId = channelId;
        this.channelName = channelName;
        this.category = category;
        this.uploadTime = LocalDateTime.now();
        this.region = region;
        this.durationSeconds = durationSeconds;
        this.description = description;
        this.tags = tags != null ? tags.clone() : new String[0];
        this.thumbnailUrl = thumbnailUrl;
        
        // Initialize atomic counters
        this.viewCount = new AtomicLong(0);
        this.likeCount = new AtomicLong(0);
        this.commentCount = new AtomicLong(0);
        this.shareCount = new AtomicLong(0);
        this.dislikeCount = new AtomicLong(0);
        
        // Initialize trending metrics
        this.trendingScore = 0.0;
        this.percentileRank = 0;
        this.lastScoreUpdate = LocalDateTime.now();
    }
    
    /**
     * Update engagement metrics atomically
     */
    public void updateMetrics(long views, long likes, long comments, long shares, long dislikes) {
        viewCount.set(views);
        likeCount.set(likes);
        commentCount.set(comments);
        shareCount.set(shares);
        dislikeCount.set(dislikes);
    }
    
    /**
     * Increment view count
     */
    public long incrementViews(long delta) {
        return viewCount.addAndGet(delta);
    }
    
    /**
     * Increment like count
     */
    public long incrementLikes(long delta) {
        return likeCount.addAndGet(delta);
    }
    
    /**
     * Increment comment count
     */
    public long incrementComments(long delta) {
        return commentCount.addAndGet(delta);
    }
    
    /**
     * Increment share count
     */
    public long incrementShares(long delta) {
        return shareCount.addAndGet(delta);
    }
    
    /**
     * Update trending score
     */
    public synchronized void updateTrendingScore(double score, int percentile) {
        this.trendingScore = score;
        this.percentileRank = percentile;
        this.lastScoreUpdate = LocalDateTime.now();
    }
    
    /**
     * Calculate video age in hours
     */
    public double getAgeInHours() {
        return java.time.Duration.between(uploadTime, LocalDateTime.now()).toHours();
    }
    
    /**
     * Calculate engagement rate
     */
    public double getEngagementRate() {
        long totalViews = viewCount.get();
        if (totalViews == 0) return 0.0;
        
        long totalEngagement = likeCount.get() + commentCount.get() + shareCount.get();
        return (double) totalEngagement / totalViews;
    }
    
    /**
     * Calculate like-to-dislike ratio
     */
    public double getLikeRatio() {
        long likes = likeCount.get();
        long dislikes = dislikeCount.get();
        
        if (likes + dislikes == 0) return 1.0;
        return (double) likes / (likes + dislikes);
    }
    
    /**
     * Get views per hour rate
     */
    public double getViewsPerHour() {
        double ageHours = getAgeInHours();
        if (ageHours == 0) return 0.0;
        return viewCount.get() / ageHours;
    }
    
    /**
     * Check if video matches search criteria
     */
    public boolean matches(String query, VideoCategory categoryFilter, String regionFilter) {
        if (categoryFilter != null && category != categoryFilter) {
            return false;
        }
        
        if (regionFilter != null && !region.equalsIgnoreCase(regionFilter)) {
            return false;
        }
        
        if (query == null || query.trim().isEmpty()) {
            return true;
        }
        
        String lowerQuery = query.toLowerCase();
        return title.toLowerCase().contains(lowerQuery) ||
               channelName.toLowerCase().contains(lowerQuery) ||
               description.toLowerCase().contains(lowerQuery) ||
               containsTag(lowerQuery);
    }
    
    private boolean containsTag(String query) {
        for (String tag : tags) {
            if (tag.toLowerCase().contains(query)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get video metrics summary
     */
    public VideoMetrics getMetrics() {
        return new VideoMetrics(
            viewCount.get(),
            likeCount.get(),
            commentCount.get(),
            shareCount.get(),
            dislikeCount.get(),
            getEngagementRate(),
            getLikeRatio(),
            getViewsPerHour(),
            trendingScore,
            percentileRank
        );
    }
    
    // Getters
    public String getVideoId() { return videoId; }
    public String getTitle() { return title; }
    public String getChannelId() { return channelId; }
    public String getChannelName() { return channelName; }
    public VideoCategory getCategory() { return category; }
    public LocalDateTime getUploadTime() { return uploadTime; }
    public String getRegion() { return region; }
    public int getDurationSeconds() { return durationSeconds; }
    public String getDescription() { return description; }
    public String[] getTags() { return tags.clone(); }
    public String getThumbnailUrl() { return thumbnailUrl; }
    
    public long getViewCount() { return viewCount.get(); }
    public long getLikeCount() { return likeCount.get(); }
    public long getCommentCount() { return commentCount.get(); }
    public long getShareCount() { return shareCount.get(); }
    public long getDislikeCount() { return dislikeCount.get(); }
    
    public double getTrendingScore() { return trendingScore; }
    public int getPercentileRank() { return percentileRank; }
    public LocalDateTime getLastScoreUpdate() { return lastScoreUpdate; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Video video = (Video) obj;
        return Objects.equals(videoId, video.videoId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(videoId);
    }
    
    @Override
    public String toString() {
        return String.format("Video{id='%s', title='%s', views=%d, score=%.2f, rank=%d}", 
                           videoId, title, viewCount.get(), trendingScore, percentileRank);
    }
    
    // Inner class for metrics
    public static class VideoMetrics {
        private final long views;
        private final long likes;
        private final long comments;
        private final long shares;
        private final long dislikes;
        private final double engagementRate;
        private final double likeRatio;
        private final double viewsPerHour;
        private final double trendingScore;
        private final int percentileRank;
        
        public VideoMetrics(long views, long likes, long comments, long shares, long dislikes,
                          double engagementRate, double likeRatio, double viewsPerHour,
                          double trendingScore, int percentileRank) {
            this.views = views;
            this.likes = likes;
            this.comments = comments;
            this.shares = shares;
            this.dislikes = dislikes;
            this.engagementRate = engagementRate;
            this.likeRatio = likeRatio;
            this.viewsPerHour = viewsPerHour;
            this.trendingScore = trendingScore;
            this.percentileRank = percentileRank;
        }
        
        // Getters
        public long getViews() { return views; }
        public long getLikes() { return likes; }
        public long getComments() { return comments; }
        public long getShares() { return shares; }
        public long getDislikes() { return dislikes; }
        public double getEngagementRate() { return engagementRate; }
        public double getLikeRatio() { return likeRatio; }
        public double getViewsPerHour() { return viewsPerHour; }
        public double getTrendingScore() { return trendingScore; }
        public int getPercentileRank() { return percentileRank; }
    }
}