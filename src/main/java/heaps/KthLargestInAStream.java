package heaps;

import java.util.PriorityQueue;

public class KthLargestInAStream {
    int k=0;
    PriorityQueue<Integer> pq = new PriorityQueue<>();
    public KthLargestInAStream(int k, int[] nums) {
        this.k=k;
        for(int num: nums){
            add(num);
        }
    }

    public int add(int val) {
        pq.offer(val);
        if(pq.size()>k)
            pq.poll();
        return pq.peek();
    }
}

/*
 * Your KthLargest object will be instantiated and called as such:
 * KthLargest obj = new KthLargest(k, nums);
 * int param_1 = obj.add(val);
 */
