package lld.youtubetrending;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Main service for YouTube trending video management
 * Handles video ingestion, trending calculation, and ranking
 */
public class TrendingService {
    private final Map<String, Video> videos;
    private final Map<String, TrendingParameters> categoryParameters;
    private final Map<String, List<Video>> regionalTrending;
    private final Map<VideoCategory, List<Video>> categoryTrending;
    private final TrendingAlgorithm algorithm;
    private final ScheduledExecutorService scheduler;
    private final Object rankingLock = new Object();
    
    // Analytics
    private final Map<String, TrendingAnalytics> analytics;
    
    public TrendingService() {
        this.videos = new ConcurrentHashMap<>();
        this.categoryParameters = new ConcurrentHashMap<>();
        this.regionalTrending = new ConcurrentHashMap<>();
        this.categoryTrending = new ConcurrentHashMap<>();
        this.algorithm = new MultiFacetTrendingAlgorithm();
        this.scheduler = Executors.newScheduledThreadPool(4);
        this.analytics = new ConcurrentHashMap<>();
        
        initializeParameters();
        startPeriodicTasks();
    }
    
    /**
     * Add or update video in the system
     */
    public void addVideo(Video video) {
        videos.put(video.getVideoId(), video);
        
        // Initialize analytics if not exists
        String analyticsKey = video.getRegion() + ":" + video.getCategory();
        analytics.computeIfAbsent(analyticsKey, k -> new TrendingAnalytics());
    }
    
    /**
     * Update video metrics (simulating real-time telemetry)
     */
    public boolean updateVideoMetrics(String videoId, long views, long likes, 
                                    long comments, long shares, long dislikes) {
        Video video = videos.get(videoId);
        if (video == null) return false;
        
        video.updateMetrics(views, likes, comments, shares, dislikes);
        
        // Trigger immediate score recalculation for significant updates
        if (isSignificantUpdate(video, views, likes, comments, shares)) {
            recalculateVideoScore(video);
        }
        
        return true;
    }
    
    /**
     * Get trending videos with filtering and pagination
     */
    public List<Video> getTrendingVideos(String region, VideoCategory category, 
                                       int offset, int limit) {
        List<Video> trendingVideos;
        
        if (region != null && category != null) {
            // Both region and category specified
            trendingVideos = videos.values().stream()
                .filter(v -> v.getRegion().equals(region) && v.getCategory() == category)
                .sorted((v1, v2) -> Double.compare(v2.getTrendingScore(), v1.getTrendingScore()))
                .collect(Collectors.toList());
        } else if (region != null) {
            // Region-specific trending
            trendingVideos = regionalTrending.getOrDefault(region, Collections.emptyList());
        } else if (category != null) {
            // Category-specific trending
            trendingVideos = categoryTrending.getOrDefault(category, Collections.emptyList());
        } else {
            // Global trending
            trendingVideos = videos.values().stream()
                .sorted((v1, v2) -> Double.compare(v2.getTrendingScore(), v1.getTrendingScore()))
                .collect(Collectors.toList());
        }
        
        // Apply pagination
        return trendingVideos.stream()
            .skip(offset)
            .limit(limit)
            .collect(Collectors.toList());
    }
    
    /**
     * Get video by ID with current trending score
     */
    public Video getVideo(String videoId) {
        return videos.get(videoId);
    }
    
    /**
     * Search videos with ranking by trending score
     */
    public List<Video> searchVideos(String query, VideoCategory category, 
                                  String region, int limit) {
        return videos.values().stream()
            .filter(video -> video.matches(query, category, region))
            .sorted((v1, v2) -> Double.compare(v2.getTrendingScore(), v1.getTrendingScore()))
            .limit(limit)
            .collect(Collectors.toList());
    }
    
    /**
     * Get trending analytics for region/category
     */
    public TrendingAnalytics getAnalytics(String region, VideoCategory category) {
        String key = region + ":" + category;
        return analytics.get(key);
    }
    
    /**
     * Update algorithm parameters for specific category
     */
    public void updateAlgorithmParameters(VideoCategory category, TrendingParameters params) {
        categoryParameters.put(category.name(), params);
    }
    
    /**
     * Generate trending report
     */
    public TrendingReport generateReport(String region, VideoCategory category, 
                                       LocalDateTime startTime, LocalDateTime endTime) {
        List<Video> relevantVideos = videos.values().stream()
            .filter(v -> region == null || v.getRegion().equals(region))
            .filter(v -> category == null || v.getCategory() == category)
            .filter(v -> v.getUploadTime().isAfter(startTime) && v.getUploadTime().isBefore(endTime))
            .collect(Collectors.toList());
        
        return new TrendingReport(relevantVideos, startTime, endTime, region, category);
    }
    
    /**
     * Recalculate trending scores for all videos
     */
    public void recalculateAllScores() {
        CompletableFuture.runAsync(() -> {
            for (Video video : videos.values()) {
                recalculateVideoScore(video);
            }
            updateRankings();
        });
    }
    
    /**
     * Get system statistics
     */
    public SystemStats getSystemStats() {
        int totalVideos = videos.size();
        
        Map<VideoCategory, Long> videosByCategory = videos.values().stream()
            .collect(Collectors.groupingBy(Video::getCategory, Collectors.counting()));
        
        Map<String, Long> videosByRegion = videos.values().stream()
            .collect(Collectors.groupingBy(Video::getRegion, Collectors.counting()));
        
        double avgTrendingScore = videos.values().stream()
            .mapToDouble(Video::getTrendingScore)
            .average()
            .orElse(0.0);
        
        return new SystemStats(totalVideos, videosByCategory, videosByRegion, avgTrendingScore);
    }
    
    /**
     * Shutdown the service
     */
    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    // Private helper methods
    
    private void initializeParameters() {
        // Initialize default parameters for each category
        for (VideoCategory category : VideoCategory.values()) {
            categoryParameters.put(category.name(), 
                TrendingParameters.createCategoryParameters(category));
        }
    }
    
    private void startPeriodicTasks() {
        // Recalculate scores every 5 minutes
        scheduler.scheduleWithFixedDelay(this::recalculateAllScores, 0, 5, TimeUnit.MINUTES);
        
        // Update rankings every minute
        scheduler.scheduleWithFixedDelay(this::updateRankings, 0, 1, TimeUnit.MINUTES);
        
        // Update analytics every 10 minutes
        scheduler.scheduleWithFixedDelay(this::updateAnalytics, 0, 10, TimeUnit.MINUTES);
    }
    
    private void recalculateVideoScore(Video video) {
        TrendingParameters params = categoryParameters.getOrDefault(\n            video.getCategory().name(), \n            new TrendingParameters()\n        );\n        \n        double score = algorithm.calculateTrendingScore(video, params);\n        \n        // Calculate percentile rank would require all videos - simplified here\n        int percentile = calculatePercentileRank(video, score);\n        \n        video.updateTrendingScore(score, percentile);\n    }\n    \n    private int calculatePercentileRank(Video video, double score) {\n        // Count videos with lower scores\n        long lowerScoreCount = videos.values().stream()\n            .filter(v -> v.getCategory() == video.getCategory())\n            .filter(v -> v.getRegion().equals(video.getRegion()))\n            .filter(v -> v.getTrendingScore() < score)\n            .count();\n        \n        long totalVideos = videos.values().stream()\n            .filter(v -> v.getCategory() == video.getCategory())\n            .filter(v -> v.getRegion().equals(video.getRegion()))\n            .count();\n        \n        if (totalVideos == 0) return 50;\n        \n        return (int) ((lowerScoreCount * 100) / totalVideos);\n    }\n    \n    private void updateRankings() {\n        synchronized (rankingLock) {\n            // Update regional trending lists\n            Map<String, List<Video>> newRegionalTrending = videos.values().stream()\n                .collect(Collectors.groupingBy(Video::getRegion))\n                .entrySet().stream()\n                .collect(Collectors.toMap(\n                    Map.Entry::getKey,\n                    entry -> entry.getValue().stream()\n                        .sorted((v1, v2) -> Double.compare(v2.getTrendingScore(), v1.getTrendingScore()))\n                        .limit(100)\n                        .collect(Collectors.toList())\n                ));\n            \n            regionalTrending.clear();\n            regionalTrending.putAll(newRegionalTrending);\n            \n            // Update category trending lists\n            Map<VideoCategory, List<Video>> newCategoryTrending = videos.values().stream()\n                .collect(Collectors.groupingBy(Video::getCategory))\n                .entrySet().stream()\n                .collect(Collectors.toMap(\n                    Map.Entry::getKey,\n                    entry -> entry.getValue().stream()\n                        .sorted((v1, v2) -> Double.compare(v2.getTrendingScore(), v1.getTrendingScore()))\n                        .limit(100)\n                        .collect(Collectors.toList())\n                ));\n            \n            categoryTrending.clear();\n            categoryTrending.putAll(newCategoryTrending);\n        }\n    }\n    \n    private void updateAnalytics() {\n        for (TrendingAnalytics analytic : analytics.values()) {\n            analytic.updateMetrics();\n        }\n    }\n    \n    private boolean isSignificantUpdate(Video video, long newViews, long newLikes, \n                                      long newComments, long newShares) {\n        // Consider update significant if any metric increased by more than 10%\n        long currentViews = video.getViewCount();\n        long currentLikes = video.getLikeCount();\n        \n        double viewIncrease = currentViews > 0 ? (double)(newViews - currentViews) / currentViews : 1.0;\n        double likeIncrease = currentLikes > 0 ? (double)(newLikes - currentLikes) / currentLikes : 1.0;\n        \n        return viewIncrease > 0.1 || likeIncrease > 0.1;\n    }\n    \n    // Inner classes for analytics and reporting\n    \n    public static class TrendingAnalytics {\n        private long totalVideos;\n        private double avgTrendingScore;\n        private long totalViews;\n        private LocalDateTime lastUpdate;\n        \n        public TrendingAnalytics() {\n            this.lastUpdate = LocalDateTime.now();\n        }\n        \n        public void updateMetrics() {\n            this.lastUpdate = LocalDateTime.now();\n            // In a real implementation, this would calculate from actual data\n        }\n        \n        // Getters\n        public long getTotalVideos() { return totalVideos; }\n        public double getAvgTrendingScore() { return avgTrendingScore; }\n        public long getTotalViews() { return totalViews; }\n        public LocalDateTime getLastUpdate() { return lastUpdate; }\n    }\n    \n    public static class TrendingReport {\n        private final List<Video> videos;\n        private final LocalDateTime startTime;\n        private final LocalDateTime endTime;\n        private final String region;\n        private final VideoCategory category;\n        private final long totalViews;\n        private final double avgTrendingScore;\n        \n        public TrendingReport(List<Video> videos, LocalDateTime startTime, LocalDateTime endTime,\n                            String region, VideoCategory category) {\n            this.videos = new ArrayList<>(videos);\n            this.startTime = startTime;\n            this.endTime = endTime;\n            this.region = region;\n            this.category = category;\n            this.totalViews = videos.stream().mapToLong(Video::getViewCount).sum();\n            this.avgTrendingScore = videos.stream().mapToDouble(Video::getTrendingScore).average().orElse(0.0);\n        }\n        \n        // Getters\n        public List<Video> getVideos() { return new ArrayList<>(videos); }\n        public LocalDateTime getStartTime() { return startTime; }\n        public LocalDateTime getEndTime() { return endTime; }\n        public String getRegion() { return region; }\n        public VideoCategory getCategory() { return category; }\n        public long getTotalViews() { return totalViews; }\n        public double getAvgTrendingScore() { return avgTrendingScore; }\n        public int getVideoCount() { return videos.size(); }\n    }\n    \n    public static class SystemStats {\n        private final int totalVideos;\n        private final Map<VideoCategory, Long> videosByCategory;\n        private final Map<String, Long> videosByRegion;\n        private final double avgTrendingScore;\n        \n        public SystemStats(int totalVideos, Map<VideoCategory, Long> videosByCategory,\n                         Map<String, Long> videosByRegion, double avgTrendingScore) {\n            this.totalVideos = totalVideos;\n            this.videosByCategory = new HashMap<>(videosByCategory);\n            this.videosByRegion = new HashMap<>(videosByRegion);\n            this.avgTrendingScore = avgTrendingScore;\n        }\n        \n        // Getters\n        public int getTotalVideos() { return totalVideos; }\n        public Map<VideoCategory, Long> getVideosByCategory() { return new HashMap<>(videosByCategory); }\n        public Map<String, Long> getVideosByRegion() { return new HashMap<>(videosByRegion); }\n        public double getAvgTrendingScore() { return avgTrendingScore; }\n    }\n}"