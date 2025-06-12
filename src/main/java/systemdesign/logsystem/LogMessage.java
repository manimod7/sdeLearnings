package systemdesign.logsystem;

/**
 * A log message with level and text.
 */
public class LogMessage {
    private final String level;
    private final String message;

    public LogMessage(String level, String message) {
        this.level = level;
        this.message = message;
    }

    public String getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }
}
