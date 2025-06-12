package systemdesign.urlshortener;

import java.util.Random;

/**
 * Service to shorten URLs and retrieve original URLs.
 */
public class UrlShortenerService {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 6;

    private final UrlRepository repository;
    private final Random random = new Random();

    public UrlShortenerService(UrlRepository repository) {
        this.repository = repository;
    }

    public String shorten(String url) {
        String code = generateCode();
        repository.save(code, url);
        return code;
    }

    public String getOriginalUrl(String code) {
        return repository.find(code);
    }

    private String generateCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}
