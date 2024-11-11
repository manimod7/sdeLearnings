package maths;

import java.util.ArrayList;

public class PairSumDivisibleByM {

    // Given an array of integers A and an integer B, find and return the number of pairs in A whose
    // sum is divisible by B. Since the answer may be large, return the answer modulo (109 + 7).
    //Note: Ensure to handle integer overflow when performing the calculations.

    public int solve(ArrayList<Integer> A, int B) {
        int n = A.size();
        int MOD = 1000000007;
        int[] modArray = new int[B];

        // Count remainders of elements in A when divided by B
        for (int num : A) {
            modArray[num % B]++;
        }

        long ans = 0;  // Use long to handle larger intermediate values

        // Special case: count pairs where both elements give remainder 0
        ans = ((long)modArray[0] * (modArray[0] - 1) / 2) % MOD;  // Cast to long to avoid overflow during multiplication

        // Count pairs between different remainder groups i and B-i
        for (int i = 1; i <= B / 2 && i != B - i; i++) {
            ans = (ans + (long)modArray[i] * modArray[B - i] % MOD) % MOD;
        }

        // Special case: count pairs within the same remainder group i where i == B-i
        if (B % 2 == 0) { // B is even, check the middle group
            int j = B / 2;
            ans = (ans + ((long)modArray[j] * (modArray[j] - 1) / 2) % MOD) % MOD;
        }

        return (int) ans;
    }
}
