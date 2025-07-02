package lld.musicstreaming;

import java.util.*;

public class User {
    private String id;
    private String name;
    private String email;
    private Set<String> favoriteGenres;
    private List<Song> likedSongs;
    private Map<String, Integer> artistPlayCount;
    private List<Song> recentlyPlayed;
    private static final int MAX_RECENT_SONGS = 50;

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.favoriteGenres = new HashSet<>();
        this.likedSongs = new ArrayList<>();
        this.artistPlayCount = new HashMap<>();
        this.recentlyPlayed = new ArrayList<>();
    }

    public void addFavoriteGenre(String genre) {
        favoriteGenres.add(genre);
    }

    public void likeSong(Song song) {
        if (!likedSongs.contains(song)) {
            likedSongs.add(song);
        }
    }

    public void playedSong(Song song) {
        // Update artist play count
        artistPlayCount.merge(song.getArtist(), 1, Integer::sum);
        
        // Add to recently played (remove if already exists to avoid duplicates)
        recentlyPlayed.remove(song);
        recentlyPlayed.add(0, song);
        
        // Keep only the most recent songs
        if (recentlyPlayed.size() > MAX_RECENT_SONGS) {
            recentlyPlayed.remove(recentlyPlayed.size() - 1);
        }
    }

    public Set<String> getTopArtists(int limit) {
        return artistPlayCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(LinkedHashSet::new, LinkedHashSet::add, LinkedHashSet::addAll);
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public Set<String> getFavoriteGenres() { return new HashSet<>(favoriteGenres); }
    public List<Song> getLikedSongs() { return new ArrayList<>(likedSongs); }
    public Map<String, Integer> getArtistPlayCount() { return new HashMap<>(artistPlayCount); }
    public List<Song> getRecentlyPlayed() { return new ArrayList<>(recentlyPlayed); }

    @Override
    public String toString() {
        return String.format("User{id='%s', name='%s', email='%s'}", id, name, email);
    }
}