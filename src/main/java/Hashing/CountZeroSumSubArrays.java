package Hashing;

import java.util.ArrayList;
import java.util.HashMap;

public class CountZeroSumSubArrays {
    public long countZeroSumSubarrays(ArrayList<Integer> A) {
        // Running prefix‐sum
        long sum = 0;

        // freq.get(s) = how many times we have seen prefix‐sum == s so far.
        // Initialize freq(0) = 1 so that any prefixSum == 0 at index i
        // counts the subarray [0..i].
        HashMap<Long, Integer> freq = new HashMap<>();
        freq.put(0L, 1);

        long count = 0;
        for (int x : A) {
            sum += x;

            // If we've seen this prefix‐sum before, say 'f' times,
            // that contributes 'f' new zero‐sum subarrays ending here.
            if (freq.containsKey(sum)) {
                count += freq.get(sum);
            }

            // Record that we've now seen 'sum' one more time.
            freq.put(sum, freq.getOrDefault(sum, 0) + 1);
        }

        return count;
    }
}
