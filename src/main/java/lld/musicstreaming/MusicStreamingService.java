package lld.musicstreaming;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MusicStreamingService {
    private Map<String, Song> songCatalog;
    private Map<String, User> users;
    private RecommendationStrategy recommendationStrategy;

    public MusicStreamingService() {
        this.songCatalog = new ConcurrentHashMap<>();
        this.users = new ConcurrentHashMap<>();
        this.recommendationStrategy = new HybridRecommendationStrategy();
    }

    public void setRecommendationStrategy(RecommendationStrategy strategy) {
        this.recommendationStrategy = strategy;
    }

    public void addSong(Song song) {
        songCatalog.put(song.getId(), song);
    }

    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public Song playSong(String userId, String songId) {
        User user = users.get(userId);
        Song song = songCatalog.get(songId);

        if (user != null && song != null) {
            song.incrementPlayCount();
            user.playedSong(song);
            System.out.println(String.format("🎵 %s is now playing: %s", user.getName(), song));
            return song;
        }
        return null;
    }

    public void likeSong(String userId, String songId) {
        User user = users.get(userId);
        Song song = songCatalog.get(songId);

        if (user != null && song != null) {
            user.likeSong(song);
            System.out.println(String.format("❤️ %s liked: %s", user.getName(), song));
        }
    }

    public List<Song> getRecommendations(String userId, int limit) {
        User user = users.get(userId);
        if (user == null) {
            return Collections.emptyList();
        }

        List<Song> allSongs = new ArrayList<>(songCatalog.values());
        return recommendationStrategy.recommend(user, allSongs, limit);
    }

    public List<Song> searchSongs(String query) {
        String lowerQuery = query.toLowerCase();
        return songCatalog.values().stream()
                .filter(song -> song.getTitle().toLowerCase().contains(lowerQuery) ||
                               song.getArtist().toLowerCase().contains(lowerQuery) ||
                               song.getAlbum().toLowerCase().contains(lowerQuery) ||
                               song.getGenre().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    public List<Song> getSongsByGenre(String genre) {
        return songCatalog.values().stream()
                .filter(song -> song.getGenre().equalsIgnoreCase(genre))
                .sorted((s1, s2) -> Double.compare(s2.getRating(), s1.getRating()))
                .collect(Collectors.toList());
    }

    public List<Song> getSongsByArtist(String artist) {
        return songCatalog.values().stream()
                .filter(song -> song.getArtist().equalsIgnoreCase(artist))
                .sorted((s1, s2) -> Integer.compare(s2.getPlayCount(), s1.getPlayCount()))
                .collect(Collectors.toList());
    }

    public List<Song> getTrendingSongs(int limit) {
        return songCatalog.values().stream()
                .sorted((s1, s2) -> Integer.compare(s2.getPlayCount(), s1.getPlayCount()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public void displayUserProfile(String userId) {
        User user = users.get(userId);
        if (user == null) {
            System.out.println("User not found!");
            return;
        }

        System.out.println("\n=== User Profile: " + user.getName() + " ===");
        System.out.println("Email: " + user.getEmail());
        System.out.println("Favorite Genres: " + user.getFavoriteGenres());
        System.out.println("Liked Songs: " + user.getLikedSongs().size());
        System.out.println("Top Artists: " + user.getTopArtists(3));
        
        System.out.println("\nRecently Played:");
        user.getRecentlyPlayed().stream()
                .limit(5)
                .forEach(song -> System.out.println("  " + song));
    }

    public String getCurrentRecommendationStrategy() {
        return recommendationStrategy.getStrategyName();
    }

    // Getters
    public Map<String, Song> getSongCatalog() { return new HashMap<>(songCatalog); }
    public Map<String, User> getUsers() { return new HashMap<>(users); }
}