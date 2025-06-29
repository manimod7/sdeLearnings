package lld.chatapplication;

public interface MessageObserver {
    void onMessageReceived(Message message, ChatRoom chatRoom);
    void onMessageDelivered(Message message, String userId);
    void onMessageRead(Message message, String userId);
    void onUserTyping(String userId, String chatRoomId);
    void onUserStatusChanged(User user);
    void onParticipantJoined(ChatRoom chatRoom, String userId);
    void onParticipantLeft(ChatRoom chatRoom, String userId);
}