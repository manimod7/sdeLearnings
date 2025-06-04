package bitManipulation;

import java.util.ArrayList;

public class SubArrayOr {
    public int solve(ArrayList<Integer> A) {
        final long MOD = 1_000_000_007L;
        int N = A.size();

        // Total number of subarrays = N * (N + 1) / 2
        long totalSubarrays = (long) N * (N + 1) / 2;

        // Precompute powers of 2 up to bit 30 (since A[i] <= 10^8 < 2^27)
        long[] pow2 = new long[31];
        pow2[0] = 1;
        for (int i = 1; i <= 30; i++) {
            pow2[i] = (pow2[i - 1] * 2) % MOD;
        }

        long answer = 0;

        // For each bit position 0..30, count how many subarrays have this bit = 1 in their OR
        for (int bit = 0; bit <= 30; bit++) {
            long zeroSegmentSubarrays = 0L;
            long lengthZeroRun = 0L;
            int mask = 1 << bit;

            // Scan A; every time we see an element with this bit = 0, we extend the "zero run"
            // If the bit = 1, we finish the current zero-run and add its subarray count.
            for (int x : A) {
                if ((x & mask) == 0) {
                    lengthZeroRun++;
                } else {
                    zeroSegmentSubarrays += lengthZeroRun * (lengthZeroRun + 1) / 2;
                    lengthZeroRun = 0;
                }
            }
            // If the array ended with a run of zeros for this bit, account for it
            zeroSegmentSubarrays += lengthZeroRun * (lengthZeroRun + 1) / 2;

            // Number of subarrays where this bit is set in the OR
            long subarraysWithBit = totalSubarrays - zeroSegmentSubarrays;
            subarraysWithBit %= MOD;
            if (subarraysWithBit < 0) {
                subarraysWithBit += MOD;
            }

            // Contribution of this bit to the final sum
            answer = (answer + (subarraysWithBit * pow2[bit]) % MOD) % MOD;
        }

        return (int) answer;
    }
}
