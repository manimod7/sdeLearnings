package twoPointers;

import java.util.ArrayList;
import java.util.Collections;

public class SubArrayWithGivenSum {
    public ArrayList<Integer> solve(ArrayList<Integer> A, int B) {
        int left = 0, right = 0; // Two pointers for the sliding window
        int n = A.size();
        long currentSum = 0; // Use long to handle large sums due to constraints

        while (right < n) {
            // Add the current element to the window
            currentSum += A.get(right);

            // If the current sum exceeds B, shrink the window from the left
            while (currentSum > B && left <= right) {
                currentSum -= A.get(left);
                left++;
            }

            // If currentSum equals B, return the subarray
            if (currentSum == B) {
                return new ArrayList<>(A.subList(left, right + 1)); // Include right
            }

            // Expand the window by moving right pointer
            right++;
        }

        // If no subArray is found, return [-1]
        return new ArrayList<>(Collections.singletonList(-1));
    }
}
