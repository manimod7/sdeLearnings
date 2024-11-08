package Interview;

import java.util.HashMap;

public class LRUCache {


    private final int capacity;
    private final HashMap<Integer, Node> map; // HashMap to store keys and their corresponding nodes
    private Node head, tail; // Head and tail of the doubly linked list

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();
    }

    // Get the value of a key
    public int get(int key) {
        if (!map.containsKey(key)) {
            return -1; // Key not found
        }

        Node node = map.get(key);
        moveToHead(node); // Move the accessed node to the head (most recently used)
        return node.value;
    }

    // Insert or update a key-value pair
    public void put(int key, int value) {
        if (map.containsKey(key)) {
            // If the key exists, update its value and move it to the head
            Node node = map.get(key);
            node.value = value;
            moveToHead(node);
        } else {
            // Create a new node
            Node newNode = new Node(key, value);

            if (map.size() == capacity) {
                // If the cache is full, remove the least recently used item (tail)
                map.remove(tail.key);
                removeNode(tail);
            }

            // Add the new node to the head of the list
            addToHead(newNode);
            map.put(key, newNode); // Add to the hashmap
        }
    }

    // Helper function to remove a node from the doubly linked list
    private void removeNode(Node node) {
        if (node == head && node == tail) {
            // If it's the only node in the list
            head = null;
            tail = null;
        } else if (node == head) {
            head = head.next;
            if (head != null) {
                head.prev = null;
            }
        } else if (node == tail) {
            tail = tail.prev;
            if (tail != null) {
                tail.next = null;
            }
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
    }

    // Helper function to add a node to the head of the doubly linked list
    private void addToHead(Node node) {
        if (head == null) {
            head = node;
            tail = node;
        } else {
            node.next = head;
            head.prev = node;
            head = node;
        }
    }

    // Move a node to the head (marking it as most recently used)
    private void moveToHead(Node node) {
        if (node == head) {
            return; // Already at the head
        }

        // Remove the node from its current position
        removeNode(node);

        // Add it to the head
        addToHead(node);
    }
}
