package systemdesign.lrucache;

/**
 * Doubly linked list node.
 */
class Node {
    int key;
    int value;
    Node prev;
    Node next;
    Node(int key, int value) {
        this.key = key;
        this.value = value;
    }
}
