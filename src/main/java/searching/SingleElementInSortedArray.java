package searching;

import java.util.ArrayList;

public class SingleElementInSortedArray {
    /**
     * Finds the single element that appears only once in a sorted array
     * where every other element appears exactly twice.
     *
     * @param A Sorted ArrayList of integers with adjacent duplicates.
     * @return The single unique integer.
     */
    public int solve(ArrayList<Integer> A) {
        int low = 0;
        int high = A.size() - 1;

        while (low < high) {
            int mid = low + (high - low) / 2;

            // Ensure mid is even
            if (mid % 2 == 1) {
                mid--;
            }

            // Compare the pair starting at mid
            if (mid + 1 < A.size() && A.get(mid).equals(A.get(mid + 1))) {
                // Unique element is in the right half
                low = mid + 2;
            } else {
                // Unique element is in the left half (including mid)
                high = mid;
            }
        }

        // When low == high, we've found the unique element
        return A.get(low);
    }
}
