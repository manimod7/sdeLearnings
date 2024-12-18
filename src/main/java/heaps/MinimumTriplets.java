package heaps;

import java.util.*;

public class MinimumTriplets {
    public static int solve(ArrayList<Integer> A, int B) {
        // Step 1: Sort the array to handle increasing triplet sums
        Collections.sort(A);

        // Min-Heap to store triplet sums and their indices
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((x, y) -> Integer.compare(x[0], y[0]));

        int n = A.size();

        // Step 2: Push the first triplet into the heap
        minHeap.offer(new int[]{A.get(0) + A.get(1) + A.get(2), 0, 1, 2});

        int count = 0;
        int result = 0;

        // Set to track visited states
        Set<String> visited = new HashSet<>();

        while (!minHeap.isEmpty() && count < B) {
            int[] current = minHeap.poll();
            result = current[0];
            int i = current[1], j = current[2], k = current[3];
            count++;

            // Generate the next triplets by systematically incrementing indices
            if (k + 1 < n) { // Increment k
                String key = i + "," + j + "," + (k + 1);
                if (!visited.contains(key)) {
                    minHeap.offer(new int[]{A.get(i) + A.get(j) + A.get(k + 1), i, j, k + 1});
                    visited.add(key);
                }
            }
            if (j + 1 < k && k < n) { // Increment j and reset k
                String key = i + "," + (j + 1) + "," + k;
                if (!visited.contains(key)) {
                    minHeap.offer(new int[]{A.get(i) + A.get(j + 1) + A.get(k), i, j + 1, k});
                    visited.add(key);
                }
            }
            if (i + 1 < j && j < n) { // Increment i and reset j, k
                String key = (i + 1) + "," + j + "," + k;
                if (!visited.contains(key)) {
                    minHeap.offer(new int[]{A.get(i + 1) + A.get(j) + A.get(k), i + 1, j, k});
                    visited.add(key);
                }
            }
        }

        return result;
    }
    public static void main (String[] args) {

        ArrayList<Integer> nums = new ArrayList<>(Arrays.asList(1,2,3,4));
        int B = 3;
        MinimumTriplets.solve(nums, B);

    }
}
