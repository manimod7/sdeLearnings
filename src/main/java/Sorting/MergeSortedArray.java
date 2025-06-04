package Sorting;

//Leetcode 88

import java.util.ArrayList;
import java.util.List;

public class MergeSortedArray {

    public ArrayList<Integer> solve(final List<Integer> A, final List<Integer> B) {
        int n = A.size(), m = B.size();
        ArrayList<Integer> merged = new ArrayList<>(n + m);

        int i = 0, j = 0;
        // Merge until one list is exhausted
        while (i < n && j < m) {
            if (A.get(i) <= B.get(j)) {
                merged.add(A.get(i));
                i++;
            } else {
                merged.add(B.get(j));
                j++;
            }
        }
        // Append any remaining elements from A
        while (i < n) {
            merged.add(A.get(i));
            i++;
        }
        // Append any remaining elements from B
        while (j < m) {
            merged.add(B.get(j));
            j++;
        }

        return merged;
    }
}
