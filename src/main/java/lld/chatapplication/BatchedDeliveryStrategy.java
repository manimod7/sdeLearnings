package lld.chatapplication;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BatchedDeliveryStrategy implements MessageDeliveryStrategy {
    private Map<String, List<Message>> userBatches;
    private ScheduledExecutorService scheduler;
    private boolean available;
    private int batchSize;
    private int batchTimeoutSeconds;
    
    public BatchedDeliveryStrategy() {
        this(10, 30); // Default: 10 messages or 30 seconds
    }
    
    public BatchedDeliveryStrategy(int batchSize, int batchTimeoutSeconds) {
        this.userBatches = new ConcurrentHashMap<>();
        this.scheduler = Executors.newScheduledThreadPool(2);
        this.available = true;
        this.batchSize = batchSize;
        this.batchTimeoutSeconds = batchTimeoutSeconds;
        
        // Start periodic batch delivery
        startBatchDeliveryScheduler();
    }
    
    @Override
    public void deliverMessage(Message message, User recipient) {
        if (!available) {
            throw new IllegalStateException("Batched delivery is not available");
        }
        
        batchMessage(message, recipient);
    }
    
    private void batchMessage(Message message, User recipient) {
        String userId = recipient.getUserId();
        
        userBatches.computeIfAbsent(userId, k -> new ArrayList<>());
        
        synchronized (userBatches.get(userId)) {
            List<Message> batch = userBatches.get(userId);
            batch.add(message);
            
            System.out.println(String.format("[BATCHED] Message %s added to batch for %s (batch size: %d)", 
                                            message.getMessageId().substring(0, 8) + "...", 
                                            recipient.getUsername(),
                                            batch.size()));
            
            // Deliver immediately if batch is full
            if (batch.size() >= batchSize) {
                deliverBatch(userId, new ArrayList<>(batch));
                batch.clear();
            }
        }
    }
    
    private void startBatchDeliveryScheduler() {
        scheduler.scheduleAtFixedRate(() -> {
            deliverAllPendingBatches();
        }, batchTimeoutSeconds, batchTimeoutSeconds, TimeUnit.SECONDS);
    }
    
    private void deliverAllPendingBatches() {
        for (Map.Entry<String, List<Message>> entry : userBatches.entrySet()) {
            String userId = entry.getKey();
            List<Message> batch = entry.getValue();
            
            synchronized (batch) {
                if (!batch.isEmpty()) {
                    deliverBatch(userId, new ArrayList<>(batch));
                    batch.clear();
                }
            }
        }
    }
    
    private void deliverBatch(String userId, List<Message> messages) {
        if (messages.isEmpty()) {
            return;
        }
        
        System.out.println(String.format("[BATCHED] Delivering batch of %d messages to %s", 
                                        messages.size(), userId));
        
        // Simulate batch delivery
        for (Message message : messages) {
            System.out.println(String.format("  Batch item: %s", 
                                            message.getDisplayContent().substring(0, 
                                            Math.min(40, message.getDisplayContent().length()))));
            message.markAsDelivered();
        }
        
        // In a real system, this could:
        // 1. Send one email with multiple messages
        // 2. Create a single push notification with message count
        // 3. Optimize database operations
    }
    
    public int getBatchSize(String userId) {
        List<Message> batch = userBatches.get(userId);
        return batch == null ? 0 : batch.size();
    }
    
    public void forceBatchDelivery(String userId) {
        List<Message> batch = userBatches.get(userId);
        if (batch != null) {
            synchronized (batch) {
                if (!batch.isEmpty()) {
                    deliverBatch(userId, new ArrayList<>(batch));
                    batch.clear();
                }
            }
        }
    }
    
    @Override
    public String getDeliveryMethod() {
        return "Batched";
    }
    
    @Override
    public boolean isAvailable() {
        return available;
    }
    
    @Override
    public int getPriority() {
        return 1; // Lowest priority for efficiency
    }
    
    public void setBatchSize(int batchSize) {
        this.batchSize = Math.max(1, batchSize);
    }
    
    public void setBatchTimeoutSeconds(int batchTimeoutSeconds) {
        this.batchTimeoutSeconds = Math.max(1, batchTimeoutSeconds);
    }
    
    public void setAvailable(boolean available) {
        this.available = available;
    }
    
    public void shutdown() {
        // Deliver all pending batches before shutdown
        deliverAllPendingBatches();
        scheduler.shutdown();
    }
}