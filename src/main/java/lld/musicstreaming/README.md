# Music Streaming Service (Spotify-like) - Low Level Design

## 🎯 Problem Statement
Design a music streaming service similar to Spotify that can recommend music to users based on their listening history, preferences, and various recommendation algorithms. The system should track user behavior and provide personalized experiences.

## 📋 Requirements

### Functional Requirements
- 🎵 Play songs and track listening history
- ❤️ Like/dislike songs and maintain user preferences
- 📊 Track user listening analytics (play count, favorite genres, top artists)
- 🎯 Provide personalized music recommendations using multiple algorithms
- 🔍 Search songs by title, artist, album, or genre
- 📈 Generate trending songs and artist rankings
- 👤 Maintain user profiles with listening preferences
- 🎨 Support for different recommendation strategies

### Non-Functional Requirements
- ⚡ Real-time recommendation updates
- 📊 Support millions of songs and users
- 🔄 Pluggable recommendation algorithms
- 💾 Memory-efficient user tracking
- 🎯 High-quality personalized recommendations
- 📈 Analytics for user engagement

## 🏗️ Architecture & Design Patterns

### Primary Design Patterns

#### 1. Strategy Pattern
- **Purpose**: Enable different recommendation algorithms to be used interchangeably
- **Implementation**: `RecommendationStrategy` interface with multiple implementations
- **Benefits**: 
  - Runtime algorithm switching
  - Easy addition of new recommendation algorithms
  - Algorithm-specific optimizations

#### 2. Observer Pattern
- **Purpose**: Notify components when user actions occur (song played, liked, etc.)
- **Benefits**: Loose coupling between user actions and analytics

#### 3. Factory Pattern
- **Purpose**: Create appropriate recommendation strategies based on user preferences
- **Benefits**: Centralized strategy creation logic

## 🎨 UML Class Diagram

```
                    ┌─────────────────────────┐
                    │ MusicStreamingService   │
                    ├─────────────────────────┤
                    │ - songCatalog: Map      │
                    │ - users: Map            │
                    │ - strategy: Rec...      │
                    ├─────────────────────────┤
                    │ + playSong()            │
                    │ + likeSong()            │
                    │ + getRecommendations()  │
                    │ + setStrategy()         │
                    │ + searchSongs()         │
                    │ + getTrendingSongs()    │
                    └─────────────────────────┘
                              │
                    ┌─────────┴─────────┐
                    │                   │
                    ▼                   ▼
        ┌─────────────────────┐ ┌─────────────────────┐
        │        Song         │ │        User         │
        ├─────────────────────┤ ├─────────────────────┤
        │ - id: String        │ │ - id: String        │
        │ - title: String     │ │ - name: String      │
        │ - artist: String    │ │ - email: String     │
        │ - album: String     │ │ - favoriteGenres    │
        │ - duration: Duration│ │ - likedSongs: List  │
        │ - genre: String     │ │ - artistPlayCount   │
        │ - playCount: int    │ │ - recentlyPlayed    │
        │ - rating: double    │ ├─────────────────────┤
        ├─────────────────────┤ │ + addFavoriteGenre()│
        │ + incrementPlay()   │ │ + likeSong()        │
        │ + setRating()       │ │ + playedSong()      │
        └─────────────────────┘ │ + getTopArtists()   │
                                └─────────────────────┘
                                          │
                                          │ uses
                                          ▼
                    ┌─────────────────────────────────┐
                    │   RecommendationStrategy        │
                    ├─────────────────────────────────┤
                    │ + recommend(user, songs, limit) │
                    │ + getStrategyName()             │
                    └─────────────────────────────────┘
                                          △
                                          │
            ┌─────────────────────────────┼─────────────────────────────┐
            │                             │                             │
            ▼                             ▼                             ▼
┌─────────────────────┐ ┌─────────────────────┐ ┌─────────────────────┐
│ GenreBasedRec...    │ │ ArtistBasedRec...   │ │ PopularityBasedRec..│
├─────────────────────┤ ├─────────────────────┤ ├─────────────────────┤
│ + recommend()       │ │ + recommend()       │ │ + recommend()       │
└─────────────────────┘ └─────────────────────┘ └─────────────────────┘
                                          │
                                          ▼
                            ┌─────────────────────┐
                            │ HybridRec...        │
                            ├─────────────────────┤
                            │ - genreWeight       │
                            │ - artistWeight      │
                            │ - popularityWeight  │
                            ├─────────────────────┤
                            │ + recommend()       │
                            │ + calculateScore()  │
                            └─────────────────────┘
```

## 📊 Time & Space Complexity Analysis

| Operation | Time Complexity | Space Complexity | Notes |
|-----------|----------------|------------------|--------|
| Play Song | O(1) | O(1) | HashMap lookup + updates |
| Like Song | O(1) | O(1) | Direct user data update |
| Genre-Based Rec | O(n log n) | O(k) | n=songs, k=recommendations, sorting by rating |
| Artist-Based Rec | O(n log n) | O(k) | Sorting by play count + rating |
| Popularity-Based Rec | O(n log n) | O(k) | Sorting by global popularity |
| Hybrid Rec | O(n log n) | O(k) | Composite scoring + sorting |
| Search Songs | O(n) | O(k) | Linear search, k=matching songs |
| Get Trending | O(n log n) | O(k) | Sort by play count |
| User Profile Update | O(1) | O(1) | Direct data structure updates |

**Overall Space Complexity**: O(U + S + R) where U = users, S = songs, R = user relationships

## 🔄 Recommendation Algorithm Flow

```
User Request for Recommendations
              │
              ▼
    ┌─────────────────┐
    │ Select Strategy │
    │ Based on User   │
    │ Preferences     │
    └─────────────────┘
              │
              ▼
    ┌─────────────────┐
    │ Filter Available│
    │ Songs (exclude  │
    │ played/liked)   │
    └─────────────────┘
              │
              ▼
    ┌─────────────────┐
    │ Apply Algorithm │
    │ Specific Logic  │
    │ (Genre/Artist/  │
    │ Popularity)     │
    └─────────────────┘
              │
              ▼
    ┌─────────────────┐
    │ Score and Sort  │
    │ Candidate Songs │
    └─────────────────┘
              │
              ▼
    ┌─────────────────┐
    │ Return Top N    │
    │ Recommendations │
    └─────────────────┘
```

## 🎵 Recommendation Strategies

### 1. Genre-Based Recommendation
```java
// Algorithm: Filter by user's favorite genres, sort by rating
songs.stream()
    .filter(song -> user.getFavoriteGenres().contains(song.getGenre()))
    .filter(song -> !user.getLikedSongs().contains(song))
    .sorted((s1, s2) -> Double.compare(s2.getRating(), s1.getRating()))
    .limit(limit)
```

### 2. Artist-Based Recommendation
```java
// Algorithm: Recommend songs from user's top artists
Set<String> topArtists = user.getTopArtists(5);
songs.stream()
    .filter(song -> topArtists.contains(song.getArtist()))
    .sorted(byArtistPlayCountThenRating)
    .limit(limit)
```

### 3. Popularity-Based Recommendation
```java
// Algorithm: Global trending songs user hasn't heard
songs.stream()
    .filter(song -> !user.getRecentlyPlayed().contains(song))
    .sorted((s1, s2) -> Integer.compare(s2.getPlayCount(), s1.getPlayCount()))
    .limit(limit)
```

### 4. Hybrid Recommendation
```java
// Algorithm: Weighted combination of multiple factors
double score = genreScore * genreWeight + 
               artistScore * artistWeight + 
               popularityScore * popularityWeight;
```

## ⚖️ Pros and Cons

### Pros
✅ **Flexible Algorithms**: Strategy pattern allows runtime algorithm switching  
✅ **Personalization**: Deep user preference tracking for better recommendations  
✅ **Scalable**: Efficient data structures for large catalogs  
✅ **Extensible**: Easy to add new recommendation strategies  
✅ **Real-time**: Immediate updates to user preferences  
✅ **Analytics**: Comprehensive user behavior tracking  
✅ **Performance**: Optimized data structures for fast operations  

### Cons
❌ **Memory Usage**: Stores detailed user listening history  
❌ **Cold Start**: Limited recommendations for new users  
❌ **Algorithm Bias**: May create filter bubbles  
❌ **Complexity**: Hybrid algorithms require parameter tuning  
❌ **Data Freshness**: Need to balance between personalization and discovery  

## 🚀 Scalability & Extensibility

### Horizontal Scaling
- **Microservices**: Separate user-service, catalog-service, recommendation-service
- **Database Sharding**: Partition users and songs across databases
- **Caching Layer**: Redis for user preferences and popular songs
- **CDN**: Content delivery for song metadata and recommendations
- **Message Queues**: Async processing of user actions

### Vertical Scaling
- **Database Optimization**: Indexes on userId, artistId, genre
- **Memory Management**: LRU cache for frequently accessed data
- **Algorithm Optimization**: Pre-computed recommendations for popular users
- **Batch Processing**: Offline computation of complex recommendations

### Extensibility Features
- **New Algorithms**: Plugin architecture for recommendation strategies
- **Machine Learning**: Integration with ML models for advanced recommendations
- **Social Features**: Friend-based recommendations
- **Context Awareness**: Time, location, mood-based recommendations
- **A/B Testing**: Framework for testing different algorithms
- **Real-time Streaming**: Live recommendation updates

## 🧪 Edge Cases Handled

1. **New Users**: Fallback to popularity-based recommendations
2. **Empty Preferences**: Gradual learning from user interactions
3. **Limited Catalog**: Graceful degradation with fewer recommendations
4. **Duplicate Recommendations**: Filtering of previously recommended songs
5. **Concurrent Access**: Thread-safe user data updates
6. **Invalid Data**: Validation of song ratings and user inputs
7. **Memory Pressure**: LRU eviction of inactive user data
8. **Algorithm Failures**: Fallback to simpler recommendation strategies

## 🏭 Production Considerations

### Performance Optimization
- **Caching**: Cache popular songs and user preferences
- **Pre-computation**: Background calculation of recommendations
- **Database Indexes**: Optimize query performance
- **Connection Pooling**: Efficient database connections
- **Load Balancing**: Distribute recommendation requests

### Monitoring & Analytics
- **User Engagement**: Track recommendation click-through rates
- **Algorithm Performance**: A/B testing for different strategies
- **System Metrics**: Response times, cache hit rates
- **Business Metrics**: User retention, listening time

### Data Management
- **Privacy**: User data protection and GDPR compliance
- **Data Retention**: Policies for user listening history
- **Backup Strategy**: Regular backups of user preferences
- **Data Migration**: Smooth transitions during schema changes

## 🧰 How to Run

### Prerequisites
```bash
Java 11+
Maven 3.6+
```

### Run Demo
```bash
# Compile the project
mvn compile

# Run the demo
java -cp target/classes lld.musicstreaming.MusicStreamingDemo
```

### Sample Output
```
=== Music Streaming Service Demo ===

1. Adding songs to catalog...
2. Creating users...
3. Simulating user interactions...

🎵 Alice Johnson is now playing: Queen - Bohemian Rhapsody (A Night at the Opera) [Rock]
❤️ Alice Johnson liked: Queen - Bohemian Rhapsody (A Night at the Opera) [Rock]
🎵 Alice Johnson is now playing: Led Zeppelin - Stairway to Heaven (Led Zeppelin IV) [Rock]

4. Testing different recommendation strategies...

Strategy: Genre-Based Recommendation
Recommendations for Alice:
  Eagles - Hotel California (Hotel California) [Rock]
  
Strategy: Hybrid Recommendation (Genre: 40.0%, Artist: 40.0%, Popularity: 20.0%)
Recommendations for Alice:
  Eagles - Hotel California (Hotel California) [Rock]
  Ed Sheeran - Shape of You (÷) [Pop]
```

## 🔮 Future Enhancements

### Advanced Features
1. **Collaborative Filtering**: User-user and item-item similarity
2. **Deep Learning**: Neural networks for complex pattern recognition
3. **Real-time Streaming**: Live recommendation updates during listening
4. **Context-Aware**: Time, weather, activity-based recommendations
5. **Social Integration**: Friend recommendations and shared playlists
6. **Mood Analysis**: Emotion-based music suggestions
7. **Cross-Platform**: Sync across devices and platforms

### Business Features
8. **Premium Features**: Enhanced recommendations for paid users
9. **Artist Analytics**: Detailed insights for music creators
10. **Advertising**: Targeted ads based on music preferences
11. **Playlist Generation**: AI-powered playlist creation
12. **Concert Recommendations**: Event suggestions based on music taste

## 📚 Algorithm Comparison

| Algorithm | Precision | Recall | Diversity | Novelty | Scalability |
|-----------|-----------|---------|-----------|---------|-------------|
| Genre-Based | High | Medium | Low | Low | High |
| Artist-Based | High | Medium | Medium | Low | High |
| Popularity-Based | Medium | High | Low | Very Low | Very High |
| Hybrid | High | High | Medium | Medium | Medium |
| Collaborative Filtering | Very High | High | Medium | High | Medium |
| Deep Learning | Very High | Very High | High | High | Low |

## 🔍 Testing Strategy

### Unit Tests
- Individual strategy algorithm testing
- User preference tracking validation
- Song catalog management verification

### Integration Tests
- End-to-end recommendation workflows
- Strategy switching scenarios
- User interaction tracking

### Performance Tests
- Load testing with millions of songs/users
- Recommendation latency benchmarks
- Memory usage optimization validation

### A/B Testing
- Compare different recommendation algorithms
- Measure user engagement metrics
- Optimize algorithm parameters