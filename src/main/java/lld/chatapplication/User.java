package lld.chatapplication;

import java.time.LocalDateTime;
import java.util.*;

public class User {
    private String userId;
    private String username;
    private String email;
    private UserStatus status;
    private LocalDateTime lastSeen;
    private String avatar;
    private Set<String> joinedRooms;
    private Set<MessageObserver> observers;
    
    public User(String userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.status = UserStatus.OFFLINE;
        this.lastSeen = LocalDateTime.now();
        this.joinedRooms = new HashSet<>();
        this.observers = new HashSet<>();
    }
    
    public void updateStatus(UserStatus status) {
        this.status = status;
        if (status == UserStatus.OFFLINE) {
            this.lastSeen = LocalDateTime.now();
        }
        notifyObservers();
    }
    
    public void setLastSeen(LocalDateTime lastSeen) {
        this.lastSeen = lastSeen;
    }
    
    public String getDisplayName() {
        return username + " (" + userId + ")";
    }
    
    public void joinRoom(String roomId) {
        joinedRooms.add(roomId);
    }
    
    public void leaveRoom(String roomId) {
        joinedRooms.remove(roomId);
    }
    
    public void addObserver(MessageObserver observer) {
        observers.add(observer);
    }
    
    public void removeObserver(MessageObserver observer) {
        observers.remove(observer);
    }
    
    private void notifyObservers() {
        for (MessageObserver observer : observers) {
            observer.onUserStatusChanged(this);
        }
    }
    
    // Getters and setters
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public UserStatus getStatus() { return status; }
    public LocalDateTime getLastSeen() { return lastSeen; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public Set<String> getJoinedRooms() { return new HashSet<>(joinedRooms); }
    
    @Override
    public String toString() {
        return getDisplayName() + " [" + status + "]";
    }
}