package systemdesign.urlshortener;

/**
 * Example usage of URL Shortener system.
 */
public class Main {
    public static void main(String[] args) {
        UrlRepository repository = new UrlRepository();
        UrlShortenerService service = new UrlShortenerService(repository);

        String code = service.shorten("https://example.com");
        System.out.println("Short code: " + code);
        System.out.println("Original URL: " + service.getOriginalUrl(code));
    }
}
