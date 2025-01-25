package Interview;

class ListNode {
    int val;
    ListNode next;
    ListNode(int val) { this.val = val; }
}

public class LinkedListCycle {
    // Detect cycle, return start node if cycle exists, else null
    public static ListNode detectCycleNode(ListNode head) {
        if (head == null) return null;

        ListNode slow = head, fast = head;
        boolean hasCycle = false;

        // Detect cycle
        while (fast != null && fast.next != null) {
            slow = slow.next;           // 1 step
            fast = fast.next.next;      // 2 steps
            if (slow == fast) {
                hasCycle = true;
                break;
            }
        }

        // If no cycle
        if (!hasCycle) return null;

        // Find start of cycle
        slow = head;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }
        return slow;  // The start node of the cycle
    }

    // Remove cycle
    public static void removeCycle(ListNode head) {
        ListNode cycleNode = detectCycleNode(head);
        if (cycleNode == null) return;  // no cycle

        // Find the node just before cycleNode in the cycle
        ListNode ptr = cycleNode;
        while (ptr.next != cycleNode) {
            ptr = ptr.next;
        }
        // ptr is the "last" node in cycle, break it
        ptr.next = null;
    }

    public static void main(String[] args) {
        // Example usage:
        ListNode head = new ListNode(3);
        head.next = new ListNode(2);
        head.next.next = new ListNode(0);
        head.next.next.next = new ListNode(-4);

        // Make a cycle: tail’s next points to head.next
        head.next.next.next.next = head.next;

        removeCycle(head);  // removes cycle
        // Now the list is 3 -> 2 -> 0 -> -4 -> null
    }
}

