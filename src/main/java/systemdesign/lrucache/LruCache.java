package systemdesign.lrucache;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple LRU cache implementation.
 */
public class LruCache {
    private final Map<Integer, Node> map = new HashMap<>();
    private final int capacity;
    private Node head;
    private Node tail;

    public LruCache(int capacity) {
        this.capacity = capacity;
    }

    public int get(int key) {
        Node node = map.get(key);
        if (node == null) return -1;
        moveToHead(node);
        return node.value;
    }

    public void put(int key, int value) {
        Node node = map.get(key);
        if (node != null) {
            node.value = value;
            moveToHead(node);
        } else {
            Node newNode = new Node(key, value);
            map.put(key, newNode);
            addNode(newNode);
            if (map.size() > capacity) {
                map.remove(tail.key);
                removeNode(tail);
            }
        }
    }

    private void addNode(Node node) {
        node.next = head;
        if (head != null) head.prev = node;
        head = node;
        if (tail == null) tail = node;
    }

    private void removeNode(Node node) {
        if (node.prev != null) node.prev.next = node.next;
        else head = node.next;
        if (node.next != null) node.next.prev = node.prev;
        else tail = node.prev;
    }

    private void moveToHead(Node node) {
        removeNode(node);
        addNode(node);
    }
}
