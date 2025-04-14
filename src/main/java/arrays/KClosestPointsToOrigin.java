package arrays;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class KClosestPointsToOrigin {

    public int[][] kClosest(int[][] points, int k) {


        // Sort points based on their distance from (0,0)
        Arrays.sort(points, (int[] a, int[] b) ->
                Integer.compare(a[0] * a[0] + a[1] * a[1], b[0] * b[0] + b[1] * b[1])
        );

        // Return first k points
        return Arrays.copyOfRange(points, 0, k);
    }

    public int[][] kClosest2(int[][] points, int k) {
        // Max Heap based on distance
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>(
                (a, b) -> Integer.compare(
                        (b[0]*b[0] + b[1]*b[1]),
                        (a[0]*a[0] + a[1]*a[1])
                )
        );

        for (int[] point : points) {
            maxHeap.offer(point);
            if (maxHeap.size() > k) {
                maxHeap.poll(); // remove farthest point
            }
        }

        int[][] result = new int[k][2];
        for (int i = 0; i < k; i++) {
            result[i] = maxHeap.poll();
        }
        return result;
    }

    public int[][] kClosest3(int[][] points, int k) {
        Arrays.sort(points, new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                int distA = a[0]*a[0] + a[1]*a[1];
                int distB = b[0]*b[0] + b[1]*b[1];
                return Integer.compare(distA, distB);
            }
        });

        return Arrays.copyOfRange(points, 0, k);
    }
}
