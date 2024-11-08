package searching;

import java.util.ArrayList;

public class MatrixSearch {
    /**
     * Searches for integer B in a sorted matrix A.
     *
     * @param A Sorted matrix represented as an ArrayList of ArrayLists of Integers.
     * @param B The target integer to search for.
     * @return 1 if B is present in A, else 0.
     */
    public int searchMatrix(ArrayList<ArrayList<Integer>> A, int B) {
        // Handle empty matrix
        if (A == null || A.isEmpty()) {
            return 0;
        }

        int N = A.size();         // Number of rows
        int M = A.get(0).size();  // Number of columns

        // Handle case where matrix has no columns
        if (M == 0) {
            return 0;
        }

        int left = 0;
        int right = N * M - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            // Map mid to row and column
            int row = mid / M;
            int col = mid % M;

            int midVal = A.get(row).get(col);

            if (midVal == B) {
                return 1; // Found target
            } else if (midVal < B) {
                left = mid + 1; // Search in the right half
            } else {
                right = mid - 1; // Search in the left half
            }
        }

        // Target not found
        return 0;
    }
}
