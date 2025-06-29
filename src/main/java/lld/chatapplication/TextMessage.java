package lld.chatapplication;

public class TextMessage extends Message {
    private String text;
    private boolean hasFormatting;
    
    public TextMessage(String senderId, String chatRoomId, String text) {
        super(senderId, chatRoomId, text, MessageType.TEXT);
        this.text = text;
        this.hasFormatting = false;
    }
    
    @Override
    public String getDisplayContent() {
        return encrypted ? "[Encrypted Message]" : text;
    }
    
    public boolean validateText() {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        
        // Basic validation - no excessive length, basic sanitization
        if (text.length() > 4000) {
            return false;
        }
        
        // Check for malicious content patterns
        String lowerText = text.toLowerCase();
        if (lowerText.contains("<script>") || lowerText.contains("javascript:")) {
            return false;
        }
        
        return true;
    }
    
    public void applyFormatting(String formattedText) {
        if (validateText()) {
            this.text = formattedText;
            this.hasFormatting = true;
            setContent(formattedText);
        }
    }
    
    public String getText() {
        return text;
    }
    
    public boolean hasFormatting() {
        return hasFormatting;
    }
    
    @Override
    public String toString() {
        return String.format("[TEXT] %s", super.toString());
    }
}