package lld.musicstreaming;

import java.time.Duration;
import java.util.List;

public class MusicStreamingDemo {
    public static void main(String[] args) {
        System.out.println("=== Music Streaming Service Demo ===\n");

        // Initialize the music streaming service
        MusicStreamingService musicService = new MusicStreamingService();

        // Create and add songs
        System.out.println("1. Adding songs to catalog...\n");
        setupSongCatalog(musicService);

        // Create and add users
        System.out.println("2. Creating users...\n");
        setupUsers(musicService);

        // Simulate user interactions
        System.out.println("3. Simulating user interactions...\n");
        simulateUserActivity(musicService);

        // Test different recommendation strategies
        System.out.println("\n4. Testing different recommendation strategies...\n");
        testRecommendationStrategies(musicService);

        // Display user profiles and analytics
        System.out.println("\n5. User profiles and analytics...\n");
        displayAnalytics(musicService);

        System.out.println("\n=== Demo completed successfully! ===");
    }

    private static void setupSongCatalog(MusicStreamingService service) {
        // Rock songs
        service.addSong(new Song("1", "Bohemian Rhapsody", "Queen", "A Night at the Opera", Duration.ofMinutes(6), "Rock"));
        service.addSong(new Song("2", "Stairway to Heaven", "Led Zeppelin", "Led Zeppelin IV", Duration.ofMinutes(8), "Rock"));
        service.addSong(new Song("3", "Hotel California", "Eagles", "Hotel California", Duration.ofMinutes(6), "Rock"));

        // Pop songs
        service.addSong(new Song("4", "Shape of You", "Ed Sheeran", "÷", Duration.ofMinutes(4), "Pop"));
        service.addSong(new Song("5", "Blinding Lights", "The Weeknd", "After Hours", Duration.ofMinutes(3), "Pop"));
        service.addSong(new Song("6", "Watermelon Sugar", "Harry Styles", "Fine Line", Duration.ofMinutes(3), "Pop"));

        // Jazz songs
        service.addSong(new Song("7", "Take Five", "Dave Brubeck Quartet", "Time Out", Duration.ofMinutes(5), "Jazz"));
        service.addSong(new Song("8", "Fly Me to the Moon", "Frank Sinatra", "It Might as Well Be Swing", Duration.ofMinutes(2), "Jazz"));
        service.addSong(new Song("9", "Blue Train", "John Coltrane", "Blue Train", Duration.ofMinutes(10), "Jazz"));

        // Electronic songs
        service.addSong(new Song("10", "Strobe", "Deadmau5", "For Lack of a Better Name", Duration.ofMinutes(10), "Electronic"));
        service.addSong(new Song("11", "One More Time", "Daft Punk", "Discovery", Duration.ofMinutes(5), "Electronic"));

        // Set ratings and play counts
        setInitialRatingsAndPlayCounts(service);
    }

    private static void setInitialRatingsAndPlayCounts(MusicStreamingService service) {
        service.getSongCatalog().get("1").setRating(4.8);
        service.getSongCatalog().get("2").setRating(4.9);
        service.getSongCatalog().get("3").setRating(4.7);
        service.getSongCatalog().get("4").setRating(4.2);
        service.getSongCatalog().get("5").setRating(4.5);
        service.getSongCatalog().get("6").setRating(4.1);
        service.getSongCatalog().get("7").setRating(4.6);
        service.getSongCatalog().get("8").setRating(4.4);
        service.getSongCatalog().get("9").setRating(4.7);
        service.getSongCatalog().get("10").setRating(4.3);
        service.getSongCatalog().get("11").setRating(4.4);

        // Simulate some initial play counts
        for (int i = 0; i < 100; i++) service.getSongCatalog().get("5").incrementPlayCount(); // Popular song
        for (int i = 0; i < 80; i++) service.getSongCatalog().get("4").incrementPlayCount();
        for (int i = 0; i < 75; i++) service.getSongCatalog().get("1").incrementPlayCount();
        for (int i = 0; i < 60; i++) service.getSongCatalog().get("2").incrementPlayCount();
        for (int i = 0; i < 45; i++) service.getSongCatalog().get("11").incrementPlayCount();
    }

    private static void setupUsers(MusicStreamingService service) {
        // Create users
        User alice = new User("alice", "Alice Johnson", "alice@email.com");
        alice.addFavoriteGenre("Rock");
        alice.addFavoriteGenre("Pop");

        User bob = new User("bob", "Bob Smith", "bob@email.com");
        bob.addFavoriteGenre("Jazz");
        bob.addFavoriteGenre("Electronic");

        User charlie = new User("charlie", "Charlie Brown", "charlie@email.com");
        charlie.addFavoriteGenre("Pop");
        charlie.addFavoriteGenre("Electronic");

        service.addUser(alice);
        service.addUser(bob);
        service.addUser(charlie);
    }

    private static void simulateUserActivity(MusicStreamingService service) {
        // Alice's activity (Rock and Pop fan)
        service.playSong("alice", "1"); // Bohemian Rhapsody
        service.likeSong("alice", "1");
        service.playSong("alice", "2"); // Stairway to Heaven
        service.playSong("alice", "4"); // Shape of You
        service.likeSong("alice", "4");
        service.playSong("alice", "5"); // Blinding Lights

        // Bob's activity (Jazz and Electronic fan)
        service.playSong("bob", "7"); // Take Five
        service.likeSong("bob", "7");
        service.playSong("bob", "8"); // Fly Me to the Moon
        service.playSong("bob", "10"); // Strobe
        service.likeSong("bob", "10");
        service.playSong("bob", "11"); // One More Time

        // Charlie's activity (Pop and Electronic fan)
        service.playSong("charlie", "4"); // Shape of You
        service.playSong("charlie", "5"); // Blinding Lights
        service.likeSong("charlie", "5");
        service.playSong("charlie", "11"); // One More Time
        service.playSong("charlie", "6"); // Watermelon Sugar
    }

    private static void testRecommendationStrategies(MusicStreamingService service) {
        String userId = "alice";

        // Test Genre-based recommendations
        service.setRecommendationStrategy(new GenreBasedRecommendationStrategy());
        System.out.println("Strategy: " + service.getCurrentRecommendationStrategy());
        List<Song> recommendations = service.getRecommendations(userId, 3);
        System.out.println("Recommendations for Alice:");
        recommendations.forEach(song -> System.out.println("  " + song));

        System.out.println();

        // Test Artist-based recommendations
        service.setRecommendationStrategy(new ArtistBasedRecommendationStrategy());
        System.out.println("Strategy: " + service.getCurrentRecommendationStrategy());
        recommendations = service.getRecommendations(userId, 3);
        System.out.println("Recommendations for Alice:");
        recommendations.forEach(song -> System.out.println("  " + song));

        System.out.println();

        // Test Popularity-based recommendations
        service.setRecommendationStrategy(new PopularityBasedRecommendationStrategy());
        System.out.println("Strategy: " + service.getCurrentRecommendationStrategy());
        recommendations = service.getRecommendations(userId, 3);
        System.out.println("Recommendations for Alice:");
        recommendations.forEach(song -> System.out.println("  " + song));

        System.out.println();

        // Test Hybrid recommendations
        service.setRecommendationStrategy(new HybridRecommendationStrategy());
        System.out.println("Strategy: " + service.getCurrentRecommendationStrategy());
        recommendations = service.getRecommendations(userId, 3);
        System.out.println("Recommendations for Alice:");
        recommendations.forEach(song -> System.out.println("  " + song));
    }

    private static void displayAnalytics(MusicStreamingService service) {
        // Display user profiles
        service.displayUserProfile("alice");
        service.displayUserProfile("bob");

        // Display trending songs
        System.out.println("\n=== Trending Songs ===");
        service.getTrendingSongs(5).forEach(song -> 
            System.out.println(String.format("%s (Plays: %d, Rating: %.1f)", 
                song, song.getPlayCount(), song.getRating())));

        // Display songs by genre
        System.out.println("\n=== Rock Songs ===");
        service.getSongsByGenre("Rock").forEach(song -> 
            System.out.println(String.format("%s (Rating: %.1f)", song, song.getRating())));
    }
}