package lld.chatapplication;

public class PushNotificationObserver implements MessageObserver {
    private String deviceToken;
    private boolean enabled;
    
    public PushNotificationObserver(String deviceToken) {
        this.deviceToken = deviceToken;
        this.enabled = true;
    }
    
    @Override
    public void onMessageReceived(Message message, ChatRoom chatRoom) {
        if (!enabled) return;
        
        String notification = String.format("New message in %s: %s", 
                                           chatRoom.getName(), 
                                           truncateContent(message.getDisplayContent()));
        sendPushNotification(notification);
    }
    
    @Override
    public void onMessageDelivered(Message message, String userId) {
        // Usually don't send push for delivery confirmations
    }
    
    @Override
    public void onMessageRead(Message message, String userId) {
        // Usually don't send push for read confirmations
    }
    
    @Override
    public void onUserTyping(String userId, String chatRoomId) {
        // Real-time typing indicators don't need push notifications
    }
    
    @Override
    public void onUserStatusChanged(User user) {
        if (!enabled) return;
        
        if (user.getStatus() == UserStatus.ONLINE) {
            String notification = String.format("%s is now online", user.getUsername());
            sendPushNotification(notification);
        }
    }
    
    @Override
    public void onParticipantJoined(ChatRoom chatRoom, String userId) {
        if (!enabled) return;
        
        String notification = String.format("Someone joined %s", chatRoom.getName());
        sendPushNotification(notification);
    }
    
    @Override
    public void onParticipantLeft(ChatRoom chatRoom, String userId) {
        if (!enabled) return;
        
        String notification = String.format("Someone left %s", chatRoom.getName());
        sendPushNotification(notification);
    }
    
    private void sendPushNotification(String message) {
        // Simulate push notification
        System.out.println(String.format("[PUSH %s] %s", 
                                        deviceToken.substring(0, 8) + "...", 
                                        message));
    }
    
    private String truncateContent(String content) {
        if (content.length() > 50) {
            return content.substring(0, 47) + "...";
        }
        return content;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public String getDeviceToken() {
        return deviceToken;
    }
}