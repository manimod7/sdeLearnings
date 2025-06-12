package systemdesign.lrucache;

/**
 * Demo usage of LRU cache.
 */
public class Main {
    public static void main(String[] args) {
        LruCache cache = new LruCache(2);
        cache.put(1, 1);
        cache.put(2, 2);
        cache.get(1);
        cache.put(3, 3);
        System.out.println(cache.get(2)); // should be -1
    }
}
