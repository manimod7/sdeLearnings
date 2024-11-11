package linkedlist;

public class ReverseLinkedList {

      //Definition for singly-linked list.

    static class ListNode {
        public int val;
        public ListNode next;
        ListNode(int x) { val = x; next = null; }
      }
    public class Solution {
        public ListNode reverseList(ListNode A) {
            ListNode prev = null;
            ListNode current = A;
            ListNode next = null;

            while (current != null) {
                next = current.next; // Store the next node
                current.next = prev; // Reverse the current node's pointer
                prev = current; // Move pointers one position ahead
                current = next;
            }

            return prev; // New head of the reversed list
        }
    }


}
