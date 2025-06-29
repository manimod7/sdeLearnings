package lld.chatapplication;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class Message {
    protected String messageId;
    protected String senderId;
    protected String chatRoomId;
    protected String content;
    protected MessageType type;
    protected LocalDateTime timestamp;
    protected MessageStatus status;
    protected boolean encrypted;
    
    public Message(String senderId, String chatRoomId, String content, MessageType type) {
        this.messageId = UUID.randomUUID().toString();
        this.senderId = senderId;
        this.chatRoomId = chatRoomId;
        this.content = content;
        this.type = type;
        this.timestamp = LocalDateTime.now();
        this.status = MessageStatus.SENT;
        this.encrypted = false;
    }
    
    public abstract String getDisplayContent();
    
    public void markAsDelivered() {
        if (this.status == MessageStatus.SENT) {
            this.status = MessageStatus.DELIVERED;
        }
    }
    
    public void markAsRead() {
        this.status = MessageStatus.READ;
    }
    
    public void encrypt() {
        if (!encrypted) {
            // Simple encryption simulation
            this.content = "ENCRYPTED:" + content;
            this.encrypted = true;
        }
    }
    
    public void decrypt() {
        if (encrypted && content.startsWith("ENCRYPTED:")) {
            this.content = content.substring(10);
            this.encrypted = false;
        }
    }
    
    public boolean isEncrypted() {
        return encrypted;
    }
    
    // Getters and setters
    public String getMessageId() { return messageId; }
    public String getSenderId() { return senderId; }
    public String getChatRoomId() { return chatRoomId; }
    public String getContent() { return content; }
    public MessageType getType() { return type; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public MessageStatus getStatus() { return status; }
    
    protected void setContent(String content) { this.content = content; }
    
    @Override
    public String toString() {
        return String.format("[%s] %s: %s (%s)", 
                           timestamp.toString().substring(11, 19), 
                           senderId, 
                           getDisplayContent(), 
                           status);
    }
}