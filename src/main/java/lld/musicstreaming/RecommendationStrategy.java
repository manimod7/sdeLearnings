package lld.musicstreaming;

import java.util.List;

public interface RecommendationStrategy {
    List<Song> recommend(User user, List<Song> allSongs, int limit);
    String getStrategyName();
}