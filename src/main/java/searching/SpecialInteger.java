package searching;

import java.util.ArrayList;

public class SpecialInteger {
    public int solve(ArrayList<Integer> A, int B) {
        if (A == null || A.isEmpty()) {
            return 0;
        }

        int N = A.size();
        int low = 1;
        int high = N;
        int result = 0;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (isValid(A, N, mid, B)) {
                result = mid; // Current K is valid, try to find a larger K
                low = mid + 1;
            } else {
                high = mid - 1; // Current K is invalid, try smaller K
            }
        }

        return result;
    }

    /**
     * Helper method to check if all subarrays of size K have a sum <= B.
     *
     * @param A    The array of integers.
     * @param N    The size of the array.
     * @param K    The subarray size to check.
     * @param B    The maximum allowed sum for any subarray of size K.
     * @return     True if all subarrays of size K have sum <= B, else False.
     */
    private boolean isValid(ArrayList<Integer> A, int N, int K, int B) {
        // Compute the sum of the first K elements
        long windowSum = 0;
        for (int i = 0; i < K; i++) {
            windowSum += A.get(i);
            if (A.get(i) > B) {
                return false; // Early exit if any single element exceeds B
            }
        }

        if (windowSum > B) {
            return false;
        }

        // Slide the window from K to N-1
        for (int i = K; i < N; i++) {
            windowSum += A.get(i) - A.get(i - K);
            if (A.get(i) > B) {
                return false; // Early exit if any single element exceeds B
            }
            if (windowSum > B) {
                return false;
            }
        }

        return true;
    }
}
