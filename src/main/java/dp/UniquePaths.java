package dp;

/**
 * LeetCode 62 - Unique Paths
 *
 * Problem: A robot moves in an m x n grid from top-left to bottom-right only
 * right or down. Count unique paths.
 *
 * Solution: DP grid where dp[i][j] = dp[i-1][j] + dp[i][j-1].
 *
 * Time: O(m*n)
 * Space: O(m*n) (can optimize to O(n))
 */
public class UniquePaths {
    public int uniquePaths(int m, int n) {
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) dp[i][0] = 1;
        for (int j = 0; j < n; j++) dp[0][j] = 1;
        for (int i = 1; i < m; i++)
            for (int j = 1; j < n; j++)
                dp[i][j] = dp[i-1][j] + dp[i][j-1];
        return dp[m-1][n-1];
    }
}
