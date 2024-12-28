package dp;

import java.util.Arrays;

public class ClimbingStairsToReachTheTop {
    static int countWaysRec(int n, int[] memo) {

        // Base cases
        if (n == 0 || n == 1)
            return 1;

        // if the result for this subproblem is
        // already computed then return it
        if (memo[n] != -1)
            return memo[n];

        return memo[n] = countWaysRec(n - 1, memo) +
                countWaysRec(n - 2, memo);
    }

    static int countWays(int n) {

        // Memoization array to store the results
        int[] memo = new int[n + 1];
        Arrays.fill(memo, -1);
        return countWaysRec(n, memo);
    }
}
