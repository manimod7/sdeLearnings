package lld.musicstreaming;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ArtistBasedRecommendationStrategy implements RecommendationStrategy {
    
    @Override
    public List<Song> recommend(User user, List<Song> allSongs, int limit) {
        Set<String> topArtists = user.getTopArtists(5);
        
        return allSongs.stream()
                .filter(song -> topArtists.contains(song.getArtist()))
                .filter(song -> !user.getLikedSongs().contains(song))
                .filter(song -> !user.getRecentlyPlayed().contains(song))
                .sorted((s1, s2) -> {
                    // First sort by artist play count, then by song rating
                    int artistComparison = Integer.compare(
                            user.getArtistPlayCount().getOrDefault(s2.getArtist(), 0),
                            user.getArtistPlayCount().getOrDefault(s1.getArtist(), 0)
                    );
                    if (artistComparison != 0) {
                        return artistComparison;
                    }
                    return Double.compare(s2.getRating(), s1.getRating());
                })
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public String getStrategyName() {
        return "Artist-Based Recommendation";
    }
}