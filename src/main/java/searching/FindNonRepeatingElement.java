package searching;

import java.util.ArrayList;

public class FindNonRepeatingElement {
    /**
     * Finds the unique element in an unsorted array where every other element appears twice,
     * and duplicates are adjacent. Uses a modified binary search approach with O(log N) time.
     *
     * @param A ArrayList of integers with adjacent duplicates.
     * @return The unique element.
     */
    public int findUniqueUnsorted(ArrayList<Integer> A) {
        int low = 0;
        int high = A.size() - 1;

        while (low < high) {
            int mid = low + (high - low) / 2;

            // Ensure mid is even to correctly identify pairs
            if (mid % 2 == 1) {
                mid--;
            }

            // Check if the current pair is valid
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
