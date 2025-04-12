package heaps;

import java.util.PriorityQueue;

public class KthLargestElement {
    public int findKthLargest(int[] numbs, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int numb : numbs) {
            pq.offer(numb);
            if (pq.size() > k)
                pq.poll();
        }
        return pq.poll();
    }
}
