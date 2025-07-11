package lld.musicstreaming;

import java.util.List;
import java.util.stream.Collectors;

public class GenreBasedRecommendationStrategy implements RecommendationStrategy {
    
    @Override
    public List<Song> recommend(User user, List<Song> allSongs, int limit) {
        return allSongs.stream()
                .filter(song -> user.getFavoriteGenres().contains(song.getGenre()))
                .filter(song -> !user.getLikedSongs().contains(song))
                .filter(song -> !user.getRecentlyPlayed().contains(song))
                .sorted((s1, s2) -> Double.compare(s2.getRating(), s1.getRating()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public String getStrategyName() {
        return "Genre-Based Recommendation";
    }
}