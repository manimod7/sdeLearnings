package systemdesign.messaging;

import java.util.ArrayList;
import java.util.List;

/**
 * Service sending and storing messages.
 */
public class MessagingService {
    private final List<Message> store = new ArrayList<>();

    public void send(Message message) {
        store.add(message);
        System.out.println(message.getFrom().getName() + " -> " +
                message.getTo().getName() + ": " + message.getText());
    }

    public List<Message> getMessages() {
        return store;
    }
}
