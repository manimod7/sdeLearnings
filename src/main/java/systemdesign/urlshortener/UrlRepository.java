package systemdesign.urlshortener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple in-memory repository for storing URL mappings.
 */
public class UrlRepository {
    private final Map<String, String> store = new ConcurrentHashMap<>();

    public void save(String code, String url) {
        store.put(code, url);
    }

    public String find(String code) {
        return store.get(code);
    }
}
