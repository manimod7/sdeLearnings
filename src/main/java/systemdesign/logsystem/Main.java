package systemdesign.logsystem;

/**
 * Demo for LogService.
 */
public class Main {
    public static void main(String[] args) {
        LogRepository repo = new LogRepository();
        LogService service = new LogService(repo);
        service.log("INFO", "service started");
        service.log("ERROR", "something failed");
        System.out.println("Logs size: " + repo.getAll().size());
    }
}
