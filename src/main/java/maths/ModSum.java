package maths;

import java.util.ArrayList;
import java.util.HashMap;

public class ModSum {
    public int solve(ArrayList<Integer> A) {
        final int MOD = 1000000007;
        HashMap<Integer, Integer> frequency = new HashMap<>();
        long sumMod = 0;

        // Calculate frequency of each element
        for (int num : A) {
            frequency.put(num, frequency.getOrDefault(num, 0) + 1);
        }

        // Calculate sum of moduli for unique pairs, considering frequency
        for (int a : frequency.keySet()) {
            for (int b : frequency.keySet()) {
                int modResult = a % b;
                long countContribution = (long) modResult * frequency.get(a) * frequency.get(b);
                sumMod = (sumMod + countContribution) % MOD;
            }
        }

        return (int) sumMod;
    }
}
