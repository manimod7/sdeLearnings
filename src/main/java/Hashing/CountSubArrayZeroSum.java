package Hashing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CountSubArrayZeroSum {
    public int solve(ArrayList<Integer> A) {
        final int MOD = 1_000_000_007;
        Map<Long, Long> freq = new HashMap<>();

        freq.put(0L, 1L);

        long count = 0;
        long prefixSum = 0;
        for (int x : A) {
            prefixSum += x;


            if (freq.containsKey(prefixSum)) {
                count = (count + freq.get(prefixSum)) % MOD;
            }


            freq.put(prefixSum, freq.getOrDefault(prefixSum, 0L) + 1);
        }

        return (int) count;
    }
}
