package lld.chatapplication;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EmailNotificationObserver implements MessageObserver {
    private String emailAddress;
    private boolean enabled;
    private boolean onlyWhenOffline;
    
    public EmailNotificationObserver(String emailAddress) {
        this.emailAddress = emailAddress;
        this.enabled = true;
        this.onlyWhenOffline = true;
    }
    
    @Override
    public void onMessageReceived(Message message, ChatRoom chatRoom) {
        if (!enabled) return;
        
        String subject = String.format("New message in %s", chatRoom.getName());
        String body = buildMessageEmailBody(message, chatRoom);
        sendEmail(subject, body);
    }
    
    @Override
    public void onMessageDelivered(Message message, String userId) {
        // Usually don't send emails for delivery confirmations
    }
    
    @Override
    public void onMessageRead(Message message, String userId) {
        // Usually don't send emails for read confirmations
    }
    
    @Override
    public void onUserTyping(String userId, String chatRoomId) {
        // Don't send emails for typing indicators
    }
    
    @Override
    public void onUserStatusChanged(User user) {
        // Usually don't send emails for status changes
    }
    
    @Override
    public void onParticipantJoined(ChatRoom chatRoom, String userId) {
        if (!enabled) return;
        
        String subject = String.format("New participant in %s", chatRoom.getName());
        String body = String.format("User %s has joined the chat room %s at %s.",
                                   userId, 
                                   chatRoom.getName(),
                                   LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        sendEmail(subject, body);
    }
    
    @Override
    public void onParticipantLeft(ChatRoom chatRoom, String userId) {
        if (!enabled) return;
        
        String subject = String.format("Participant left %s", chatRoom.getName());
        String body = String.format("User %s has left the chat room %s at %s.",
                                   userId, 
                                   chatRoom.getName(),
                                   LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        sendEmail(subject, body);
    }
    
    private String buildMessageEmailBody(Message message, ChatRoom chatRoom) {
        StringBuilder body = new StringBuilder();
        body.append("You have received a new message:\\n\\n");
        body.append("Chat Room: ").append(chatRoom.getName()).append("\\n");
        body.append("From: ").append(message.getSenderId()).append("\\n");
        body.append("Time: ").append(message.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).append("\\n");
        body.append("Message: ").append(message.getDisplayContent()).append("\\n\\n");
        body.append("Reply by logging into the chat application.");
        
        return body.toString();
    }
    
    private void sendEmail(String subject, String body) {
        // Simulate email sending
        System.out.println(String.format("[EMAIL %s] %s", emailAddress, subject));
        System.out.println("  " + body.substring(0, Math.min(100, body.length())) + "...");
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setOnlyWhenOffline(boolean onlyWhenOffline) {
        this.onlyWhenOffline = onlyWhenOffline;
    }
    
    public boolean isOnlyWhenOffline() {
        return onlyWhenOffline;
    }
    
    public String getEmailAddress() {
        return emailAddress;
    }
}