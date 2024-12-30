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

    static int countWays2(int n) {
        int[] dp = new int[n + 1];

        // Base cases
        dp[0] = 1;
        dp[1] = 1;

        for (int i = 2; i <= n; i++)
            dp[i] = dp[i - 1] + dp[i - 2];

        return dp[n];
    }
}
