package lld.chatapplication;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChatApplication {
    private Map<String, User> users;
    private Map<String, ChatRoom> chatRooms;
    private MessageDeliveryStrategy deliveryStrategy;
    private EncryptionStrategy encryptionStrategy;
    private Set<MessageObserver> globalObservers;
    
    public ChatApplication() {
        this.users = new ConcurrentHashMap<>();
        this.chatRooms = new ConcurrentHashMap<>();
        this.deliveryStrategy = new RealtimeDeliveryStrategy();
        this.encryptionStrategy = new NoEncryptionStrategy();
        this.globalObservers = ConcurrentHashMap.newKeySet();
    }
    
    // User management
    public User registerUser(String userId, String username, String email) {
        if (users.containsKey(userId)) {
            throw new IllegalArgumentException("User already exists: " + userId);
        }
        
        User user = new User(userId, username, email);
        users.put(userId, user);
        
        // Add global observers to user
        for (MessageObserver observer : globalObservers) {
            user.addObserver(observer);
        }
        
        return user;
    }
    
    public User authenticateUser(String userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        
        user.updateStatus(UserStatus.ONLINE);
        return user;
    }
    
    public void logoutUser(String userId) {
        User user = users.get(userId);
        if (user != null) {
            user.updateStatus(UserStatus.OFFLINE);
        }
    }
    
    // Chat room management
    public ChatRoom createChatRoom(String name, RoomType type, String createdBy) {
        if (!ChatRoomFactory.isValidRoomName(name)) {
            throw new IllegalArgumentException("Invalid room name: " + name);
        }
        
        if (!users.containsKey(createdBy)) {
            throw new IllegalArgumentException("Creator user not found: " + createdBy);
        }
        
        ChatRoom room = ChatRoomFactory.createChatRoom(name, type, createdBy);
        chatRooms.put(room.getRoomId(), room);
        
        // Add global observers to room
        for (MessageObserver observer : globalObservers) {
            room.addObserver(observer);
        }
        
        return room;
    }
    
    public ChatRoom createPrivateChat(String user1Id, String user2Id) {
        if (!users.containsKey(user1Id) || !users.containsKey(user2Id)) {
            throw new IllegalArgumentException("One or both users not found");
        }
        
        ChatRoom room = ChatRoomFactory.createPrivateChat(user1Id, user2Id);
        chatRooms.put(room.getRoomId(), room);
        
        // Add global observers to room
        for (MessageObserver observer : globalObservers) {
            room.addObserver(observer);
        }
        
        return room;
    }
    
    public boolean joinChatRoom(String userId, String roomId) {
        User user = users.get(userId);
        ChatRoom room = chatRooms.get(roomId);
        
        if (user == null || room == null) {
            return false;
        }
        
        if (room.addParticipant(userId)) {
            user.joinRoom(roomId);
            return true;
        }
        
        return false;
    }
    
    public boolean leaveChatRoom(String userId, String roomId) {
        User user = users.get(userId);
        ChatRoom room = chatRooms.get(roomId);
        
        if (user == null || room == null) {
            return false;
        }
        
        if (room.removeParticipant(userId)) {
            user.leaveRoom(roomId);
            return true;
        }
        
        return false;
    }
    
    // Message operations
    public Message sendMessage(String senderId, String chatRoomId, String content, MessageType type) {
        return sendMessage(senderId, chatRoomId, content, type, null);
    }
    
    public Message sendMessage(String senderId, String chatRoomId, String content, 
                             MessageType type, String additionalInfo) {
        User sender = users.get(senderId);
        ChatRoom room = chatRooms.get(chatRoomId);
        
        if (sender == null) {
            throw new IllegalArgumentException("Sender not found: " + senderId);
        }
        
        if (room == null) {
            throw new IllegalArgumentException("Chat room not found: " + chatRoomId);
        }
        
        if (!room.getParticipants().contains(senderId)) {
            throw new IllegalArgumentException("Sender is not a participant in the room");
        }
        
        // Create message using factory
        Message message = MessageFactory.createMessage(senderId, chatRoomId, content, type, additionalInfo);
        
        // Encrypt if needed
        if (encryptionStrategy != null && !(encryptionStrategy instanceof NoEncryptionStrategy)) {
            String encrypted = encryptionStrategy.encrypt(content, "defaultKey");
            message.encrypt();
        }
        
        // Add message to room
        room.addMessage(message);
        
        // Deliver to participants
        deliverToParticipants(message, room);
        
        return message;
    }
    
    private void deliverToParticipants(Message message, ChatRoom room) {
        for (String participantId : room.getParticipants()) {
            if (!participantId.equals(message.getSenderId())) {
                User recipient = users.get(participantId);
                if (recipient != null) {
                    try {
                        deliveryStrategy.deliverMessage(message, recipient);
                    } catch (Exception e) {
                        System.err.println("Failed to deliver message to " + participantId + ": " + e.getMessage());
                    }
                }
            }
        }
    }
    
    public List<Message> searchMessages(String query, String userId) {
        List<Message> results = new ArrayList<>();
        
        User user = users.get(userId);
        if (user == null) {
            return results;
        }
        
        for (String roomId : user.getJoinedRooms()) {
            ChatRoom room = chatRooms.get(roomId);
            if (room != null) {
                results.addAll(room.searchMessages(query));
            }
        }
        
        return results;
    }
    
    public User findUser(String userId) {
        return users.get(userId);
    }
    
    public ChatRoom findChatRoom(String roomId) {
        return chatRooms.get(roomId);
    }
    
    public List<User> getOnlineUsers() {
        return users.values().stream()
                .filter(user -> user.getStatus() == UserStatus.ONLINE)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    public List<ChatRoom> getUserChatRooms(String userId) {
        User user = users.get(userId);
        if (user == null) {
            return new ArrayList<>();
        }
        
        return user.getJoinedRooms().stream()
                .map(chatRooms::get)
                .filter(Objects::nonNull)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    // Observer management
    public void addGlobalObserver(MessageObserver observer) {
        globalObservers.add(observer);
        
        // Add to all existing users and rooms
        for (User user : users.values()) {
            user.addObserver(observer);
        }
        
        for (ChatRoom room : chatRooms.values()) {
            room.addObserver(observer);
        }
    }
    
    public void removeGlobalObserver(MessageObserver observer) {
        globalObservers.remove(observer);
        
        // Remove from all existing users and rooms
        for (User user : users.values()) {
            user.removeObserver(observer);
        }
        
        for (ChatRoom room : chatRooms.values()) {
            room.removeObserver(observer);
        }
    }
    
    // Strategy setters
    public void setDeliveryStrategy(MessageDeliveryStrategy strategy) {
        this.deliveryStrategy = strategy;
    }
    
    public void setEncryptionStrategy(EncryptionStrategy strategy) {
        this.encryptionStrategy = strategy;
    }
    
    // Statistics
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", users.size());
        stats.put("onlineUsers", getOnlineUsers().size());
        stats.put("totalChatRooms", chatRooms.size());
        stats.put("activeChatRooms", chatRooms.values().stream()
                .filter(ChatRoom::isActive)
                .count());
        
        int totalMessages = chatRooms.values().stream()
                .mapToInt(ChatRoom::getMessageCount)
                .sum();
        stats.put("totalMessages", totalMessages);
        
        return stats;
    }
    
    // Getters
    public Map<String, User> getUsers() {
        return new HashMap<>(users);
    }
    
    public Map<String, ChatRoom> getChatRooms() {
        return new HashMap<>(chatRooms);
    }
    
    public MessageDeliveryStrategy getDeliveryStrategy() {
        return deliveryStrategy;
    }
    
    public EncryptionStrategy getEncryptionStrategy() {
        return encryptionStrategy;
    }
}