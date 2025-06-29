package lld.chatapplication;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Queue;
import java.util.Map;

public class QueuedDeliveryStrategy implements MessageDeliveryStrategy {
    private Map<String, Queue<QueuedMessage>> userQueues;
    private boolean available;
    
    public QueuedDeliveryStrategy() {
        this.userQueues = new ConcurrentHashMap<>();
        this.available = true;
    }
    
    @Override
    public void deliverMessage(Message message, User recipient) {
        if (!available) {
            throw new IllegalStateException("Queued delivery is not available");
        }
        
        queueMessage(message, recipient);
        
        // If user comes online, deliver queued messages
        if (recipient.getStatus() == UserStatus.ONLINE) {
            deliverQueuedMessages(recipient.getUserId());
        }
    }
    
    private void queueMessage(Message message, User recipient) {
        String userId = recipient.getUserId();
        
        userQueues.computeIfAbsent(userId, k -> new ConcurrentLinkedQueue<>());
        
        QueuedMessage queuedMessage = new QueuedMessage(message, LocalDateTime.now());
        userQueues.get(userId).offer(queuedMessage);
        
        System.out.println(String.format("[QUEUED] Message %s queued for %s", 
                                        message.getMessageId().substring(0, 8) + "...", 
                                        recipient.getUsername()));
    }
    
    public void deliverQueuedMessages(String userId) {
        Queue<QueuedMessage> queue = userQueues.get(userId);
        if (queue == null || queue.isEmpty()) {
            return;
        }
        
        System.out.println(String.format("[QUEUED] Delivering %d queued messages to %s", 
                                        queue.size(), userId));
        
        while (!queue.isEmpty()) {
            QueuedMessage queuedMessage = queue.poll();
            Message message = queuedMessage.getMessage();
            
            // Simulate delivery
            System.out.println(String.format("  Delivering: %s", 
                                            message.getDisplayContent().substring(0, 
                                            Math.min(50, message.getDisplayContent().length()))));
            
            message.markAsDelivered();
        }
    }
    
    public int getQueueSize(String userId) {
        Queue<QueuedMessage> queue = userQueues.get(userId);
        return queue == null ? 0 : queue.size();
    }
    
    public void clearQueue(String userId) {
        userQueues.remove(userId);
    }
    
    @Override
    public String getDeliveryMethod() {
        return "Queued";
    }
    
    @Override
    public boolean isAvailable() {
        return available;
    }
    
    @Override
    public int getPriority() {
        return 5; // Medium priority for offline users
    }
    
    public void setAvailable(boolean available) {
        this.available = available;
    }
    
    private static class QueuedMessage {
        private final Message message;
        private final LocalDateTime queuedAt;
        
        public QueuedMessage(Message message, LocalDateTime queuedAt) {
            this.message = message;
            this.queuedAt = queuedAt;
        }
        
        public Message getMessage() {
            return message;
        }
        
        public LocalDateTime getQueuedAt() {
            return queuedAt;
        }
    }
}