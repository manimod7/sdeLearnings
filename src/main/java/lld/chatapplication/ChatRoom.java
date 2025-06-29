package lld.chatapplication;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatRoom {
    private String roomId;
    private String name;
    private RoomType type;
    private Set<String> participants;
    private List<Message> messages;
    private LocalDateTime createdAt;
    private boolean isActive;
    private String createdBy;
    private int maxParticipants;
    private Set<MessageObserver> observers;
    
    public ChatRoom(String roomId, String name, RoomType type, String createdBy) {
        this.roomId = roomId;
        this.name = name;
        this.type = type;
        this.createdBy = createdBy;
        this.participants = ConcurrentHashMap.newKeySet();
        this.messages = new CopyOnWriteArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
        this.maxParticipants = (type == RoomType.PRIVATE) ? 2 : Integer.MAX_VALUE;
        this.observers = ConcurrentHashMap.newKeySet();
    }
    
    public synchronized boolean addParticipant(String userId) {
        if (participants.size() >= maxParticipants) {
            return false;
        }
        boolean added = participants.add(userId);
        if (added) {
            notifyObservers("PARTICIPANT_JOINED", userId);
        }
        return added;
    }
    
    public synchronized boolean removeParticipant(String userId) {
        boolean removed = participants.remove(userId);
        if (removed) {
            notifyObservers("PARTICIPANT_LEFT", userId);
        }
        return removed;
    }
    
    public synchronized void addMessage(Message message) {
        if (!isActive) {
            throw new IllegalStateException("Cannot add message to inactive chat room");
        }
        
        if (!participants.contains(message.getSenderId())) {
            throw new IllegalArgumentException("Sender is not a participant in this room");
        }
        
        messages.add(message);
        notifyMessageObservers(message);
    }
    
    public List<Message> getMessageHistory(int limit, int offset) {
        int start = Math.max(0, messages.size() - offset - limit);
        int end = Math.max(0, messages.size() - offset);
        
        if (start >= messages.size()) {
            return new ArrayList<>();
        }
        
        return new ArrayList<>(messages.subList(start, end));
    }
    
    public List<Message> getRecentMessages(int count) {
        int size = messages.size();
        int start = Math.max(0, size - count);
        return new ArrayList<>(messages.subList(start, size));
    }
    
    public List<Message> searchMessages(String query) {
        return messages.stream()
                .filter(msg -> msg.getContent().toLowerCase().contains(query.toLowerCase()))\n                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    public void addObserver(MessageObserver observer) {
        observers.add(observer);
    }
    
    public void removeObserver(MessageObserver observer) {
        observers.remove(observer);
    }
    
    private void notifyMessageObservers(Message message) {
        for (MessageObserver observer : observers) {
            observer.onMessageReceived(message, this);
        }
    }
    
    private void notifyObservers(String event, String userId) {
        for (MessageObserver observer : observers) {
            if ("PARTICIPANT_JOINED".equals(event)) {
                observer.onParticipantJoined(this, userId);
            } else if ("PARTICIPANT_LEFT".equals(event)) {
                observer.onParticipantLeft(this, userId);
            }
        }
    }
    
    public boolean canAddParticipant() {
        return participants.size() < maxParticipants;
    }
    
    public void setMaxParticipants(int maxParticipants) {
        if (type == RoomType.PRIVATE && maxParticipants != 2) {
            throw new IllegalArgumentException("Private rooms must have exactly 2 participants");
        }
        this.maxParticipants = maxParticipants;
    }
    
    public void deactivate() {
        this.isActive = false;
    }
    
    public void activate() {
        this.isActive = true;
    }
    
    // Getters
    public String getRoomId() { return roomId; }
    public String getName() { return name; }
    public RoomType getType() { return type; }
    public Set<String> getParticipants() { return new HashSet<>(participants); }
    public int getParticipantCount() { return participants.size(); }
    public List<Message> getMessages() { return new ArrayList<>(messages); }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean isActive() { return isActive; }
    public String getCreatedBy() { return createdBy; }
    public int getMaxParticipants() { return maxParticipants; }
    public int getMessageCount() { return messages.size(); }
    
    @Override
    public String toString() {
        return String.format("%s [%s] - %d participants, %d messages", 
                           name, type.getDisplayName(), participants.size(), messages.size());
    }
}