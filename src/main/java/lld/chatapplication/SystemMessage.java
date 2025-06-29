package lld.chatapplication;

public class SystemMessage extends Message {
    
    public SystemMessage(String senderId, String chatRoomId, String content) {
        super(senderId, chatRoomId, content, MessageType.SYSTEM);
    }
    
    @Override
    public String getDisplayContent() {
        return "🤖 " + getContent();
    }
    
    @Override
    public String toString() {
        return String.format("[SYSTEM] %s", super.toString());
    }
}