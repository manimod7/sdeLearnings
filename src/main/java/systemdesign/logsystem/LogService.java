package systemdesign.logsystem;

/**
 * Service accepting log messages.
 */
public class LogService {
    private final LogRepository repository;

    public LogService(LogRepository repository) {
        this.repository = repository;
    }

    public void log(String level, String message) {
        repository.add(new LogMessage(level, message));
    }
}
