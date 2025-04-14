package heaps;

import java.util.Collections;
import java.util.PriorityQueue;

public class LastStoneWeight {
    public int lastStoneWeight(int[] stones) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());

        for(int num:stones) {
            pq.offer(num);
        }
        while(!pq.isEmpty()) {
            int y = pq.poll();
            if(pq.isEmpty())
                return y;
            int x = pq.poll();
            if(x!=y)
                pq.offer(y-x);
        }
        return 0;
    }
}
