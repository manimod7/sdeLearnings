package linkedlist;


import java.util.HashSet;
import java.util.Set;

public class RemoveDuplicate {
    public ListNode deleteDuplicates(ListNode A) {
        if(A==null || A.next == null)
            return A;
        Set<Integer> list = new HashSet();
        ListNode current = A;
        while(current.next!=null) {
            int val = current.next.val;
            if(list.contains(val)){
                current.next = current.next.next;
            }
            else{
                list.add(val);
                current = current.next;
            }
        }
        if(A.val == A.next.val)
            return A.next;
        return A;
    }
}
