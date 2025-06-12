package systemdesign.messaging;

/**
 * Demo for messaging service.
 */
public class Main {
    public static void main(String[] args) {
        MessagingService service = new MessagingService();
        User alice = new User("Alice");
        User bob = new User("Bob");
        service.send(new Message(alice, bob, "Hello"));
    }
}
