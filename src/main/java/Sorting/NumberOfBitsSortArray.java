package Sorting;

import java.util.Arrays;
import java.util.Comparator;

public class NumberOfBitsSortArray {
    public int[] sortByBits(int[] arr) {
        Integer[] arrInteger = Arrays.stream(arr).boxed().toArray(Integer[]::new);
        // Sort the array using a custom comparator
        Arrays.sort(arrInteger, new BitComparator());
        return arr; // Return the sorted array
    }

    // Custom Comparator class to sort by number of 1 bits
    private static class BitComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer a, Integer b) {
            int countA = Integer.bitCount(a); // Count 1-bits in a
            int countB = Integer.bitCount(b); // Count 1-bits in b

            if (countA == countB) {
                // If bit counts are the same, return smaller number
                return a - b;
            } else {
                // Otherwise, sort by the bit count
                return countA - countB;
            }
        }
    }
}
