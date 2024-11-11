package linkedlist;

public class DeleteLinkedList {
     // Definition for singly-linked list.
    public class ListNode {
        public int val;
        public ListNode next;
        ListNode(int x) { val = x; next = null; }
    }

    public class Solution {
        public ListNode solve(ListNode A, int B) {

            // Case 1: Delete the head node
            if (B == 0) {
                return A.next;  // New head of the list
            }

            // Traverse to the node just before the B-th node
            ListNode current = A;
            int currentPosition = 0;

            while (current != null && currentPosition < B - 1) {
                current = current.next;
                currentPosition++;
            }

            // If the next node is not null, skip the B-th node
            if (current != null && current.next != null) {
                current.next = current.next.next;
            }

            return A;
        }
    }
}
