package dp;

/**
 * LeetCode 1143 - Longest Common Subsequence
 *
 * Problem: Given two strings text1 and text2, return the length of their longest
 * common subsequence.
 *
 * Solution: DP table where dp[i][j] is LCS length of prefixes text1[0..i-1],
 * text2[0..j-1].
 *
 * Time: O(m*n)
 * Space: O(m*n)
 */
public class LongestCommonSubsequence {
    public int longestCommonSubsequence(String a, String b) {
        int m = a.length(), n = b.length();
        int[][] dp = new int[m+1][n+1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (a.charAt(i-1) == b.charAt(j-1))
                    dp[i][j] = dp[i-1][j-1] + 1;
                else
                    dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]);
            }
        }
        return dp[m][n];
    }
}
