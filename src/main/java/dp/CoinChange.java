package dp;

/**
 * LeetCode 322 - Coin Change
 *
 * Problem: Given coins of different denominations and a total amount, return
 * the fewest number of coins needed to make up that amount. If not possible,
 * return -1.
 *
 * Solution: DP where dp[i] is min coins for amount i.
 *
 * Time: O(amount * coins)
 * Space: O(amount)
 */
public class CoinChange {
    public int coinChange(int[] coins, int amount) {
        int max = amount + 1;
        int[] dp = new int[amount + 1];
        java.util.Arrays.fill(dp, max);
        dp[0] = 0;
        for (int coin : coins) {
            for (int i = coin; i <= amount; i++) {
                dp[i] = Math.min(dp[i], dp[i - coin] + 1);
            }
        }
        return dp[amount] > amount ? -1 : dp[amount];
    }
}
