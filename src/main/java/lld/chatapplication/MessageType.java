package lld.chatapplication;

public enum MessageType {
    TEXT("Text Message"),
    MEDIA("Media Message"),
    FILE("File Message"),
    SYSTEM("System Message");
    
    private final String displayName;
    
    MessageType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}