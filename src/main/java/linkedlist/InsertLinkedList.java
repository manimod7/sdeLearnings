package linkedlist;

import linkedlist.ReverseLinkedList.ListNode;

public class InsertLinkedList {
    public ListNode solve(ListNode A, int B, int C) {
        // Create the new node with value B
        ListNode newNode = new ListNode(B);

        // Case 1: Insert at the head (position C == 0) or if the list is empty (A == null)
        if (C == 0 || A == null) {
            newNode.next = A;
            return newNode;  // new head of the list
        }

        // Traverse to find the (C-1)-th node (node before the position C)
        ListNode current = A;
        int currentPosition = 0;

        while (current.next != null && currentPosition < C - 1) {
            current = current.next;
            currentPosition++;
        }

        // Insert the new node
        newNode.next = current.next;
        current.next = newNode;

        return A;  // Return the head of the modified list
    }
}
