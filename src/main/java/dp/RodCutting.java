package dp;

/**
 * Rod Cutting Problem
 *
 * Problem: Given a rod of length n and prices of different lengths, maximize
 * profit by cutting the rod.
 *
 * Solution: Unbounded knapsack DP where dp[i] is max profit for length i.
 *
 * Time: O(n^2)
 * Space: O(n)
 */
public class RodCutting {
    public int cutRod(int[] price, int n){
        int[] dp=new int[n+1];
        for(int i=1;i<=n;i++)
            for(int j=1;j<=i;j++)
                dp[i]=Math.max(dp[i],price[j-1]+dp[i-j]);
        return dp[n];
    }
}
