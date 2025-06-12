package systemdesign.logsystem;

import java.util.ArrayList;
import java.util.List;

/**
 * In-memory repository for logs.
 */
public class LogRepository {
    private final List<LogMessage> messages = new ArrayList<>();

    public void add(LogMessage message) {
        messages.add(message);
    }

    public List<LogMessage> getAll() {
        return messages;
    }
}
