package searching;

import java.util.ArrayList;

public class NumberOfPainters {
    public int paint(int A, int B, ArrayList<Integer> C) {
        int MOD = 10000003;
        int n = C.size();

        // Find the bounds for binary search
        long low = 0, high = 0;
        for (int length : C) {
            low = Math.max(low, length); // Minimum time is max of single board
            high += length;             // Maximum time is sum of all boards
        }

        long result = high * B; // Initialize result with the worst-case time

        // Binary search for the optimal maximum time
        while (low <= high) {
            long mid = low + (high - low) / 2;

            if (isFeasible(C, A, mid)) {
                result = mid * B % MOD; // Update result if feasible
                high = mid - 1;         // Try for a smaller maximum time
            } else {
                low = mid + 1;          // Try for a larger maximum time
            }
        }

        return (int) (result % MOD);
    }

    // Check if it's feasible to paint all boards with at most A painters and max time T
    private boolean isFeasible(ArrayList<Integer> C, int A, long maxTime) {
        int paintersRequired = 1;
        long currentTime = 0;

        for (int length : C) {
            if (currentTime + length > maxTime) {
                // Assign a new painter
                paintersRequired++;
                currentTime = length;

                // If painters exceed A, not feasible
                if (paintersRequired > A) {
                    return false;
                }
            } else {
                // Accumulate the current board's length
                currentTime += length;
            }
        }

        return true;
    }
}
