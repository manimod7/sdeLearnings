package searching;

import java.util.List;

public class RotatedSortedArraySearch {
    public int search(final List<Integer> A, int B) {
        int left = 0;
        int right = A.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            // Check if mid element is the target
            if (A.get(mid) == B) {
                return mid;
            }
            // Determine which part is sorted
            if (A.get(left) <= A.get(mid)) {
                // Left part is sorted
                if (A.get(left) <= B && B < A.get(mid)) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else {
                // Right part is sorted
                if (A.get(mid) < B && B <= A.get(right)) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        // Target not found
        return -1;
    }
}
