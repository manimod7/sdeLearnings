package lld.chatapplication;

public class MessageFactory {
    
    public static Message createMessage(String senderId, String chatRoomId, 
                                      String content, MessageType type) {
        return createMessage(senderId, chatRoomId, content, type, null);
    }
    
    public static Message createMessage(String senderId, String chatRoomId, 
                                      String content, MessageType type, 
                                      String additionalInfo) {
        switch (type) {
            case TEXT:
                return createTextMessage(senderId, chatRoomId, content);
                
            case MEDIA:
                if (additionalInfo == null) {
                    throw new IllegalArgumentException("Media type is required for media messages");
                }
                return createMediaMessage(senderId, chatRoomId, content, additionalInfo);
                
            case FILE:
                return createFileMessage(senderId, chatRoomId, content, additionalInfo);
                
            case SYSTEM:
                return createSystemMessage(senderId, chatRoomId, content);
                
            default:
                throw new IllegalArgumentException("Unsupported message type: " + type);
        }
    }
    
    public static TextMessage createTextMessage(String senderId, String chatRoomId, String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text message content cannot be empty");
        }
        
        TextMessage message = new TextMessage(senderId, chatRoomId, text);
        if (!message.validateText()) {
            throw new IllegalArgumentException("Invalid text message content");
        }
        
        return message;
    }
    
    public static MediaMessage createMediaMessage(String senderId, String chatRoomId, 
                                                String mediaUrl, String mediaType) {
        if (mediaUrl == null || mediaUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Media URL cannot be empty");
        }
        
        if (mediaType == null || mediaType.trim().isEmpty()) {
            throw new IllegalArgumentException("Media type cannot be empty");
        }
        
        return new MediaMessage(senderId, chatRoomId, mediaUrl, mediaType);
    }
    
    public static FileMessage createFileMessage(String senderId, String chatRoomId, 
                                              String fileName, String fileUrl) {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name cannot be empty");
        }
        
        FileMessage message = new FileMessage(senderId, chatRoomId, fileName, fileUrl);
        
        // Security check
        if (!message.isAllowedFileType()) {
            throw new IllegalArgumentException("File type not allowed: " + message.getFileType());
        }
        
        return message;
    }
    
    public static SystemMessage createSystemMessage(String senderId, String chatRoomId, String content) {
        return new SystemMessage(senderId, chatRoomId, content);
    }
    
    public static Message createWelcomeMessage(String chatRoomId, String username) {
        return createSystemMessage("SYSTEM", chatRoomId, 
                                 String.format("Welcome %s to the chat!", username));
    }
    
    public static Message createUserJoinedMessage(String chatRoomId, String username) {
        return createSystemMessage("SYSTEM", chatRoomId, 
                                 String.format("%s joined the chat", username));
    }
    
    public static Message createUserLeftMessage(String chatRoomId, String username) {
        return createSystemMessage("SYSTEM", chatRoomId, 
                                 String.format("%s left the chat", username));
    }
    
    public static Message createRoomCreatedMessage(String chatRoomId, String roomName, String creatorName) {
        return createSystemMessage("SYSTEM", chatRoomId, 
                                 String.format("Chat room '%s' created by %s", roomName, creatorName));
    }
}