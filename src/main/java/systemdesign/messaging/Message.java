package systemdesign.messaging;

/**
 * Message sent between users.
 */
public class Message {
    private final User from;
    private final User to;
    private final String text;

    public Message(User from, User to, String text) {
        this.from = from;
        this.to = to;
        this.text = text;
    }

    public User getFrom() { return from; }
    public User getTo() { return to; }
    public String getText() { return text; }
}
