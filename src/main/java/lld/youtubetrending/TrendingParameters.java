package lld.youtubetrending;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration parameters for trending algorithms
 * Allows fine-tuning of algorithm behavior
 */
public class TrendingParameters {
    private final Map<String, Double> parameters;
    
    public TrendingParameters() {
        this.parameters = new HashMap<>();
        setDefaultParameters();
    }
    
    private void setDefaultParameters() {
        // Default weights for multi-factor algorithm
        parameters.put("viewWeight", 0.4);
        parameters.put("likeWeight", 0.2);
        parameters.put("commentWeight", 0.15);
        parameters.put("shareWeight", 0.15);
        parameters.put("freshnessWeight", 0.1);
        
        // Time decay parameters
        parameters.put("timeDecayRate", 0.1);
        parameters.put("maxAgeHours", 168.0); // 7 days
        
        // Engagement thresholds
        parameters.put("minViews", 1000.0);
        parameters.put("minEngagementRate", 0.01);
        
        // Quality factors
        parameters.put("likeRatioWeight", 0.3);
        parameters.put("channelSubscriberBoost", 0.1);
        
        // Regional factors
        parameters.put("regionBoost", 1.2);
        parameters.put("globalReach", 0.8);
    }
    
    public double getParameter(String key) {
        return parameters.getOrDefault(key, 0.0);
    }
    
    public void setParameter(String key, double value) {
        parameters.put(key, value);
    }
    
    public Map<String, Double> getAllParameters() {
        return new HashMap<>(parameters);
    }
    
    /**
     * Create parameters optimized for real-time trending
     */
    public static TrendingParameters createRealtimeParameters() {
        TrendingParameters params = new TrendingParameters();
        params.setParameter("freshnessWeight", 0.3); // Higher freshness weight
        params.setParameter("viewWeight", 0.3);
        params.setParameter("timeDecayRate", 0.2); // Faster decay
        params.setParameter("maxAgeHours", 24.0); // 1 day window
        return params;
    }
    
    /**
     * Create parameters optimized for long-term trending
     */
    public static TrendingParameters createLongTermParameters() {
        TrendingParameters params = new TrendingParameters();
        params.setParameter("viewWeight", 0.5); // Higher view weight
        params.setParameter("freshnessWeight", 0.05); // Lower freshness weight
        params.setParameter("timeDecayRate", 0.05); // Slower decay
        params.setParameter("maxAgeHours", 720.0); // 30 days
        return params;
    }
    
    /**
     * Create parameters optimized for specific categories
     */
    public static TrendingParameters createCategoryParameters(VideoCategory category) {
        TrendingParameters params = new TrendingParameters();
        
        switch (category) {
            case NEWS:
                // News values freshness highly
                params.setParameter("freshnessWeight", 0.4);
                params.setParameter("timeDecayRate", 0.3);
                params.setParameter("maxAgeHours", 12.0);
                break;
            case MUSIC:
                // Music values engagement highly
                params.setParameter("likeWeight", 0.3);
                params.setParameter("shareWeight", 0.2);
                params.setParameter("commentWeight", 0.1);
                break;
            case EDUCATION:
                // Education values quality and longevity
                params.setParameter("likeRatioWeight", 0.4);
                params.setParameter("timeDecayRate", 0.02);
                params.setParameter("maxAgeHours", 2160.0); // 90 days
                break;
            default:
                // Use default parameters
                break;
        }
        
        return params;
    }
}