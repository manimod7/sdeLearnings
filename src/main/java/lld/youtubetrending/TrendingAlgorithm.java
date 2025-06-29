package lld.youtubetrending;

import java.util.List;

/**
 * Strategy interface for different trending calculation algorithms
 * Implements Strategy pattern for pluggable trending algorithms
 */
public interface TrendingAlgorithm {
    /**
     * Calculate trending score for a video
     * @param video The video to calculate score for
     * @param algorithmParams Algorithm-specific parameters
     * @return Trending score (typically 0.0 to 1.0)
     */
    double calculateTrendingScore(Video video, TrendingParameters algorithmParams);
    
    /**
     * Get algorithm name for logging and analytics
     * @return Algorithm identifier
     */
    String getAlgorithmName();
    
    /**
     * Get algorithm version for tracking changes
     * @return Version string
     */
    String getVersion();
}