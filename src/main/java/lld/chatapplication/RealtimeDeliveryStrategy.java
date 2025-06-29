package lld.chatapplication;

public class RealtimeDeliveryStrategy implements MessageDeliveryStrategy {
    private boolean available;
    
    public RealtimeDeliveryStrategy() {
        this.available = true;
    }
    
    @Override
    public void deliverMessage(Message message, User recipient) {
        if (!available) {
            throw new IllegalStateException("Realtime delivery is not available");
        }
        
        if (recipient.getStatus() == UserStatus.ONLINE) {
            deliverNow(message, recipient);
            message.markAsDelivered();
        } else {
            // Fall back to queued delivery for offline users
            throw new IllegalStateException("User is offline, cannot deliver in realtime");
        }
    }
    
    private void deliverNow(Message message, User recipient) {
        // Simulate real-time delivery (WebSocket, Server-Sent Events, etc.)
        System.out.println(String.format("[REALTIME] Delivering message %s to %s immediately", 
                                        message.getMessageId().substring(0, 8) + "...", 
                                        recipient.getUsername()));
        
        // In a real system, this would:
        // 1. Send through WebSocket connection
        // 2. Update UI in real-time
        // 3. Show notification
        // 4. Play sound/vibration
    }
    
    @Override
    public String getDeliveryMethod() {
        return "Realtime";
    }
    
    @Override
    public boolean isAvailable() {
        return available;
    }
    
    @Override
    public int getPriority() {
        return 10; // Highest priority for online users
    }
    
    public void setAvailable(boolean available) {
        this.available = available;
    }
}