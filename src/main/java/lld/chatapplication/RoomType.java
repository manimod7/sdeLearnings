package lld.chatapplication;

public enum RoomType {
    PRIVATE("Private Chat"),
    GROUP("Group Chat"),
    CHANNEL("Channel"),
    BROADCAST("Broadcast");
    
    private final String displayName;
    
    RoomType(String displayName) {
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