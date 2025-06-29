package lld.chatapplication;

import java.util.List;
import java.util.Map;

public class ChatApplicationDemo {
    
    public static void main(String[] args) {
        System.out.println("💬 Chat Application System Demo 💬");
        System.out.println("=" .repeat(60));
        
        // Create chat application
        ChatApplication app = new ChatApplication();
        
        // Add global observers for monitoring
        app.addGlobalObserver(new DemoNotificationObserver());
        app.addGlobalObserver(new WebSocketNotificationObserver("demo-ws-001"));
        
        // Demo scenarios
        demonstrateUserRegistration(app);
        demonstrateChatRoomCreation(app);
        demonstrateMessaging(app);
        demonstrateAdvancedFeatures(app);
        displayFinalStatistics(app);
        
        System.out.println("\\n🎉 Demo completed successfully! 🎉");
    }
    
    private static void demonstrateUserRegistration(ChatApplication app) {
        System.out.println("\\n👤 Registering users...");
        
        User alice = app.registerUser("alice", "Alice Johnson", "alice@example.com");
        alice.setAvatar("https://example.com/avatars/alice.jpg");
        app.authenticateUser("alice");
        
        User bob = app.registerUser("bob", "Bob Smith", "bob@example.com");
        bob.setAvatar("https://example.com/avatars/bob.jpg");
        app.authenticateUser("bob");
        
        User charlie = app.registerUser("charlie", "Charlie Brown", "charlie@example.com");
        charlie.setAvatar("https://example.com/avatars/charlie.jpg");
        app.authenticateUser("charlie");
        
        System.out.println("✅ User registered: " + alice.getDisplayName());
        System.out.println("✅ User registered: " + bob.getDisplayName());
        System.out.println("✅ User registered: " + charlie.getDisplayName());
    }
    
    private static void demonstrateChatRoomCreation(ChatApplication app) {
        System.out.println("\\n🏠 Creating chat rooms...");
        
        // Create private chat
        ChatRoom privateChat = app.createPrivateChat("alice", "bob");
        System.out.println("✅ Private chat created between alice and bob");
        
        // Create group chat
        ChatRoom groupChat = app.createChatRoom("Project Team", RoomType.GROUP, "charlie");
        app.joinChatRoom("charlie", groupChat.getRoomId());
        app.joinChatRoom("alice", groupChat.getRoomId());
        app.joinChatRoom("bob", groupChat.getRoomId());
        System.out.println("✅ Group chat 'Project Team' created with 3 participants");
        
        // Create channel
        ChatRoom channel = app.createChatRoom("General Announcements", RoomType.CHANNEL, "alice");
        app.joinChatRoom("alice", channel.getRoomId());
        app.joinChatRoom("bob", channel.getRoomId());
        app.joinChatRoom("charlie", channel.getRoomId());
        System.out.println("✅ Channel 'General Announcements' created");
    }
    
    private static void demonstrateMessaging(ChatApplication app) {
        System.out.println("\\n📱 Sending messages...");
        
        // Get chat rooms
        List<ChatRoom> aliceRooms = app.getUserChatRooms("alice");
        ChatRoom privateChat = aliceRooms.stream()
                .filter(room -> room.getType() == RoomType.PRIVATE)
                .findFirst()
                .orElse(null);
        
        ChatRoom groupChat = aliceRooms.stream()
                .filter(room -> room.getType() == RoomType.GROUP)
                .findFirst()
                .orElse(null);
        
        if (privateChat != null) {
            // Send text messages
            Message msg1 = app.sendMessage("alice", privateChat.getRoomId(), 
                                          "Hey Bob, how's the project going?", MessageType.TEXT);
            System.out.println("💬 alice → Private Chat: " + msg1.getDisplayContent());
            
            Message msg2 = app.sendMessage("bob", privateChat.getRoomId(), 
                                          "Going well! Just finished the design phase.", MessageType.TEXT);
            System.out.println("💬 bob → Private Chat: " + msg2.getDisplayContent());
        }
        
        if (groupChat != null) {
            // Send group message
            Message msg3 = app.sendMessage("charlie", groupChat.getRoomId(), 
                                          "Hi everyone! Ready for the meeting?", MessageType.TEXT);
            System.out.println("💬 charlie → Project Team: " + msg3.getDisplayContent());
            
            // Send media message
            Message msg4 = app.sendMessage("alice", groupChat.getRoomId(), 
                                          "https://example.com/meeting-notes.pdf", 
                                          MessageType.FILE, "meeting-notes.pdf");
            System.out.println("📎 alice → Project Team: " + msg4.getDisplayContent());
        }
    }
    
    private static void demonstrateAdvancedFeatures(ChatApplication app) {
        System.out.println("\\n🎯 Demonstrating advanced features...");
        
        // Test message search
        System.out.println("\\n🔍 Searching messages...");
        List<Message> searchResults = app.searchMessages("project", "alice");
        System.out.println("Found " + searchResults.size() + " messages containing 'project'");
        
        // Test different delivery strategies
        System.out.println("\\n📦 Testing delivery strategies...");
        
        // Switch to queued delivery
        app.setDeliveryStrategy(new QueuedDeliveryStrategy());
        System.out.println("Switched to queued delivery strategy");
        
        // Switch to batched delivery
        app.setDeliveryStrategy(new BatchedDeliveryStrategy(2, 5));
        System.out.println("Switched to batched delivery strategy");
        
        // Test encryption
        System.out.println("\\n🔒 Testing encryption...");
        app.setEncryptionStrategy(new BasicEncryptionStrategy());
        System.out.println("Enabled basic encryption");
        
        // Test user status changes
        System.out.println("\\n👥 Testing user status changes...");
        User bob = app.findUser("bob");
        if (bob != null) {
            bob.updateStatus(UserStatus.AWAY);
            System.out.println("Bob is now away");
            
            bob.updateStatus(UserStatus.ONLINE);
            System.out.println("Bob is back online");
        }
        
        // Test leaving room
        System.out.println("\\n🚪 Testing room management...");
        List<ChatRoom> bobRooms = app.getUserChatRooms("bob");
        if (!bobRooms.isEmpty()) {
            ChatRoom groupRoom = bobRooms.stream()
                    .filter(room -> room.getType() == RoomType.GROUP)
                    .findFirst()
                    .orElse(null);
            
            if (groupRoom != null) {
                app.leaveChatRoom("bob", groupRoom.getRoomId());
                System.out.println("Bob left the group chat");
                
                app.joinChatRoom("bob", groupRoom.getRoomId());
                System.out.println("Bob rejoined the group chat");
            }
        }
    }
    
    private static void displayFinalStatistics(ChatApplication app) {
        System.out.println("\\n📊 Final Statistics:");
        
        Map<String, Object> stats = app.getStatistics();
        System.out.println("  Total Users: " + stats.get("totalUsers"));
        System.out.println("  Online Users: " + stats.get("onlineUsers"));
        System.out.println("  Total Chat Rooms: " + stats.get("totalChatRooms"));
        System.out.println("  Active Chat Rooms: " + stats.get("activeChatRooms"));
        System.out.println("  Total Messages: " + stats.get("totalMessages"));
        
        System.out.println("\\n  Chat Rooms:");
        for (ChatRoom room : app.getChatRooms().values()) {
            System.out.println("    " + room.toString());
        }
        
        System.out.println("\\n  Online Users:");
        for (User user : app.getOnlineUsers()) {
            System.out.println("    " + user.toString());
        }
    }
    
    static class DemoNotificationObserver implements MessageObserver {
        
        @Override
        public void onMessageReceived(Message message, ChatRoom chatRoom) {
            System.out.println(String.format("  📨 Message delivered in %s", chatRoom.getName()));
        }
        
        @Override
        public void onMessageDelivered(Message message, String userId) {
            System.out.println(String.format("  ✅ Message delivered to %s", userId));
        }
        
        @Override
        public void onMessageRead(Message message, String userId) {
            System.out.println(String.format("  📖 Message read by %s", userId));
        }
        
        @Override
        public void onUserTyping(String userId, String chatRoomId) {
            System.out.println(String.format("  ⌨️ %s is typing...", userId));
        }
        
        @Override
        public void onUserStatusChanged(User user) {
            System.out.println(String.format("  🔄 %s status changed to %s", user.getUsername(), user.getStatus()));
        }
        
        @Override
        public void onParticipantJoined(ChatRoom chatRoom, String userId) {
            System.out.println(String.format("  ➕ %s joined %s", userId, chatRoom.getName()));
        }
        
        @Override
        public void onParticipantLeft(ChatRoom chatRoom, String userId) {
            System.out.println(String.format("  ➖ %s left %s", userId, chatRoom.getName()));
        }
    }
}