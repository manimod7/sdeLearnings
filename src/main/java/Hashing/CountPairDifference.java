package Hashing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CountPairDifference {
    public int solve(ArrayList<Integer> A, int B) {
        long MOD = 1_000_000_007L;

        HashMap<Integer, Long> freq = new HashMap<>();
        for (int x : A) {
            freq.put(x, freq.getOrDefault(x, 0L) + 1L);
        }

        long count = 0;

        if (B == 0) {
            for (long c : freq.values()) {
                if (c > 1) {
                    count = (count + (c * (c - 1)) % MOD) % MOD;
                }
            }
        }
        else {
            for (Map.Entry<Integer, Long> e : freq.entrySet()) {
                int v = e.getKey();
                long c1 = e.getValue();
                long c2 = freq.getOrDefault(v - B, 0L);
                if (c2 > 0) {
                    count = (count + (c1 * c2) % MOD) % MOD;
                }
            }
        }

        return (int) count;
    }
}
