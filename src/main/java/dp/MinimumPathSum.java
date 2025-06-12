package dp;

/**
 * LeetCode 64 - Minimum Path Sum
 *
 * Problem: Given a grid of numbers, find a path from top-left to bottom-right
 * with minimum sum, moving only right or down.
 *
 * Solution: DP grid where dp[i][j] = grid[i][j] + min(top, left).
 *
 * Time: O(m*n)
 * Space: O(m*n)
 */
public class MinimumPathSum {
    public int minPathSum(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[][] dp = new int[m][n];
        dp[0][0] = grid[0][0];
        for (int i=1;i<m;i++) dp[i][0] = dp[i-1][0] + grid[i][0];
        for (int j=1;j<n;j++) dp[0][j] = dp[0][j-1] + grid[0][j];
        for (int i=1;i<m;i++)
            for (int j=1;j<n;j++)
                dp[i][j] = grid[i][j] + Math.min(dp[i-1][j], dp[i][j-1]);
        return dp[m-1][n-1];
    }
}
