package lld.chatapplication;

public interface MessageDeliveryStrategy {
    void deliverMessage(Message message, User recipient);
    String getDeliveryMethod();
    boolean isAvailable();
    int getPriority(); // Higher number = higher priority
}