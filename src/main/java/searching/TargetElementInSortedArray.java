package searching;

import java.util.ArrayList;

public class TargetElementInSortedArray {
    /**
     * Finds the index of target B in sorted array A.
     * If B is not present, returns the index of the smallest element greater than or equal to B.
     * If no such element exists, returns the length of the array.
     *
     * @param A Sorted ArrayList of integers.
     * @param B Target integer to search for.
     * @return 0-based index as per the problem statement.
     */
    public int searchInsert(ArrayList<Integer> A, int B) {
        if (A == null || A.size() == 0) {
            // If the array is empty, the insertion index is 0
            return 0;
        }

        int left = 0;
        int right = A.size() - 1;
        int result = A.size(); // Default to N if no greater element is found

        while (left <= right) {
            int mid = left + (right - left) / 2;
            int midVal = A.get(mid);

            if (midVal == B) {
                // Target found
                return mid;
            } else if (midVal < B) {
                // Target is in the right half
                left = mid + 1;
            } else {
                // Potential candidate found; continue searching in the left half
                result = mid;
                right = mid - 1;
            }
        }

        // If target not found, 'result' holds the index of the smallest element >= B
        return result;
    }
}
