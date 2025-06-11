package dp;

/**
 * LeetCode 97 - Interleaving String
 *
 * Problem: Given strings s1, s2, s3, determine if s3 is formed by interleaving
 * s1 and s2.
 *
 * Solution: DP where dp[i][j] indicates s3[0..i+j-1] formed by s1[0..i-1] and
 * s2[0..j-1].
 *
 * Time: O(m*n)
 * Space: O(m*n)
 */
public class InterleavingString {
    public boolean isInterleave(String s1, String s2, String s3) {
        int m=s1.length(), n=s2.length();
        if(m+n!=s3.length()) return false;
        boolean[][] dp=new boolean[m+1][n+1];
        dp[0][0]=true;
        for(int i=0;i<=m;i++)
            for(int j=0;j<=n;j++){
                if(i>0) dp[i][j]|=dp[i-1][j]&&s1.charAt(i-1)==s3.charAt(i+j-1);
                if(j>0) dp[i][j]|=dp[i][j-1]&&s2.charAt(j-1)==s3.charAt(i+j-1);
            }
        return dp[m][n];
    }
}
