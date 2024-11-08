package searching;

import java.util.ArrayList;
import java.util.List;

public class LeftMostRightMost {
    /**
     * Finds the leftmost and rightmost indices of B in sorted array A.
     * If B is not found, returns [-1, -1].
     *
     * @param A Sorted list of integers.
     * @param B Target integer to find.
     * @return ArrayList containing the leftmost and rightmost indices.
     */
    public ArrayList<Integer> searchRange(final List<Integer> A, int B) {
        ArrayList<Integer> ans = new ArrayList<>();
        ans.add(findLeftMost(A, B));
        ans.add(findRightMost(A, B));
        return ans;
    }

    /**
     * Finds the leftmost (first) index of B in A using binary search.
     *
     * @param A Sorted list of integers.
     * @param B Target integer to find.
     * @return Leftmost index of B, or -1 if not found.
     */
    public int findLeftMost(List<Integer> A, int B) {
        int leftMost = -1;
        int left = 0, right = A.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (B > A.get(mid)) {
                left = mid + 1;
            }
            else if (B == A.get(mid)) {
                leftMost = mid;
                right = mid - 1; // Continue searching to the left
            }
            else { // B < A.get(mid)
                right = mid - 1;
            }
        }
        return leftMost;
    }

    /**
     * Finds the rightmost (last) index of B in A using binary search.
     *
     * @param A Sorted list of integers.
     * @param B Target integer to find.
     * @return Rightmost index of B, or -1 if not found.
     */
    public int findRightMost(List<Integer> A, int B) {
        int rightMost = -1;
        int left = 0, right = A.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (B > A.get(mid)) {
                left = mid + 1;
            }
            else if (B == A.get(mid)) {
                rightMost = mid;
                left = mid + 1; // Continue searching to the right
            }
            else { // B < A.get(mid)
                right = mid - 1;
            }
        }
        return rightMost;
    }
}

