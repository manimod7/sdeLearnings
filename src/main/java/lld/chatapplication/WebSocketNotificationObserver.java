package lld.chatapplication;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketNotificationObserver implements MessageObserver {
    private String connectionId;
    private boolean connected;
    private Map<String, Object> metadata;
    
    public WebSocketNotificationObserver(String connectionId) {
        this.connectionId = connectionId;
        this.connected = true;
        this.metadata = new ConcurrentHashMap<>();
    }
    
    @Override
    public void onMessageReceived(Message message, ChatRoom chatRoom) {
        if (!connected) return;
        
        Map<String, Object> payload = new ConcurrentHashMap<>();
        payload.put("type", "MESSAGE_RECEIVED");
        payload.put("messageId", message.getMessageId());
        payload.put("senderId", message.getSenderId());
        payload.put("chatRoomId", message.getChatRoomId());
        payload.put("content", message.getDisplayContent());
        payload.put("timestamp", message.getTimestamp().toString());
        payload.put("messageType", message.getType().toString());
        
        sendWebSocketMessage(payload);
    }
    
    @Override
    public void onMessageDelivered(Message message, String userId) {
        if (!connected) return;
        
        Map<String, Object> payload = new ConcurrentHashMap<>();
        payload.put("type", "MESSAGE_DELIVERED");
        payload.put("messageId", message.getMessageId());
        payload.put("userId", userId);
        
        sendWebSocketMessage(payload);
    }
    
    @Override
    public void onMessageRead(Message message, String userId) {
        if (!connected) return;
        
        Map<String, Object> payload = new ConcurrentHashMap<>();
        payload.put("type", "MESSAGE_READ");
        payload.put("messageId", message.getMessageId());
        payload.put("userId", userId);
        
        sendWebSocketMessage(payload);
    }
    
    @Override
    public void onUserTyping(String userId, String chatRoomId) {
        if (!connected) return;
        
        Map<String, Object> payload = new ConcurrentHashMap<>();
        payload.put("type", "USER_TYPING");
        payload.put("userId", userId);
        payload.put("chatRoomId", chatRoomId);
        
        sendWebSocketMessage(payload);
    }
    
    @Override
    public void onUserStatusChanged(User user) {
        if (!connected) return;
        
        Map<String, Object> payload = new ConcurrentHashMap<>();
        payload.put("type", "USER_STATUS_CHANGED");
        payload.put("userId", user.getUserId());
        payload.put("username", user.getUsername());
        payload.put("status", user.getStatus().toString());
        payload.put("lastSeen", user.getLastSeen().toString());
        
        sendWebSocketMessage(payload);
    }
    
    @Override
    public void onParticipantJoined(ChatRoom chatRoom, String userId) {
        if (!connected) return;
        
        Map<String, Object> payload = new ConcurrentHashMap<>();
        payload.put("type", "PARTICIPANT_JOINED");
        payload.put("chatRoomId", chatRoom.getRoomId());
        payload.put("chatRoomName", chatRoom.getName());
        payload.put("userId", userId);
        payload.put("participantCount", chatRoom.getParticipantCount());
        
        sendWebSocketMessage(payload);
    }
    
    @Override
    public void onParticipantLeft(ChatRoom chatRoom, String userId) {
        if (!connected) return;
        
        Map<String, Object> payload = new ConcurrentHashMap<>();
        payload.put("type", "PARTICIPANT_LEFT");
        payload.put("chatRoomId", chatRoom.getRoomId());
        payload.put("chatRoomName", chatRoom.getName());
        payload.put("userId", userId);
        payload.put("participantCount", chatRoom.getParticipantCount());
        
        sendWebSocketMessage(payload);
    }
    
    private void sendWebSocketMessage(Map<String, Object> payload) {
        // Simulate WebSocket message sending
        System.out.println(String.format("[WS %s] %s", 
                                        connectionId.substring(0, 8) + "...", 
                                        payload.get("type")));
        
        // In a real implementation, this would serialize the payload to JSON
        // and send it through the WebSocket connection
    }
    
    public void connect() {
        this.connected = true;
        System.out.println(String.format("[WS %s] Connected", connectionId.substring(0, 8) + "..."));
    }
    
    public void disconnect() {
        this.connected = false;
        System.out.println(String.format("[WS %s] Disconnected", connectionId.substring(0, 8) + "..."));
    }
    
    public boolean isConnected() {
        return connected;
    }
    
    public String getConnectionId() {
        return connectionId;
    }
    
    public void setMetadata(String key, Object value) {
        metadata.put(key, value);
    }
    
    public Object getMetadata(String key) {
        return metadata.get(key);
    }
}