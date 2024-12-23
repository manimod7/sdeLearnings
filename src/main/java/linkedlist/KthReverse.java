package linkedlist;

public class KthReverse {
    public ListNode reverseList(ListNode head, int k) {
        if (head == null || k == 1) {
            return head;
        }

        // Dummy node to simplify edge cases
        ListNode dummy = new ListNode(0);
        dummy.next = head;

        ListNode prevGroupEnd = dummy;
        ListNode current = head;

        while (true) {
            // Check if there are at least k nodes remaining
            ListNode check = current;
            for (int i = 0; i < k; i++) {
                if (check == null) {
                    return dummy.next; // Fewer than k nodes left, no more reversals
                }
                check = check.next;
            }

            // Reverse k nodes
            ListNode prev = null;
            ListNode tail = current; // tail will end up being the end of the reversed group
            for (int i = 0; i < k; i++) {
                ListNode nextNode = current.next;
                current.next = prev;
                prev = current;
                current = nextNode;
            }

            // Connect previous group with the newly reversed group
            prevGroupEnd.next = prev;
            tail.next = current;
            prevGroupEnd = tail;
        }
    }
}
