package Sorting;

public class SortLinkedList {

    public class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    class Solution {
        public ListNode sortList(ListNode head) {
            // Base case: if head is null or there is only one element
            if (head == null || head.next == null) {
                return head;
            }

            // Step 1: Split the list into two halves
            ListNode mid = getMiddle(head);
            ListNode left = head;
            ListNode right = mid.next;
            mid.next = null;  // Split the list

            // Step 2: Recursively sort both halves
            left = sortList(left);
            right = sortList(right);

            // Step 3: Merge the two sorted halves
            return merge(left, right);
        }

        // Helper function to find the middle of the linked list
        private ListNode getMiddle(ListNode head) {
            ListNode slow = head;
            ListNode fast = head;

            // Move fast two steps and slow one step to find the middle
            while (fast.next != null && fast.next.next != null) {
                slow = slow.next;
                fast = fast.next.next;
            }

            return slow;
        }

        // Helper function to merge two sorted lists
        private ListNode merge(ListNode left, ListNode right) {
            ListNode dummy = new ListNode(0); // Dummy node to simplify the merge
            ListNode current = dummy;

            // Merge the two lists
            while (left != null && right != null) {
                if (left.val < right.val) {
                    current.next = left;
                    left = left.next;
                } else {
                    current.next = right;
                    right = right.next;
                }
                current = current.next;
            }

            // Attach remaining elements, if any
            if (left != null) {
                current.next = left;
            } else {
                current.next = right;
            }

            return dummy.next;
        }
    }
}
