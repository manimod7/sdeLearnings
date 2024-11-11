package linkedlist;

public class DeepCopy {

      //Definition for singly-linked list with a random pointer.
    class RandomListNode {
        int label;
        RandomListNode next, random;
        RandomListNode(int x) { this.label = x; }
    };


    public class Solution {
        public RandomListNode copyRandomList(RandomListNode head) {
            if (head == null) {
                return null;
            }

            // Step 1: Clone each node and insert it right after the original node
            RandomListNode current = head;
            while (current != null) {
                RandomListNode clonedNode = new RandomListNode(current.label);
                clonedNode.next = current.next;
                current.next = clonedNode;
                current = clonedNode.next;
            }

            // Step 2: Set the random pointers for the cloned nodes
            current = head;
            while (current != null) {
                if (current.random != null) {
                    current.next.random = current.random.next;
                }
                current = current.next.next; // Move to the next original node
            }

            // Step 3: Separate the cloned list from the original list
            current = head;
            RandomListNode newHead = head.next;
            RandomListNode copy = newHead;

            while (current != null) {
                current.next = current.next.next; // Restore the original list's next pointers
                if (copy.next != null) {
                    copy.next = copy.next.next; // Set the next pointer for the copied list
                }
                current = current.next;
                copy = copy.next;
            }

            return newHead;
        }
    }

}
