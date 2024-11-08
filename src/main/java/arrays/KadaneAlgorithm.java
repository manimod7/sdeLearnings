package arrays;

import java.util.List;

public class KadaneAlgorithm {
    public int maxSubArray(final List<Integer> A) {
        if (A == null || A.isEmpty()) {
            return 0;
        }

        // Initialize with the first element since we need to handle negative numbers as well
        long maxEndingHere = A.get(0);
        long maxSoFar = A.get(0);

        for (int i = 1; i < A.size(); i++) {
            // Convert A.get(i) to long to ensure all arithmetic operations are done using long arithmetic
            maxEndingHere = Math.max(A.get(i), maxEndingHere + A.get(i));
            maxSoFar = Math.max(maxSoFar, maxEndingHere);
        }

        return (int)maxSoFar;
    }

}
