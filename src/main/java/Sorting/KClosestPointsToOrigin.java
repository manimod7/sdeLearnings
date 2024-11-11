package Sorting;

import java.util.Arrays;
import java.util.PriorityQueue;

public class KClosestPointsToOrigin {

    // Leetcode 973. K Closest Points to Origin

    public int[][] kClosest(int[][] points, int k) {

        Arrays.sort(points,(a, b)->{
            int xa = a[0], ya=a[1], xb = b[0], yb = b[1];
            return xa*xa + ya*ya -(xb*xb + yb*yb);
        });

        int[][] ans = new int [k][2];

        System.arraycopy(points, 0, ans, 0, k);
        return ans;
    }

    public int[][] kClosestUsingPriorityQueue(int[][] points, int k) {
        // Max heap to store the k closest points based on distance
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>(
                (a, b) -> Integer.compare((b[0] * b[0] + b[1] * b[1]), (a[0] * a[0] + a[1] * a[1]))
        );

        // Add points to the max heap
        for (int[] point : points) {
            maxHeap.offer(point);
            // Remove the farthest point if we have more than k points
            if (maxHeap.size() > k) {
                maxHeap.poll();
            }
        }

        // Collect the k closest points from the max heap
        int[][] result = new int[k][2];
        for (int i = 0; i < k; i++) {
            result[i] = maxHeap.poll();
        }

        return result;
    }
}
