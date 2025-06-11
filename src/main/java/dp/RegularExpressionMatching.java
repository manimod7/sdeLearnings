package dp;

/**
 * LeetCode 10 - Regular Expression Matching
 *
 * Problem: Implement regex matching with support for '.' and '*'.
 *
 * Solution: DP where dp[i][j] means s[0..i) matches p[0..j).
 *
 * Time: O(m*n)
 * Space: O(m*n)
 */
public class RegularExpressionMatching {
    public boolean isMatch(String s, String p) {
        int m=s.length(), n=p.length();
        boolean[][] dp=new boolean[m+1][n+1];
        dp[0][0]=true;
        for(int j=1;j<=n;j++) if(p.charAt(j-1)=='*') dp[0][j]=dp[0][j-2];
        for(int i=1;i<=m;i++)
            for(int j=1;j<=n;j++){
                char pc=p.charAt(j-1);
                if(pc=='*'){
                    dp[i][j]=dp[i][j-2]||((p.charAt(j-2)=='.'||p.charAt(j-2)==s.charAt(i-1))&&dp[i-1][j]);
                }else if(pc=='.'||pc==s.charAt(i-1)) dp[i][j]=dp[i-1][j-1];
            }
        return dp[m][n];
    }
}
