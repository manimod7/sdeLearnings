package dp;

/**
 * LeetCode 63 - Unique Paths II
 *
 * Problem: Same as Unique Paths but with obstacles (1s) blocking movement.
 *
 * Solution: DP grid ignoring cells with obstacles.
 *
 * Time: O(m*n)
 * Space: O(m*n)
 */
public class UniquePathsII {
    public int uniquePathsWithObstacles(int[][] obs) {
        int m = obs.length, n = obs[0].length;
        int[][] dp = new int[m][n];
        if (obs[0][0]==1) return 0;
        dp[0][0] = 1;
        for (int i=0;i<m;i++)
            for (int j=0;j<n;j++)
                if (obs[i][j]==0) {
                    if (i>0) dp[i][j]+=dp[i-1][j];
                    if (j>0) dp[i][j]+=dp[i][j-1];
                } else dp[i][j]=0;
        return dp[m-1][n-1];
    }
}
