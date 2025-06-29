package lld.chatapplication;

public enum MessageStatus {
    SENT("Sent"),
    DELIVERED("Delivered"),
    READ("Read"),
    FAILED("Failed");
    
    private final String displayName;
    
    MessageStatus(String displayName) {
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