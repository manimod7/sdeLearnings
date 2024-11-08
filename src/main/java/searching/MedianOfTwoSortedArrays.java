package searching;

import java.util.ArrayList;

public class MedianOfTwoSortedArrays {
    public int solve(ArrayList<Integer> A, ArrayList<Integer> B) {
        int m = A.size();
        int n = B.size();

        // Ensure A is the smaller array
        if (m > n) {
            return solve(B, A);
        }

        int low = 0, high = m;
        while (low <= high) {
            int partitionA = (low + high) / 2;
            int partitionB = (m + n + 1) / 2 - partitionA;

            int maxLeftA = (partitionA == 0) ? Integer.MIN_VALUE : A.get(partitionA - 1);
            int minRightA = (partitionA == m) ? Integer.MAX_VALUE : A.get(partitionA);

            int maxLeftB = (partitionB == 0) ? Integer.MIN_VALUE : B.get(partitionB - 1);
            int minRightB = (partitionB == n) ? Integer.MAX_VALUE : B.get(partitionB);

            if (maxLeftA <= minRightB && maxLeftB <= minRightA) {
                // We found the correct partition
                if ((m + n) % 2 == 0) {
                    return (int) Math.floor((double)(Math.max(maxLeftA, maxLeftB) + Math.min(minRightA, minRightB)) / 2);
                } else {
                    return Math.max(maxLeftA, maxLeftB);
                }
            } else if (maxLeftA > minRightB) {
                high = partitionA - 1;
            } else {
                low = partitionA + 1;
            }
        }
        return 0;
    }
}
