package dp;

/**
 * LeetCode 115 - Distinct Subsequences
 *
 * Problem: Count distinct subsequences of string s which equals string t.
 *
 * Solution: DP where dp[i][j] = ways to form t[0..j) from s[0..i).
 *
 * Time: O(m*n)
 * Space: O(m*n)
 */
public class DistinctSubsequences {
    public int numDistinct(String s, String t) {
        int m=s.length(), n=t.length();
        int[][] dp=new int[m+1][n+1];
        for(int i=0;i<=m;i++) dp[i][0]=1;
        for(int i=1;i<=m;i++)
            for(int j=1;j<=n;j++){
                dp[i][j]=dp[i-1][j];
                if(s.charAt(i-1)==t.charAt(j-1)) dp[i][j]+=dp[i-1][j-1];
            }
        return dp[m][n];
    }
}
