package searching;

import java.util.ArrayList;

public class LocalMaxima {
    /**
     * Finds a local peak in a sorted array.
     *
     * @param A Sorted ArrayList of integers.
     * @return The local peak element.
     */
    public int solve(ArrayList<Integer> A) {
        // Handle null or empty array
        if (A == null || A.isEmpty()) {
            throw new IllegalArgumentException("Input array is null or empty.");
        }

        int n = A.size();

        // If there's only one element, it's the peak
        if (n == 1) {
            return A.get(0);
        }

        // Check if the first element is a peak
        if (A.get(0) >= A.get(1)) {
            return A.get(0);
        }

        // Check if the last element is a peak
        if (A.get(n - 1) >= A.get(n - 2)) {
            return A.get(n - 1);
        }

        // Initialize binary search boundaries
        int left = 0;
        int right = n - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            // Fetch current, previous, and next elements
            int current = A.get(mid);
            int prev = A.get(mid - 1);
            int next = A.get(mid + 1);

            // Check if the current element is a peak
            if (current >= prev && current >= next) {
                return current;
            }

            // If the left neighbor is greater, move left
            if (prev > current) {
                right = mid - 1;
            }
            // If the right neighbor is greater, move right
            else {
                left = mid + 1;
            }
        }

        // In theory, the function should have returned within the loop
        // due to the problem constraints guaranteeing at least one peak
        // However, to satisfy the method's return type, return -1 as an indicator
        return -1;
    }
}
