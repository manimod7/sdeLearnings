package lld.musicstreaming;

import java.util.List;
import java.util.stream.Collectors;

public class PopularityBasedRecommendationStrategy implements RecommendationStrategy {
    
    @Override
    public List<Song> recommend(User user, List<Song> allSongs, int limit) {
        return allSongs.stream()
                .filter(song -> !user.getLikedSongs().contains(song))
                .filter(song -> !user.getRecentlyPlayed().contains(song))
                .sorted((s1, s2) -> {
                    // Sort by play count first, then by rating
                    int playCountComparison = Integer.compare(s2.getPlayCount(), s1.getPlayCount());
                    if (playCountComparison != 0) {
                        return playCountComparison;
                    }
                    return Double.compare(s2.getRating(), s1.getRating());
                })
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public String getStrategyName() {
        return "Popularity-Based Recommendation";
    }
}