package searching;

import java.util.ArrayList;
import java.util.Collections;

public class AggressiveCows {
    public int solve(ArrayList<Integer> A, int B) {
        // Step 1: Sort the array
        Collections.sort(A);

        int n = A.size();
        int low = 1; // Minimum possible distance
        int high = A.get(n - 1) - A.get(0); // Maximum possible distance
        int result = 0;

        // Step 2: Binary search on the minimum distance
        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (isFeasible(A, B, mid)) {
                result = mid; // Update result to the current mid
                low = mid + 1; // Try for a larger minimum distance
            } else {
                high = mid - 1; // Try for a smaller minimum distance
            }
        }

        return result;
    }

    // Helper function to check if a distance is feasible
    private boolean isFeasible(ArrayList<Integer> A, int B, int minDist) {
        int count = 1; // Place the first cow in the first stall
        int lastPosition = A.get(0); // Track the position of the last placed cow

        for (int i = 1; i < A.size(); i++) {
            if (A.get(i) - lastPosition >= minDist) {
                count++; // Place another cow
                lastPosition = A.get(i); // Update lastPosition

                if (count == B) {
                    return true; // All cows are placed
                }
            }
        }

        return false; // Not all cows could be placed
    }
}
