package Sorting;

import java.util.ArrayList;

public class SmallestNumber {
    public ArrayList<Integer> smallestNumber(ArrayList<Integer> A) {
        // Count frequency of each digit 0..9
        int[] freq = new int[10];
        for (int d : A) {
            freq[d]++;
        }
        // Reconstruct the smallest number by placing digits from 0 to 9
        ArrayList<Integer> result = new ArrayList<>(A.size());
        for (int digit = 0; digit <= 9; digit++) {
            int count = freq[digit];
            while (count-- > 0) {
                result.add(digit);
            }
        }
        return result;
    }

}
