package dp;

/**
 * Classic 0/1 Knapsack Problem
 *
 * Problem: Given weights and values of n items with capacity W, find maximum
 * value that can be put in the knapsack.
 *
 * Solution: DP where dp[i][w] is best value using first i items with capacity w.
 *
 * Time: O(n*W)
 * Space: O(n*W)
 */
public class Knapsack01 {
    public int knapSack(int W, int[] wt, int[] val) {
        int n = wt.length;
        int[][] dp = new int[n+1][W+1];
        for (int i = 1; i <= n; i++) {
            for (int w = 1; w <= W; w++) {
                dp[i][w] = dp[i-1][w];
                if (wt[i-1] <= w)
                    dp[i][w] = Math.max(dp[i][w], val[i-1] + dp[i-1][w-wt[i-1]]);
            }
        }
        return dp[n][W];
    }
}
