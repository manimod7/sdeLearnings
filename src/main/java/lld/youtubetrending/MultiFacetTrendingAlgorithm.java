package lld.youtubetrending;

/**
 * Multi-factor trending algorithm implementation
 * Considers views, engagement, freshness, and quality factors
 */
public class MultiFacetTrendingAlgorithm implements TrendingAlgorithm {
    
    @Override
    public double calculateTrendingScore(Video video, TrendingParameters params) {
        // Get normalized component scores
        double viewScore = calculateViewScore(video, params);
        double engagementScore = calculateEngagementScore(video, params);
        double freshnessScore = calculateFreshnessScore(video, params);
        double qualityScore = calculateQualityScore(video, params);
        
        // Apply weighted combination
        double score = 
            viewScore * params.getParameter("viewWeight") +
            engagementScore * (params.getParameter("likeWeight") + 
                             params.getParameter("commentWeight") + 
                             params.getParameter("shareWeight")) +
            freshnessScore * params.getParameter("freshnessWeight") +
            qualityScore * params.getParameter("likeRatioWeight");
        
        // Apply minimum thresholds
        if (video.getViewCount() < params.getParameter("minViews")) {
            score *= 0.5; // Penalty for low view count
        }
        
        if (video.getEngagementRate() < params.getParameter("minEngagementRate")) {
            score *= 0.7; // Penalty for low engagement
        }
        
        // Ensure score is between 0 and 1
        return Math.max(0.0, Math.min(1.0, score));
    }
    
    /**
     * Calculate normalized view score with logarithmic scaling
     */
    private double calculateViewScore(Video video, TrendingParameters params) {
        long views = video.getViewCount();
        if (views <= 0) return 0.0;
        
        // Logarithmic scaling to handle large view differences
        double logViews = Math.log10(views);
        double maxLogViews = 9.0; // 1 billion views
        
        return Math.min(1.0, logViews / maxLogViews);
    }
    
    /**
     * Calculate engagement score based on likes, comments, and shares
     */
    private double calculateEngagementScore(Video video, TrendingParameters params) {
        long views = video.getViewCount();
        if (views <= 0) return 0.0;
        
        // Weighted engagement components
        double likeScore = (double) video.getLikeCount() / views * params.getParameter("likeWeight");
        double commentScore = (double) video.getCommentCount() / views * params.getParameter("commentWeight");
        double shareScore = (double) video.getShareCount() / views * params.getParameter("shareWeight");
        
        // Normalize to typical engagement rates
        double totalEngagement = likeScore + commentScore + shareScore;
        double maxExpectedEngagement = 0.1; // 10% is very high engagement
        
        return Math.min(1.0, totalEngagement / maxExpectedEngagement);
    }
    
    /**
     * Calculate freshness score with time decay
     */
    private double calculateFreshnessScore(Video video, TrendingParameters params) {
        double ageHours = video.getAgeInHours();
        double maxAge = params.getParameter("maxAgeHours");
        double decayRate = params.getParameter("timeDecayRate");
        
        if (ageHours >= maxAge) return 0.0;
        
        // Exponential decay function
        return Math.exp(-decayRate * ageHours / maxAge);
    }
    
    /**
     * Calculate quality score based on like ratio and other factors
     */
    private double calculateQualityScore(Video video, TrendingParameters params) {
        double likeRatio = video.getLikeRatio();
        double viewsPerHour = video.getViewsPerHour();
        
        // Quality components
        double likeRatioScore = likeRatio; // Already 0-1
        
        // Views per hour with logarithmic scaling
        double viewsPerHourScore = 0.0;
        if (viewsPerHour > 0) {
            double logViewsPerHour = Math.log10(viewsPerHour);
            double maxLogViewsPerHour = 6.0; // 1M views per hour
            viewsPerHourScore = Math.min(1.0, Math.max(0.0, logViewsPerHour / maxLogViewsPerHour));
        }
        
        // Combine quality factors
        return (likeRatioScore * 0.6 + viewsPerHourScore * 0.4);
    }
    
    @Override
    public String getAlgorithmName() {
        return "MultiFacetTrending";
    }
    
    @Override
    public String getVersion() {
        return "1.0";
    }
}