package searching;

import java.util.ArrayList;

public class MatrixMedian {
    public int findMedian(ArrayList<ArrayList<Integer>> A) {
        int N = A.size();
        int M = A.get(0).size();

        // Find the minimum and maximum elements in the matrix
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        for (ArrayList<Integer> integers : A) {
            min = Math.min(min, integers.get(0));
            max = Math.max(max, integers.get(M - 1));
        }

        // The desired position of the median
        int desiredCount = (N * M) / 2;

        // Binary search on the value range [min, max]
        while (min < max) {
            int mid = min + (max - min) / 2;
            int count = 0;

            // Count how many numbers are less than or equal to `mid`
            for (int i = 0; i < N; i++) {
                count += countLessEqual(A.get(i), mid);
            }

            // Adjust the range based on the count
            if (count <= desiredCount) {
                min = mid + 1;
            } else {
                max = mid;
            }
        }

        return min;
    }

    // Helper function to count numbers less than or equal to `target` in a sorted list
    private int countLessEqual(ArrayList<Integer> row, int target) {
        int low = 0;
        int high = row.size();

        // Binary search to find the count of elements <= target
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (row.get(mid) <= target) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }

        return low;
    }
}
