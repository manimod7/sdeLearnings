package linkedlist;

public class RemoveNthNodeFromEnd {
    public ListNode removeNthFromEnd(ListNode A, int B) {
        if(A==null)
            return A;
        ListNode current = A;
        int size =0;
        while(current!=null) {
            size++;
            current=current.next;
        }
        if(B>=size)
            return A.next;
        int counter = 0;
        current = A;
        while (counter<B && current!=null) {
            current = current.next;
            counter++;
        }
        ListNode x = A;
        while (current.next!=null) {
            current = current.next;
            x = x.next;
        }
        if(x.next!=null){
            x.next = x.next.next;
        }
        return A;
    }
}
