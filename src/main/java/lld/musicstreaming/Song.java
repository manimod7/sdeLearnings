package lld.musicstreaming;

import java.time.Duration;

public class Song {
    private String id;
    private String title;
    private String artist;
    private String album;
    private Duration duration;
    private String genre;
    private int playCount;
    private double rating;

    public Song(String id, String title, String artist, String album, Duration duration, String genre) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.genre = genre;
        this.playCount = 0;
        this.rating = 0.0;
    }

    public void incrementPlayCount() {
        this.playCount++;
    }

    public void setRating(double rating) {
        this.rating = Math.max(0.0, Math.min(5.0, rating));
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getAlbum() { return album; }
    public Duration getDuration() { return duration; }
    public String getGenre() { return genre; }
    public int getPlayCount() { return playCount; }
    public double getRating() { return rating; }

    @Override
    public String toString() {
        return String.format("%s - %s (%s) [%s]", artist, title, album, genre);
    }
}