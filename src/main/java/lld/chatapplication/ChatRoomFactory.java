package lld.chatapplication;

import java.util.UUID;

public class ChatRoomFactory {
    
    public static ChatRoom createChatRoom(String name, RoomType type, String createdBy) {
        String roomId = generateRoomId(type);
        return new ChatRoom(roomId, name, type, createdBy);
    }
    
    public static ChatRoom createPrivateChat(String user1Id, String user2Id) {
        String roomId = generatePrivateRoomId(user1Id, user2Id);
        String roomName = String.format("Private: %s & %s", user1Id, user2Id);
        
        ChatRoom room = new ChatRoom(roomId, roomName, RoomType.PRIVATE, user1Id);
        room.addParticipant(user1Id);
        room.addParticipant(user2Id);
        
        return room;
    }
    
    public static ChatRoom createGroupChat(String name, String createdBy, int maxParticipants) {
        String roomId = generateRoomId(RoomType.GROUP);
        
        ChatRoom room = new ChatRoom(roomId, name, RoomType.GROUP, createdBy);
        room.setMaxParticipants(maxParticipants);
        room.addParticipant(createdBy);
        
        return room;
    }
    
    public static ChatRoom createChannel(String name, String createdBy) {
        String roomId = generateRoomId(RoomType.CHANNEL);
        
        ChatRoom room = new ChatRoom(roomId, name, RoomType.CHANNEL, createdBy);
        room.setMaxParticipants(Integer.MAX_VALUE); // No limit for channels
        room.addParticipant(createdBy);
        
        return room;
    }
    
    public static ChatRoom createBroadcastRoom(String name, String createdBy) {
        String roomId = generateRoomId(RoomType.BROADCAST);
        
        ChatRoom room = new ChatRoom(roomId, name, RoomType.BROADCAST, createdBy);
        room.setMaxParticipants(Integer.MAX_VALUE);
        room.addParticipant(createdBy);
        
        return room;
    }
    
    private static String generateRoomId(RoomType type) {
        String prefix = getTypePrefix(type);
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        return prefix + "_" + uuid.toUpperCase();
    }
    
    private static String generatePrivateRoomId(String user1Id, String user2Id) {
        // Create consistent room ID regardless of user order
        String[] users = {user1Id, user2Id};
        java.util.Arrays.sort(users);
        
        String combined = users[0] + "_" + users[1];
        int hashCode = Math.abs(combined.hashCode());
        
        return "PRIV_" + String.format("%08X", hashCode);
    }
    
    private static String getTypePrefix(RoomType type) {
        switch (type) {
            case PRIVATE:
                return "PRIV";
            case GROUP:
                return "GRP";
            case CHANNEL:
                return "CHN";
            case BROADCAST:
                return "BCT";
            default:
                return "ROOM";
        }
    }
    
    public static boolean isValidRoomName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        
        // Room name validation
        if (name.length() > 100) {
            return false;
        }
        
        // Check for invalid characters
        if (name.matches(".*[<>\"'&].*")) {
            return false;
        }
        
        return true;
    }
    
    public static ChatRoom cloneRoom(ChatRoom original, String newName, String createdBy) {
        if (!isValidRoomName(newName)) {
            throw new IllegalArgumentException("Invalid room name");
        }
        
        ChatRoom clone = createChatRoom(newName, original.getType(), createdBy);
        clone.setMaxParticipants(original.getMaxParticipants());
        
        return clone;
    }
}