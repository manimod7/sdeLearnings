package dp;

/**
 * Minimum deletions to make a string palindrome
 *
 * Problem: Find minimum number of deletions in a string to make it a palindrome.
 *
 * Solution: Equivalent to length - LPS (longest palindromic subsequence) using
 * LCS DP with reversed string.
 *
 * Time: O(n^2)
 * Space: O(n^2)
 */
public class MinimumDeletionsToMakePalindrome {
    public int minDeletions(String s) {
        String rev = new StringBuilder(s).reverse().toString();
        int n = s.length();
        int[][] dp = new int[n+1][n+1];
        for (int i=1;i<=n;i++)
            for (int j=1;j<=n;j++)
                if (s.charAt(i-1)==rev.charAt(j-1)) dp[i][j]=dp[i-1][j-1]+1;
                else dp[i][j]=Math.max(dp[i-1][j],dp[i][j-1]);
        int lps = dp[n][n];
        return n - lps;
    }
}
