package dp;

/**
 * LeetCode 72 - Edit Distance
 *
 * Problem: Given two strings word1 and word2, return the minimum number of
 * operations required to convert word1 to word2. Operations are insert, delete,
 * or replace a character.
 *
 * Solution: DP table where dp[i][j] is edit distance between prefixes of
 * length i and j. Transition considers insert, delete, replace.
 *
 * Time: O(m*n)
 * Space: O(m*n)
 */
public class EditDistance {
    public int minDistance(String a, String b) {
        int m = a.length(), n = b.length();
        int[][] dp = new int[m+1][n+1];
        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (a.charAt(i-1) == b.charAt(j-1)) dp[i][j] = dp[i-1][j-1];
                else dp[i][j] = 1 + Math.min(dp[i-1][j-1], Math.min(dp[i-1][j], dp[i][j-1]));
            }
        }
        return dp[m][n];
    }
}
