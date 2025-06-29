package lld.musicstreaming;

import java.util.*;
import java.util.stream.Collectors;

public class HybridRecommendationStrategy implements RecommendationStrategy {
    private final double genreWeight;
    private final double artistWeight;
    private final double popularityWeight;

    public HybridRecommendationStrategy(double genreWeight, double artistWeight, double popularityWeight) {
        double total = genreWeight + artistWeight + popularityWeight;
        this.genreWeight = genreWeight / total;
        this.artistWeight = artistWeight / total;
        this.popularityWeight = popularityWeight / total;
    }

    public HybridRecommendationStrategy() {
        this(0.4, 0.4, 0.2); // Default weights
    }

    @Override
    public List<Song> recommend(User user, List<Song> allSongs, int limit) {
        List<Song> eligibleSongs = allSongs.stream()
                .filter(song -> !user.getLikedSongs().contains(song))
                .filter(song -> !user.getRecentlyPlayed().contains(song))
                .collect(Collectors.toList());

        // Calculate composite scores for each song
        Map<Song, Double> songScores = new HashMap<>();
        for (Song song : eligibleSongs) {
            double score = calculateCompositeScore(song, user, allSongs);
            songScores.put(song, score);
        }

        return songScores.entrySet().stream()
                .sorted(Map.Entry.<Song, Double>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private double calculateCompositeScore(Song song, User user, List<Song> allSongs) {
        double genreScore = calculateGenreScore(song, user);
        double artistScore = calculateArtistScore(song, user);
        double popularityScore = calculatePopularityScore(song, allSongs);

        return genreScore * genreWeight + artistScore * artistWeight + popularityScore * popularityWeight;
    }

    private double calculateGenreScore(Song song, User user) {
        if (user.getFavoriteGenres().contains(song.getGenre())) {
            return song.getRating() / 5.0; // Normalize rating to 0-1
        }
        return 0.0;
    }

    private double calculateArtistScore(Song song, User user) {
        int artistPlayCount = user.getArtistPlayCount().getOrDefault(song.getArtist(), 0);
        if (artistPlayCount > 0) {
            // Normalize artist play count and combine with song rating
            double normalizedPlayCount = Math.min(artistPlayCount / 10.0, 1.0);
            double normalizedRating = song.getRating() / 5.0;
            return (normalizedPlayCount + normalizedRating) / 2.0;
        }
        return 0.0;
    }

    private double calculatePopularityScore(Song song, List<Song> allSongs) {
        int maxPlayCount = allSongs.stream()
                .mapToInt(Song::getPlayCount)
                .max()
                .orElse(1);

        double normalizedPlayCount = (double) song.getPlayCount() / maxPlayCount;
        double normalizedRating = song.getRating() / 5.0;
        
        return (normalizedPlayCount + normalizedRating) / 2.0;
    }

    @Override
    public String getStrategyName() {
        return String.format("Hybrid Recommendation (Genre: %.1f%%, Artist: %.1f%%, Popularity: %.1f%%)",
                genreWeight * 100, artistWeight * 100, popularityWeight * 100);
    }
}