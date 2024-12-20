package linkedlist;

public class ReversedLinkedList2 {
    public ListNode reverseBetween(ListNode A, int B, int C) {
        if(A==null || B==C)
            return A;

        // Create a dummy node to handle edge cases where B = 1
        ListNode dummy = new ListNode(0);
        dummy.next = A;
        ListNode prev = dummy;

        for(int i=1;i<B;i++) {
            if(prev==null)
                return null;
            prev = prev.next;
        }

        // `current` will point to the first node of the sublist to be reversed
        ListNode current = prev.next;
        ListNode next = null;
        ListNode prevReversed = null;

        // Reverse the sublist from B to C
        for (int i = 0; i <= C - B; i++) {
            if (current == null) break; // C is larger than the length of the list
            next = current.next;
            current.next = prevReversed;
            prevReversed = current;
            current = next;
        }

        //Now time to attach, the first node at B is now at C and should be attached to C+1
        // Reconnect the reversed sublist back to the original list
        // `prev.next` was the start of the sublist, which is now the end after reversal
        // It should point to `current`, the node after position C
        prev.next.next = current;

        // `prev.next` should point to the head of the reversed sublist (`prevReversed`)
        prev.next = prevReversed;

        return dummy.next;
    }
}
