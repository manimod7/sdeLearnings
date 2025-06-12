package systemdesign.urlshortener;

/**
 * Entity representing a short URL mapping.
 */
public class ShortUrl {
    private final String shortCode;
    private final String originalUrl;

    public ShortUrl(String shortCode, String originalUrl) {
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
    }

    public String getShortCode() {
        return shortCode;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }
}
