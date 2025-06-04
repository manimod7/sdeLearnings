package Sorting;

import java.util.ArrayList;

public class CountSort2 {
    public ArrayList<Integer> solve(ArrayList<Integer> A) {
        if (A == null || A.isEmpty()) {
            return A;
        }
        // Find the maximum value to size the count array
        int maxVal = 0;
        for (int x : A) {
            if (x > maxVal) {
                maxVal = x;
            }
        }

        // Build count array
        int[] count = new int[maxVal + 1];
        for (int x : A) {
            count[x]++;
        }

        // Reconstruct the sorted list
        ArrayList<Integer> sorted = new ArrayList<>(A.size());
        for (int value = 0; value <= maxVal; value++) {
            int freq = count[value];
            for (int i = 0; i < freq; i++) {
                sorted.add(value);
            }
        }
        return sorted;
    }
}
