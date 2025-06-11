package dp;

/**
 * LeetCode 518 - Coin Change II
 *
 * Problem: Count the number of combinations to make up a certain amount given
 * unlimited coins of given denominations.
 *
 * Solution: Unbounded knapsack DP where dp[i] is ways to make amount i.
 * Iterate coins outer to avoid overcount.
 *
 * Time: O(amount * coins)
 * Space: O(amount)
 */
public class CoinChange2 {
    public int change(int amount, int[] coins) {
        int[] dp = new int[amount+1];
        dp[0] = 1;
        for (int coin : coins) {
            for (int i = coin; i <= amount; i++) {
                dp[i] += dp[i-coin];
            }
        }
        return dp[amount];
    }
}
